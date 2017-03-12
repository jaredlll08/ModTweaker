package modtweaker.mods.embers.handlers;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;
import teamroots.embers.compat.jei.MeltingRecipeWrapper;
import teamroots.embers.recipe.*;

import java.util.*;
import java.util.Map.Entry;

import static com.blamejared.mtlib.helpers.InputHelper.*;

@ZenClass("mods.embers.Melter")
public class Melter {
    
    public static String name = "Embers Melter";
    
    @ZenMethod
    public static void addRecipe(IItemStack input, ILiquidStack outputOne, boolean matchMeta, boolean matchNBT) {
        MineTweakerAPI.apply(new Add(toStack(input), new ItemMeltingRecipe(toStack(input), toFluid(outputOne), matchMeta, matchNBT)));
    }
    
    @ZenMethod
    public static void addOreRecipe(IOreDictEntry key, ILiquidStack outputOne, boolean matchMeta, boolean matchNBT) {
        MineTweakerAPI.apply(new AddOre(key.getName(), new ItemMeltingOreRecipe(key.getName(), toFluid(outputOne))));
    }
    
    @ZenMethod
    public static void remove(ILiquidStack fluid) {
        List<ItemStack> recipes = new ArrayList<>();
        List<String> oreRecipes = new ArrayList<>();
        if(fluid == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        for(int i = 0; i < RecipeRegistry.meltingRecipes.size(); i++) {
            if(((ItemMeltingRecipe) RecipeRegistry.meltingRecipes.values().toArray()[i]).getFluid().getFluid().getName().equals(toFluid(fluid).getFluid().getName())) {
                recipes.add(((ItemStack) RecipeRegistry.meltingRecipes.keySet().toArray()[i]));
            }
        }
        
        for(int i = 0; i < RecipeRegistry.meltingOreRecipes.size(); i++) {
            if(((ItemMeltingOreRecipe) RecipeRegistry.meltingOreRecipes.values().toArray()[i]).getFluid().getFluid().getName().equals(toFluid(fluid).getFluid().getName())) {
                oreRecipes.add(((String) RecipeRegistry.meltingOreRecipes.keySet().toArray()[i]));
            }
        }
        
        if(!recipes.isEmpty()) {
            Map<ItemStack, ItemMeltingRecipe> map = new HashMap<ItemStack, ItemMeltingRecipe>();
            for(int i = 0; i < recipes.size(); i++) {
                map.put(recipes.get(i), RecipeRegistry.meltingRecipes.get(recipes.get(i)));
            }
            MineTweakerAPI.apply(new Remove(map));
        } else if(!oreRecipes.isEmpty()) {
            Map<String, ItemMeltingOreRecipe> map = new HashMap<String, ItemMeltingOreRecipe>();
            for(int i = 0; i < oreRecipes.size(); i++) {
                map.put(oreRecipes.get(i), RecipeRegistry.meltingOreRecipes.get(oreRecipes.get(i)));
            }
            MineTweakerAPI.apply(new RemoveOre(map));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", Melter.name, fluid.toString()));
        }
        
    }
    
    private static class AddOre extends BaseMapAddition<String, ItemMeltingOreRecipe> {
        
        public AddOre(String stack, ItemMeltingOreRecipe recipe) {
            super(Melter.name, RecipeRegistry.meltingOreRecipes);
            
            this.recipes.put(stack, recipe);
        }
        
        @Override
        public void apply() {
            super.apply();
            successful.forEach((stack, recipe) -> {
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new MeltingRecipeWrapper(recipe));
            });
        }
        
        @Override
        public void undo() {
            super.undo();
            successful.forEach((stack, recipe) -> {
                MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new MeltingRecipeWrapper(recipe));
            });
        }
        
        @Override
        protected String getRecipeInfo(Entry<String, ItemMeltingOreRecipe> arg0) {
            return arg0.getValue().getOreName();
        }
    }
    
    private static class Add extends BaseMapAddition<ItemStack, ItemMeltingRecipe> {
        
        public Add(ItemStack stack, ItemMeltingRecipe recipe) {
            super(Melter.name, RecipeRegistry.meltingRecipes);
            this.recipes.put(stack, recipe);
        }
        
        @Override
        public void apply() {
            super.apply();
            successful.forEach((stack, recipe) -> {
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new MeltingRecipeWrapper(recipe));
            });
        }
        
        @Override
        public void undo() {
            super.undo();
            successful.forEach((stack, recipe) -> {
                MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new MeltingRecipeWrapper(recipe));
            });
        }
        
        @Override
        protected String getRecipeInfo(Entry<ItemStack, ItemMeltingRecipe> arg0) {
            return LogHelper.getStackDescription(arg0.getValue().getStack());
        }
    }
    
    private static class Remove extends BaseMapRemoval<ItemStack, ItemMeltingRecipe> {
        
        public Remove(Map<ItemStack, ItemMeltingRecipe> recipes) {
            super(Melter.name, RecipeRegistry.meltingRecipes, recipes);
        }
        @Override
        public void apply() {
            super.apply();
            successful.forEach((stack, recipe) -> {
                MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new MeltingRecipeWrapper(recipe));
            });
        }
    
        @Override
        public void undo() {
            super.undo();
            successful.forEach((stack, recipe) -> {
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new MeltingRecipeWrapper(recipe));
            });
        }
        @Override
        protected String getRecipeInfo(Entry<ItemStack, ItemMeltingRecipe> arg0) {
            return LogHelper.getStackDescription(arg0.getValue().getStack());
        }
    }
    
    private static class RemoveOre extends BaseMapRemoval<String, ItemMeltingOreRecipe> {
        
        public RemoveOre(Map<String, ItemMeltingOreRecipe> recipes) {
            super(Melter.name, RecipeRegistry.meltingOreRecipes, recipes);
        }
        @Override
        public void apply() {
            super.apply();
            successful.forEach((stack, recipe) -> {
                MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new MeltingRecipeWrapper(recipe));
            });
        }
    
        @Override
        public void undo() {
            super.undo();
            successful.forEach((stack, recipe) -> {
                MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new MeltingRecipeWrapper(recipe));
            });
        }
        @Override
        protected String getRecipeInfo(Entry<String, ItemMeltingOreRecipe> arg0) {
            return arg0.getValue().getOreName();
        }
    }
}
