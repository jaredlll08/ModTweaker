package modtweaker2.mods.forestry.recipes;

import net.minecraftforge.fluids.FluidStack;

import forestry.api.recipes.IStillRecipe;

public class StillRecipe implements IStillRecipe {
	private final int timePerUnit;
	private final FluidStack input;
	private final FluidStack output;

	public StillRecipe(int timePerUnit, FluidStack input, FluidStack output) {
		this.timePerUnit = timePerUnit;
		if (input == null) {
			throw new IllegalArgumentException("Still recipes need an input. Input was null.");
		}
		if (output == null) {
			throw new IllegalArgumentException("Still recipes need an output. Output was null.");
		}
		this.input = input;
		this.output = output;
	}

	@Override
	public int getCyclesPerUnit() {
		return timePerUnit;
	}

	@Override
	public FluidStack getInput() {
		return input;
	}

	@Override
	public FluidStack getOutput() {
		return output;
	}
}
