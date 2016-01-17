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
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import exnihilo.utils.ItemInfo;

@ZenClass("mods.exnihilo.Hammer")
public class Hammer {
    
    public static final String name = "ExNihilo Hammer";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, double chance, double luck) {
        if(input == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s recipe.", name));
            return;
        }
        
        addRecipe(input, new IItemStack[] { output }, new double[] { chance }, new double[] { luck });
	}
	
	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack[] output, double[] chance, double[] luck) {
	    if(input == null || output == null || chance == null || luck == null) {
            LogHelper.logError(String.format("Required parameters missing for %s recipe.", name));
            return;
	    }
	    
        if(!isABlock(input)) {
            LogHelper.logError(String.format("Input item for %s recipe must be a block.", name));
            return;
        }
	    
	    if(output.length == 0 || output.length != chance.length || output.length != luck.length) {
	        LogHelper.logError(String.format("Number of items in arrays are different or null (%d, %d, %d)", output.length, chance.length, luck.length));
	        return;
	    }
	    
	    Map<ItemInfo, ArrayList<Smashable>> recipes = new HashMap<ItemInfo, ArrayList<Smashable>>();
	    ArrayList<Smashable> list = new ArrayList<Smashable>();
	    ItemInfo itemInfo = new ItemInfo(toStack(input));
	    recipes.put(itemInfo, list);
	    
	    for(int i = 0; i < output.length; i++) {
	        ItemStack out = toStack(output[i]);
	        list.add(new Smashable(Block.getBlockFromItem(itemInfo.getItem()), itemInfo.getMeta(), out.getItem(), out.getItemDamage(), (float)chance[i], (float)luck[i]));
	    }
	    
	    MineTweakerAPI.apply(new Add(recipes));
	}

	private static class Add extends BaseMapAddition<ItemInfo, ArrayList<Smashable>> {
		public Add(Map<ItemInfo, ArrayList<Smashable>> recipes) {
		    super(Hammer.name, HammerRegistry.getRewards(), recipes);
		}

		public void apply() {
		    for(Entry<ItemInfo, ArrayList<Smashable>> entry : recipes.entrySet()) {
		        if(!map.containsKey(entry.getKey())) {
		            // no recipe for input available, add new entry
		            map.put(entry.getKey(), new ArrayList<Smashable>(entry.getValue()));
		        } else {
		            // recipes already available, add to present list
		            ArrayList<Smashable> list = map.get(entry.getKey());
		            
		            // store old Smashable objects if present
		            ArrayList<Smashable> backup = new ArrayList<Smashable>();
		            for(Smashable recipe : entry.getValue()) {
		                backup.addAll(removeFromList(list, recipe));
		            }
		            
		            if(!backup.isEmpty()) {
		                LogHelper.logWarning(String.format("Overwritten %d %s recipe outputs for %s.", backup.size(), Hammer.name, LogHelper.getStackDescription(entry.getKey().getStack())));
		                overwritten.put(entry.getKey(), backup);
		            }
		            
		            // add new Smashable objects
		            list.addAll(entry.getValue());
		        }
		        
		        successful.put(entry.getKey(), entry.getValue());
		    }
		}

		public void undo() {
		    // remove all recipes
		    for(Entry<ItemInfo, ArrayList<Smashable>> entry : successful.entrySet()) {
		        ArrayList<Smashable> list = map.get(entry.getKey());
		        for(Smashable recipe : entry.getValue()) {
		            removeFromList(list, recipe);
		        }
		    }
		    
		    // re-add overwritten recipes
		    for(Entry<ItemInfo, ArrayList<Smashable>> entry : overwritten.entrySet()) {
		        map.get(entry.getKey()).addAll(entry.getValue());
		    }
		    
		    // remove empty entries
		    for(Iterator<Entry<ItemInfo, ArrayList<Smashable>>> it = map.entrySet().iterator(); it.hasNext(); ) {
		        Entry<ItemInfo, ArrayList<Smashable>> entry = it.next();
		        if(entry.getValue().isEmpty()) {
		            it.remove();
		        }
		    }
		}
		
		@Override
		protected String getRecipeInfo(Entry<ItemInfo, ArrayList<Smashable>> recipe) {
		    return LogHelper.getStackDescription(recipe.getKey().getStack()) + " (" + recipe.getValue().size() + " entries)";
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Deprecated
	@ZenMethod
	public static void removeRecipe(IItemStack inputBlock, int inputBlockMeta, IItemStack outputItem, int outputItemMeta) {
	    removeRecipe(toIItemStack(new ItemStack(toStack(inputBlock).getItem(), 1, inputBlockMeta)), toIItemStack(new ItemStack(toStack(outputItem).getItem(), 1, outputItemMeta)));
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
        
        Map<ItemInfo, ArrayList<Smashable>> recipes = new HashMap<ItemInfo, ArrayList<Smashable>>();
        
        for(Entry<ItemInfo, ArrayList<Smashable>> entry : HammerRegistry.getRewards().entrySet()) {
            if(input.matches(toIItemStack(entry.getKey().getStack()))) {
                ArrayList<Smashable> list = entry.getValue();
                ArrayList<Smashable> toRemove = new ArrayList<Smashable>();
                
                for(Smashable recipe : list) {
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
	private static class Remove extends BaseMapRemoval<ItemInfo, ArrayList<Smashable>> {

		public Remove(Map<ItemInfo, ArrayList<Smashable>> recipes) {
		    super(Hammer.name, HammerRegistry.getRewards(), recipes);
		}

		@Override
		public void apply() {
		    for(Entry<ItemInfo, ArrayList<Smashable>> entry : recipes.entrySet()) {
		        ArrayList<Smashable> list = map.get(entry.getKey());
		        
		        for(Smashable recipe : entry.getValue()) {
		            removeFromList(list, recipe);
		        }
		        
		        successful.put(entry.getKey(), entry.getValue());
		    }
		    
            // remove empty entries
            for(Iterator<Entry<ItemInfo, ArrayList<Smashable>>> it = map.entrySet().iterator(); it.hasNext(); ) {
                Entry<ItemInfo, ArrayList<Smashable>> entry = it.next();
                if(entry.getValue().isEmpty()) {
                    it.remove();
                }
            }
		}

		@Override
		public void undo() {
		    for(Entry<ItemInfo, ArrayList<Smashable>> entry : successful.entrySet()) {
                if(!map.containsKey(entry.getKey())) {
                    map.put(entry.getKey(), new ArrayList<Smashable>(entry.getValue()));
                } else {
                    map.get(entry.getKey()).addAll(entry.getValue());
                }
		    }
		}
		
        @Override
        protected String getRecipeInfo(Entry<ItemInfo, ArrayList<Smashable>> recipe) {
            return LogHelper.getStackDescription(recipe.getKey().getStack()) + " (" + recipe.getValue().size() + " entries)";
        }
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    public static ArrayList<Smashable> removeFromList(ArrayList<Smashable> list, Smashable s) {
        ArrayList<Smashable> removed = new ArrayList<Smashable>();
        ItemInfo toRemove = new ItemInfo(s.item, s.meta);
        for(Iterator<Smashable> it = list.iterator(); it.hasNext(); ) {
            Smashable itS = it.next();
            ItemInfo item = new ItemInfo(itS.item, itS.meta);
            
            if(item.equals(toRemove)) {
                removed.add(itS);
                it.remove();
            }
        }
        
        return removed;
    }
}
