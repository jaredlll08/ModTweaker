/*
Borrowed this BordListian's Alfinivia for now, until hopefully put into CT or MTLib
 */
package com.blamejared.compat.betterwithmods.util;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IngredientCraftTweaker extends Ingredient {

    public static List<Ingredient> toIngredients(IIngredient[] inputs) {
        return Arrays.stream(inputs).map(IngredientCraftTweaker::new).collect(Collectors.toList());
    }

    private IIngredient predicate;

    public IngredientCraftTweaker(IIngredient ingredient) {
        predicate = ingredient;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        List<IItemStack> stacks = predicate != null ? predicate.getItems() : new ArrayList<>();
        return CraftTweakerMC.getItemStacks(stacks.toArray(new IItemStack[stacks.size()]));
    }

    @Override
    public boolean apply(ItemStack stack) {
        if (predicate == null)
            return stack.isEmpty();
        return predicate.matches(CraftTweakerMC.getIItemStack(stack));
    }
}