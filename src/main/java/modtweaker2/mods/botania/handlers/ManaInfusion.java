package modtweaker2.mods.botania.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toObject;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;


@ZenClass("mods.botania.ManaInfusion")
public class ManaInfusion {
    
    protected static final String name = "Botania Mana Infusion";
    
    @ZenMethod
    public static void addInfusion(IItemStack output, IIngredient input, int mana) {
        MineTweakerAPI.apply(new Add(new RecipeManaInfusion(toStack(output), toObject(input), mana)));
    }

    @ZenMethod
    public static void addAlchemy(IItemStack output, IIngredient input, int mana) {
        RecipeManaInfusion recipe = new RecipeManaInfusion(toStack(output), toObject(input), mana);
        recipe.setAlchemy(true);
        MineTweakerAPI.apply(new Add(recipe));
    }

    @ZenMethod
    public static void addConjuration(IItemStack output, IIngredient input, int mana) {
        RecipeManaInfusion recipe = new RecipeManaInfusion(toStack(output), toObject(input), mana);
        recipe.setConjuration(true);
        MineTweakerAPI.apply(new Add(recipe));
    }

    private static class Add extends BaseListAddition<RecipeManaInfusion> {
        public Add(RecipeManaInfusion recipe) {
            super(ManaInfusion.name, BotaniaAPI.manaInfusionRecipes);
            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(RecipeManaInfusion recipe) {
            return InputHelper.getStackDescription(recipe.getOutput());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        // Get list of existing recipes, matching with parameter
        List<RecipeManaInfusion> recipes = new LinkedList<RecipeManaInfusion>();
        
        for (RecipeManaInfusion r : BotaniaAPI.manaInfusionRecipes) {
            if (r.getOutput() != null && matches(output, toIItemStack(r.getOutput()))) {
                recipes.add(r);
            }
        }
        
        // Check if we found the recipes and apply the action
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", ManaInfusion.name, output.toString()));
        }
    }

    private static class Remove extends BaseListRemoval<RecipeManaInfusion> {
        public Remove(List<RecipeManaInfusion> recipes) {
            super(ManaInfusion.name, BotaniaAPI.manaInfusionRecipes, recipes);
        }

        @Override
        public String getRecipeInfo(RecipeManaInfusion recipe) {
            return InputHelper.getStackDescription(recipe.getOutput());
        }
    }
}
