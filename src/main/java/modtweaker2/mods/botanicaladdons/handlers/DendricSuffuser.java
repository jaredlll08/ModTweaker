package modtweaker2.mods.botanicaladdons.handlers;


import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import ninja.shadowfox.shadowfox_botany.api.ShadowFoxAPI;
import ninja.shadowfox.shadowfox_botany.api.recipe.RecipeTreeCrafting;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.LinkedList;
import java.util.List;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toObjects;
import static modtweaker2.helpers.StackHelper.matches;

@ZenClass("mods.botanicaladdons.DendricSuffuser")
public class DendricSuffuser {

    protected static final String name = "Botanical Addons Dendric Suffuser";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void addRecipe(IItemStack blockOutput, int mana, int throttle, IIngredient[] input) {
        Object output = InputHelper.toObject(blockOutput);
        if(output == null || !(output instanceof ItemStack) || !InputHelper.isABlock((ItemStack)output)) {
            LogHelper.logError("Output must be a block.");
            return;
        }
        MineTweakerAPI.apply(new Add(new RecipeTreeCrafting(mana, Block.getBlockFromItem(((ItemStack)output).getItem()),
                ((ItemStack)output).getItemDamage(), throttle, toObjects(input))));
    }

    @ZenMethod
    public static void addRecipe(IItemStack blockOutput, int mana, IIngredient[] input) {
        addRecipe(blockOutput, mana, -1, input);
    }

    private static class Add extends BaseListAddition<RecipeTreeCrafting> {
        public Add(RecipeTreeCrafting recipe) {
            super(DendricSuffuser.name, ShadowFoxAPI.treeRecipes);

            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(RecipeTreeCrafting recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        // Get list of existing recipes, matching with parameter
        List<RecipeTreeCrafting> recipes = new LinkedList<RecipeTreeCrafting>();

        for (RecipeTreeCrafting r : ShadowFoxAPI.treeRecipes) {
            if (r != null && r.getOutput() != null && matches(output, toIItemStack(r.getOutput()))) {
                recipes.add(r);
            }
        }

        // Check if we found the recipes and apply the action
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", DendricSuffuser.name, output.toString()));
        }
    }

    private static class Remove extends BaseListRemoval<RecipeTreeCrafting> {
        public Remove(List<RecipeTreeCrafting> recipes) {
            super(DendricSuffuser.name, ShadowFoxAPI.treeRecipes, recipes);
        }

        @Override
        public String getRecipeInfo(RecipeTreeCrafting recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }
}

