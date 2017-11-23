package com.blamejared.compat.botania.handlers;

import static com.blamejared.mtlib.helpers.InputHelper.toIItemStack;
import static com.blamejared.mtlib.helpers.InputHelper.toObjects;
import static com.blamejared.mtlib.helpers.InputHelper.toStack;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

@ZenClass("mods.botania.Apothecary")
@ModOnly("botania")
@ZenRegister
public class Apothecary {

    protected static final String name = "Botania Petal";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient[] input) {
        CraftTweakerAPI.apply(new Add(new RecipePetals(toStack(output), toObjects(input))));
    }

    @ZenMethod
    public static void addRecipe(String output, IIngredient[] input) {
        addRecipe(toIItemStack(ItemBlockSpecialFlower.ofType(output)), input);
    }

    private static class Add extends BaseListAddition<RecipePetals> {
        public Add(RecipePetals recipe) {
            super("Botania Petal", BotaniaAPI.petalRecipes);
            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(RecipePetals recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        // Get list of existing recipes, matching with parameter
        LinkedList<RecipePetals> result = new LinkedList<>();

        for(RecipePetals entry : BotaniaAPI.petalRecipes) {
            if(entry != null && entry.getOutput() != null && matches(output, toIItemStack(entry.getOutput()))) {
                result.add(entry);
            }
        }

        // Check if we found the recipes and apply the action
        if(!result.isEmpty()) {
            CraftTweakerAPI.apply(new Remove(result));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Apothecary.name, output.toString()));
        }
    }

    @ZenMethod
    public static void removeRecipe(String output) {
        removeRecipe(toIItemStack(ItemBlockSpecialFlower.ofType(output)));
    }

    private static class Remove extends BaseListRemoval<RecipePetals> {
        public Remove(List<RecipePetals> recipes) {
            super(Apothecary.name, BotaniaAPI.petalRecipes, recipes);
        }

        @Override
        public String getRecipeInfo(RecipePetals recipe) {
            return LogHelper.getStackDescription(recipe.getOutput());
        }
    }
}
