package com.blamejared.compat.mantle;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import slimeknights.mantle.util.RecipeMatch;

public class RecipeMatchIIngredient extends RecipeMatch {
	private IIngredient ingredient;
	private List<ItemStack> items;
	public RecipeMatchIIngredient(IIngredient ingredient) {
		this(ingredient, 1);
	}

	public RecipeMatchIIngredient(IIngredient ingredient, int amountMatched) {
		super(amountMatched, ingredient.getAmount());
		this.ingredient = ingredient;
	}

	@Override
	public List<ItemStack> getInputs() {
		if(items != null) {
			return items;
		}
		return items = Arrays.stream(ingredient.getItemArray()).map(CraftTweakerMC::getItemStack).collect(Collectors.toList());
	}

	@Override
	public Optional<Match> matches(NonNullList<ItemStack> stacks) {
		List<ItemStack> found = Lists.newLinkedList();
		int stillNeeded = amountNeeded;

		for(ItemStack stack : stacks) {
			if(ingredient.matches(CraftTweakerMC.getIItemStack(stack))) {
				// add the amount found to the list
				ItemStack copy = stack.copy();
				copy.setCount(Math.min(copy.getCount(), stillNeeded));
				found.add(copy);
				stillNeeded -= copy.getCount();

				// we found enough
				if(stillNeeded <= 0) {
					return Optional.of(new Match(found, amountMatched));
				}
			}
		}
		return Optional.empty();
	}

}

