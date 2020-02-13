package com.blamejared.compat.tcomplement.highoven.recipes;

import javax.annotation.Nonnull;

import knightminer.tcomplement.library.steelworks.HeatRecipe;
import net.minecraftforge.fluids.FluidStack;

public class HeatRecipeTweaker extends HeatRecipe {

	public HeatRecipeTweaker(@Nonnull FluidStack input, @Nonnull FluidStack output, int temp) {
		super(input, output, temp);
	}

}
