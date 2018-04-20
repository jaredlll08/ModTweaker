package com.blamejared.compat.betterwithmods;


import betterwithmods.common.registry.anvil.AnvilCraftingManager;
import betterwithmods.common.registry.anvil.ShapedAnvilRecipe;
import betterwithmods.common.registry.anvil.ShapelessAnvilRecipe;
import betterwithmods.module.gameplay.AnvilRecipes;
import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.Iterator;

@ZenClass("mods.betterwithmods.Anvil")
@ModOnly("betterwithmods")
@ZenRegister
public class Anvil {
    
    @ZenMethod
    public static void addShaped(IItemStack output, IIngredient[][] inputs) {
        ModTweaker.LATE_ADDITIONS.add(new AddShaped(output, inputs));
    }
    
    @ZenMethod
    public static void addShapeless(IItemStack output, IIngredient[] inputs) {
        ModTweaker.LATE_ADDITIONS.add(new AddShapeless(output, inputs));
    }
    
    @ZenMethod
    public static void removeShaped(IItemStack output, @Optional IIngredient[][] ingredients) {
        ModTweaker.LATE_REMOVALS.add(new RemoveShaped(output, ingredients));
    }
    
    @ZenMethod
    public static void removeShapeless(IItemStack output, @Optional IIngredient[] ingredients) {
        ModTweaker.LATE_REMOVALS.add(new RemoveShapeless(output, ingredients));
    }

    @ZenMethod
    public static void removeAll() {
        ModTweaker.LATE_REMOVALS.add(new RemoveAll());
    }

    public static class AddShaped extends BaseAction {
        
        private final IItemStack output;
        private final IIngredient[][] ingredients;
        
        public AddShaped(IItemStack output, IIngredient[][] ingredients) {
            super("Add Anvil Shaped Recipe");
            this.output = output;
            this.ingredients = ingredients;
        }
        
        @Override
        public void apply() {
            AnvilRecipes.addSteelShapedRecipe(new ResourceLocation("crafttweaker", this.name), InputHelper.toStack(output), toShapedAnvilObjects(ingredients));
        }
        
        @Override
        protected String getRecipeInfo() {
            return output.getDisplayName();
        }
    }
    
    public static class AddShapeless extends BaseAction {
        
        private final IItemStack output;
        private final IIngredient[] ingredients;
        
        public AddShapeless(IItemStack output, IIngredient[] ingredients) {
            super("Add Anvil Shapeless Recipe");
            this.output = output;
            this.ingredients = ingredients;
        }
        
        @Override
        public void apply() {
            AnvilRecipes.addSteelShapelessRecipe(new ResourceLocation("crafttweaker", this.name), InputHelper.toStack(output), InputHelper.toObjects(ingredients));
        }
        
        @Override
        protected String getRecipeInfo() {
            return output.getDisplayName();
        }
    }
    
    public static Object[] toShapedAnvilObjects(IIngredient[][] ingredients) {
        if(ingredients == null)
            return null;
        else {
            ArrayList prep = new ArrayList();
            char chr = 'a';
            for(int y = 0; y < 4; y++) {
                StringBuilder matrix = new StringBuilder();
                for(int x = 0; x < 4; x++) {
                    if(x < ingredients.length && ingredients[x] != null && y < ingredients[x].length) {
                        if(ingredients[x][y] != null) {
                            prep.add(chr);
                            prep.add(InputHelper.toObject(ingredients[x][y]));
                            matrix.append(chr);
                            chr++;
                        } else {
                            matrix.append(' ');
                        }
                    }
                }
                if(matrix.length() > 0)
                    prep.add(y, matrix.toString());
            }
            return prep.toArray();
        }
    }
    
    public static class RemoveShaped extends BaseAction {
        
        private final IItemStack output;
        private final IIngredient[][] ingredients;
        
        protected RemoveShaped(IItemStack output, IIngredient[][] ingredients) {
            super("Remove Shaped Anvil");
            this.output = output;
            this.ingredients = ingredients;
        }
        
        @Override
        public void apply() {
            if(ingredients != null) {
                IRecipe removal = new ShapedAnvilRecipe(new ResourceLocation("crafttweaker", this.name), InputHelper.toStack(output), toShapedAnvilObjects(ingredients));
                for(Iterator<IRecipe> iterator = AnvilCraftingManager.ANVIL_CRAFTING.iterator(); iterator.hasNext(); ) {
                    IRecipe recipe = iterator.next();
                    if(recipe.getRecipeOutput().isItemEqual(removal.getRecipeOutput()) && removal.getIngredients().equals(recipe.getIngredients()))
                        iterator.remove();
                }
            } else {
                for(Iterator<IRecipe> iterator = AnvilCraftingManager.ANVIL_CRAFTING.iterator(); iterator.hasNext(); ) {
                    IRecipe recipe = iterator.next();
                    if(recipe.getRecipeOutput().isItemEqual(InputHelper.toStack(output))) {
                        iterator.remove();
                    }
                }
            }
        }
        
        
        @Override
        protected String getRecipeInfo() {
            return output.getDisplayName();
        }
    }
    
    public static class RemoveShapeless extends BaseAction {
        
        private final IItemStack output;
        private final IIngredient[] ingredients;
        
        protected RemoveShapeless(IItemStack output, IIngredient[] ingredients) {
            super("Remove Shapeless Anvil");
            this.output = output;
            this.ingredients = ingredients;
        }
        
        @Override
        public void apply() {
            if(ingredients != null) {
                IRecipe removal = new ShapelessAnvilRecipe(new ResourceLocation("crafttweaker", this.name), InputHelper.toStack(output), InputHelper.toObjects(ingredients));
                for(Iterator<IRecipe> iterator = AnvilCraftingManager.ANVIL_CRAFTING.iterator(); iterator.hasNext(); ) {
                    IRecipe recipe = iterator.next();
                    if(recipe.getRecipeOutput().isItemEqual(removal.getRecipeOutput()) && removal.getIngredients().equals(recipe.getIngredients()))
                        iterator.remove();
                }
            } else {
                for(Iterator<IRecipe> iterator = AnvilCraftingManager.ANVIL_CRAFTING.iterator(); iterator.hasNext(); ) {
                    IRecipe recipe = iterator.next();
                    if(recipe.getRecipeOutput().isItemEqual(InputHelper.toStack(output))) {
                        iterator.remove();
                    }
                }
            }
        }
        
        
        @Override
        protected String getRecipeInfo() {
            return output.getDisplayName();
        }
    }

    public static class RemoveAll extends BaseAction {

        protected RemoveAll() {
            super("Remove All Anvil");
        }

        @Override
        public void apply() {
            AnvilCraftingManager.ANVIL_CRAFTING.clear();
        }

        @Override
        protected String getRecipeInfo() {
            return "all";
        }
    }
}
