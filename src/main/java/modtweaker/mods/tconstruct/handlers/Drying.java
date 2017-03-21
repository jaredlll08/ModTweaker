package modtweaker.mods.tconstruct.handlers;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import com.blamejared.mtlib.helpers.LogHelper;
import modtweaker.mods.tconstruct.TConstructHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.DryingRecipe;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.plugin.jei.DryingRecipeWrapper;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.LinkedList;
import java.util.List;

import static com.blamejared.mtlib.helpers.InputHelper.toIItemStack;
import static com.blamejared.mtlib.helpers.InputHelper.toStack;

@ZenClass("mods.tconstruct.Drying")
public class Drying {

    protected static final String name = "TConstruct Drying Rack";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, int time) {
        if (input == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        RecipeMatch match = new RecipeMatch.Item(toStack(input), toStack(input).stackSize);
        MineTweakerAPI.apply(new Add(TConstructHelper.getDryingRecipe(toStack(output), match, time)));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition<DryingRecipe> {
        public Add(DryingRecipe recipe) {
            super(Drying.name, TConstructHelper.dryingList);
            this.recipes.add(recipe);
        }
    
        @Override
        public void apply() {
            if(recipes.isEmpty()) {
                return;
            }
        
            for(DryingRecipe recipe : recipes) {
                if(recipe != null) {
                    if(list.add(recipe)) {
                        successful.add(recipe);
                        MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(recipe);
                    } else {
                        LogHelper.logError(String.format("Error adding %s Recipe for %s", name, getRecipeInfo(recipe)));
                    }
                } else {
                    LogHelper.logError(String.format("Error adding %s Recipe: null object", name));
                }
            }
        }
    
        @Override
        public void undo() {
            if(this.successful.isEmpty()) {
                return;
            }
        
            for(DryingRecipe recipe : successful) {
                if(recipe != null) {
                    if(!list.remove(recipe)) {
                        LogHelper.logError(String.format("Error removing %s Recipe for %s", name, this.getRecipeInfo(recipe)));
                    }else{
                        MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(recipe);
                    }
                } else {
                    LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
                }
            }
        }
    
        @Override
        protected String getRecipeInfo(DryingRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a TConstruct Drying Rack recipe
    @ZenMethod
    public static void removeRecipe(IIngredient ingredient) {
        List<DryingRecipe> recipes = new LinkedList<DryingRecipe>();

        for (DryingRecipe recipe : TinkerRegistry.getAllDryingRecipes()) {
            if (recipe != null && recipe.getResult() != null && ingredient.matches(toIItemStack(recipe.getResult()))) {
                recipes.add(recipe);
            }
        }

        if (!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Drying.name, ingredient.toString()));
        }
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval<DryingRecipe> {
        public Remove(List<DryingRecipe> list) {
            super(Drying.name, TConstructHelper.dryingList, list);
        }
    
        @Override
        public void apply() {
            if (recipes.isEmpty()) {
                return;
            }
            for (DryingRecipe recipe : this.recipes) {
                if (recipe != null) {
                    if (this.list.remove(recipe)) {
                    
                        successful.add(recipe);
                        MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(recipe);
                    } else {
                        LogHelper.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                    }
                } else {
                    LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
                }
            }
        }
    
        @Override
        public void undo() {
            if (successful.isEmpty()) {
                return;
            }
            for (DryingRecipe recipe : successful) {
                if (recipe != null) {
                    if (!list.add(recipe)) {
                        LogHelper.logError(String.format("Error restoring %s Recipe for %s", name, getRecipeInfo(recipe)));
                    }else{
                        MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(recipe);
                    }
                } else {
                    LogHelper.logError(String.format("Error restoring %s Recipe: null object", name));
                }
            }
        }
    
        @Override
        protected String getRecipeInfo(DryingRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }
    }
}
