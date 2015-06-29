package modtweaker2.mods.tconstruct.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.tconstruct.TConstructHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.crafting.CastingRecipe;

@ZenClass("mods.tconstruct.Casting")
public class Casting {
    
    protected static final String name = "TConstruct Casting";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @ZenMethod
    public static void addBasinRecipe(IItemStack output, ILiquidStack metal, @Optional IItemStack cast, @Optional boolean consume, int delay) {
        MineTweakerAPI.apply(new Add(new CastingRecipe(toStack(output), toFluid(metal), toStack(cast), consume, delay, null), TConstructHelper.basinCasting));
    }

    @ZenMethod
    public static void addTableRecipe(IItemStack output, ILiquidStack metal, @Optional IItemStack cast, @Optional boolean consume, int delay) {
        MineTweakerAPI.apply(new Add(new CastingRecipe(toStack(output), toFluid(metal), toStack(cast), consume, delay, null), TConstructHelper.tableCasting));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition<CastingRecipe> {
        public Add(CastingRecipe recipe, ArrayList<CastingRecipe> list) {
            super("TConstruct Casting", list);
            
            this.recipes.add(recipe);
        }

        @Override
        protected String getRecipeInfo(CastingRecipe recipe) {
            return InputHelper.getStackDescription(recipe.output);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeTableRecipe(IIngredient output) {
        removeRecipe(output, TConstructHelper.tableCasting, RecipeComponent.Output);
    }
    
    @ZenMethod
    public static void removeTableMaterial(IIngredient material) {
        removeRecipe(material, TConstructHelper.tableCasting, RecipeComponent.Material);
    }

    @ZenMethod
    public static void removeCastRecipes(IIngredient cast) {
        removeRecipe(cast, TConstructHelper.tableCasting, RecipeComponent.Cast);
    }

    @ZenMethod
    public static void removeBasinRecipe(IIngredient output) {
        removeRecipe(output, TConstructHelper.basinCasting, RecipeComponent.Output);
    }
    
    @ZenMethod
    public static void removeBasinMaterial(IIngredient material) {
        removeRecipe(material, TConstructHelper.basinCasting, RecipeComponent.Material);
    }

    
    public static void removeRecipe(IIngredient ingredient, List<CastingRecipe> list, RecipeComponent component) {
        
        List<CastingRecipe> recipes = new LinkedList<CastingRecipe>();
        
        for(CastingRecipe recipe : list) {
            if(recipe != null) {
                switch(component) {
                    case Cast:
                        if(recipe.cast != null && ingredient.contains(toIItemStack(recipe.cast))) {
                            recipes.add(recipe);
                        }
                        break;
                    case Output:
                        if (recipe.output != null && ingredient.contains(toIItemStack(recipe.output))) {
                            recipes.add(recipe);
                        }
                        break;
                    case Material:
                        if (recipe.castingMetal != null && ingredient.contains(toILiquidStack(recipe.castingMetal))) {
                            // TODO: Currently broken, because MCLiquidStack equals() method always returns false
                            recipes.add(recipe);
                        }
                        break;
                }
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(list, recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Casting.name, ingredient.toString()));
        }
    }

    // Removes all matching recipes, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval<CastingRecipe> {
        public Remove(List<CastingRecipe> list, List<CastingRecipe> recipes) {
            super(Casting.name, list, recipes);
        }      

        @Override
        protected String getRecipeInfo(CastingRecipe recipe) {
            return InputHelper.getStackDescription(recipe.output);
        }
    }

    public enum RecipeComponent {
        Output,
        Cast,
        Material
    }
}
