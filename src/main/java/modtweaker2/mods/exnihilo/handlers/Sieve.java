package modtweaker2.mods.exnihilo.handlers;

import static modtweaker2.helpers.InputHelper.isABlock;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IngredientAny;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.SiftingResult;
import exnihilo.utils.ItemInfo;

@ZenClass("mods.exnihilo.Sieve")
public class Sieve {
    
    public static final String name = "ExNihilo Sieve";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	// Adding a Ex Nihilo Sieve recipe
	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, int rarity) {
        if(input == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s recipe.", name));
            return;
        }
        
		addRecipe(input, new IItemStack[] { output }, new int[] { rarity });
	}

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack[] output, int[] rarity) {
        if(input == null || output == null || rarity == null) {
            LogHelper.logError(String.format("Required parameters missing for %s recipe.", name));
            return;
        }
        
        if(!isABlock(input)) {
            LogHelper.logError(String.format("Input item for %s recipe must be a block.", name));
            return;
        }
        
        if(output.length == 0 || output.length != rarity.length) {
            LogHelper.logError(String.format("Number of items in arrays are different or null (%d, %d)", output.length, rarity.length));
            return;
        }
        
        Map<ItemInfo, ArrayList<SiftingResult>> recipes = new HashMap<ItemInfo, ArrayList<SiftingResult>>();
        ArrayList<SiftingResult> list = new ArrayList<SiftingResult>();
        ItemInfo itemInfo = new ItemInfo(toStack(input));
        recipes.put(itemInfo, list);
        
        for(int i = 0; i < output.length; i++) {
            ItemStack out = toStack(output[i]);
            list.add(new SiftingResult(out.getItem(), out.getItemDamage(), rarity[i]));
        }
        
        MineTweakerAPI.apply(new Add(recipes));
	}

	private static class Add extends BaseMapAddition<ItemInfo, ArrayList<SiftingResult>> {

		public Add(Map<ItemInfo, ArrayList<SiftingResult>> recipes) {
		    super(Sieve.name, SieveRegistry.getSiftables(), recipes);
		}
		
		@Override
		public void apply() {
            for(Entry<ItemInfo, ArrayList<SiftingResult>> entry : recipes.entrySet()) {
                if(!map.containsKey(entry.getKey())) {
                    // no recipe for input available, add new entry
                    map.put(entry.getKey(), new ArrayList<SiftingResult>(entry.getValue()));
                } else {
                    // recipes already available, add to present list
                    ArrayList<SiftingResult> list = map.get(entry.getKey());
                    
                    // store old SiftingResult objects if present
                    ArrayList<SiftingResult> backup = new ArrayList<SiftingResult>();
                    for(SiftingResult recipe : entry.getValue()) {
                        backup.addAll(removeFromList(list, recipe));
                    }
                    
                    if(!backup.isEmpty()) {
                        LogHelper.logWarning(String.format("Overwritten %d %s recipe outputs for %s.", backup.size(), name, LogHelper.getStackDescription(entry.getKey().getStack())));
                        overwritten.put(entry.getKey(), backup);
                    }
                    
                    // add new SiftingResult objects
                    list.addAll(entry.getValue());
                }
                
                successful.put(entry.getKey(), entry.getValue());
            }
		}
		
		@Override
		public void undo() {
            // remove all recipes
            for(Entry<ItemInfo, ArrayList<SiftingResult>> entry : successful.entrySet()) {
                ArrayList<SiftingResult> list = map.get(entry.getKey());
                for(SiftingResult recipe : entry.getValue()) {
                    removeFromList(list, recipe);
                }
            }
            
            // re-add overwritten recipes
            for(Entry<ItemInfo, ArrayList<SiftingResult>> entry : overwritten.entrySet()) {
                map.get(entry.getKey()).addAll(entry.getValue());
            }
            
            // remove empty entries
            for(Iterator<Entry<ItemInfo, ArrayList<SiftingResult>>> it = map.entrySet().iterator(); it.hasNext(); ) {
                Entry<ItemInfo, ArrayList<SiftingResult>> entry = it.next();
                if(entry.getValue().isEmpty()) {
                    it.remove();
                }
            }
		}

        @Override
        protected String getRecipeInfo(Entry<ItemInfo, ArrayList<SiftingResult>> recipe) {
            return LogHelper.getStackDescription(recipe.getKey().getStack()) + " (" + recipe.getValue().size() + " entries)";
        }
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Deprecated
	@ZenMethod
	public static void removeRecipe(IItemStack input, IItemStack output) {
	    removeRecipe((IIngredient)input, (IIngredient)output);
	}

	@Deprecated
	@ZenMethod 
	public static void removeRewardFromAllBlocks(IItemStack output) 
	{
	    removeRecipe(IngredientAny.INSTANCE, (IIngredient)output);
	}

	@Deprecated
	@ZenMethod 
	public static void removeAllRewardsFromBlock(IItemStack output) 
	{
	    removeRecipe((IIngredient)output, IngredientAny.INSTANCE);
	}
	
	@ZenMethod
	public static void removeRecipe(IIngredient input, @Optional IIngredient output) {
        if(input == null) {
            LogHelper.logError(String.format("Required parameters missing for %s recipe.", name));
            return;
        }
        
        if(output == null) {
            output = IngredientAny.INSTANCE;
        }
        
        Map<ItemInfo, ArrayList<SiftingResult>> recipes = new HashMap<ItemInfo, ArrayList<SiftingResult>>();
        
        for(Entry<ItemInfo, ArrayList<SiftingResult>> entry : SieveRegistry.getSiftables().entrySet()) {
            if(input.matches(toIItemStack(entry.getKey().getStack()))) {
                ArrayList<SiftingResult> list = entry.getValue();
                ArrayList<SiftingResult> toRemove = new ArrayList<SiftingResult>();
                
                for(SiftingResult recipe : list) {
                    ItemStack item = new ItemStack(recipe.item, 1, recipe.meta);
                    
                    if(matches(output, toIItemStack(item))) {
                        toRemove.add(recipe);
                    }
                }
                
                if(!toRemove.isEmpty()) {
                    recipes.put(entry.getKey(), toRemove);
                }
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipes found for %s and %s. Command ignored!", name, input.toString(), output.toString()));
        }
	}



	// Removes a recipe, apply is never the same for anything, so will always
	// need to override it
	private static class Remove extends BaseMapRemoval<ItemInfo, ArrayList<SiftingResult>> {
	    public Remove(Map<ItemInfo, ArrayList<SiftingResult>> recipes) {
	        super(Sieve.name, SieveRegistry.getSiftables(), recipes);
	    }
	    
        @Override
        public void apply() {
            for(Entry<ItemInfo, ArrayList<SiftingResult>> entry : recipes.entrySet()) {
                ArrayList<SiftingResult> list = map.get(entry.getKey());
                
                for(SiftingResult recipe : entry.getValue()) {
                    removeFromList(list, recipe);
                }
                
                successful.put(entry.getKey(), entry.getValue());
            }
            
            // remove empty entries
            for(Iterator<Entry<ItemInfo, ArrayList<SiftingResult>>> it = map.entrySet().iterator(); it.hasNext(); ) {
                Entry<ItemInfo, ArrayList<SiftingResult>> entry = it.next();
                if(entry.getValue().isEmpty()) {
                    it.remove();
                }
            }
        }

        @Override
        public void undo() {
            for(Entry<ItemInfo, ArrayList<SiftingResult>> entry : successful.entrySet()) {
                if(!map.containsKey(entry.getKey())) {
                    map.put(entry.getKey(), new ArrayList<SiftingResult>(entry.getValue()));
                } else {
                    map.get(entry.getKey()).addAll(entry.getValue());
                }
            }
        }
	    
        @Override
        protected String getRecipeInfo(Entry<ItemInfo, ArrayList<SiftingResult>> recipe) {
            return LogHelper.getStackDescription(recipe.getKey().getStack()) + " (" + recipe.getValue().size() + " entries)";
        }
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    public static ArrayList<SiftingResult> removeFromList(ArrayList<SiftingResult> list, SiftingResult s) {
        ArrayList<SiftingResult> removed = new ArrayList<SiftingResult>();
        ItemInfo toRemove = new ItemInfo(s.item, s.meta);
        for(Iterator<SiftingResult> it = list.iterator(); it.hasNext(); ) {
            SiftingResult itS = it.next();
            ItemInfo item = new ItemInfo(itS.item, itS.meta);
            
            if(item.equals(toRemove)) {
                removed.add(itS);
                it.remove();
            }
        }
        
        return removed;
    }
}
