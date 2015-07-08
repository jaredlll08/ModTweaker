package modtweaker2.mods.tconstruct.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IngredientAny;
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
        if(metal == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        MineTweakerAPI.apply(new Add(new CastingRecipe(toStack(output), toFluid(metal), toStack(cast), consume, delay, null), TConstructHelper.basinCasting));
    }

    @ZenMethod
    public static void addTableRecipe(IItemStack output, ILiquidStack metal, @Optional IItemStack cast, @Optional boolean consume, int delay) {
        if(metal == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        
        MineTweakerAPI.apply(new Add(new CastingRecipe(toStack(output), toFluid(metal), toStack(cast), consume, delay, null), TConstructHelper.tableCasting));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition<CastingRecipe> {
        public Add(CastingRecipe recipe, ArrayList<CastingRecipe> list) {
            super(Casting.name, list);
            
            this.recipes.add(recipe);
        }

        @Override
        protected String getRecipeInfo(CastingRecipe recipe) {
            return InputHelper.getStackDescription(recipe.output);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeTableRecipe(IIngredient output, @Optional IIngredient material, @Optional IIngredient cast) {
        removeRecipe(output, material, cast, TConstructHelper.tableCasting);
    }
    
    @ZenMethod
    public static void removeBasinRecipe(IIngredient output, @Optional IIngredient material, @Optional IIngredient cast) {
        removeRecipe(output, material, cast, TConstructHelper.basinCasting);
    }
   
    public static void removeRecipe(IIngredient output, IIngredient material, IIngredient cast, List<CastingRecipe> list) {

        if(material == null) {
            material = IngredientAny.INSTANCE;
        }
        
        if(cast == null) {
            cast = IngredientAny.INSTANCE;
        }
        
        List<CastingRecipe> recipes = new LinkedList<CastingRecipe>();
        
        for(CastingRecipe recipe : list) {
            if(recipe != null) {
                if (!matches(output, toIItemStack(recipe.output))) {
                    continue;
                }
                
                if (!matches(material, toILiquidStack(recipe.castingMetal))) {
                    continue;
                }
                
                if(!matches(cast, toIItemStack(recipe.cast))) {
                    continue;
                }
                
                recipes.add(recipe);
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(list, recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for output %s, meterial %s and cast %s. Command ignored!", Casting.name, output.toString(), material.toString(), cast.toString()));
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
}
