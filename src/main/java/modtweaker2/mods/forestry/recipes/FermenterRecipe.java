package modtweaker2.mods.forestry.recipes;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import forestry.api.recipes.IFermenterRecipe;

public class FermenterRecipe implements IFermenterRecipe {

	private final ItemStack resource;
	private final int fermentationValue;
	private final float modifier;
	private final Fluid output;
	private final FluidStack fluidResource;

	public FermenterRecipe(ItemStack resource, int fermentationValue, float modifier, Fluid output, FluidStack fluidResource) {
		if (resource == null) {
			throw new NullPointerException("Fermenter Resource cannot be null!");
		}

		if (output == null) {
			throw new NullPointerException("Fermenter Output cannot be null!");
		}

		if (fluidResource == null) {
			throw new NullPointerException("Fermenter Liquid cannot be null!");
		}

		// assume that fermenter recipes want to use Forestry's honey and not the legacy "fluid.honey"
		if (fluidResource.getFluid().getName().equals("fluid.honey")) {
			fluidResource = new FluidStack(FluidRegistry.getFluid("fluid.for.honey"), fluidResource.amount);
		}

		this.resource = resource;
		this.fermentationValue = fermentationValue;
		this.modifier = modifier;
		this.output = output;
		this.fluidResource = fluidResource;
	}

	@Override
	public ItemStack getResource() {
		return resource;
	}

	@Override
	public FluidStack getFluidResource() {
		return fluidResource;
	}

	@Override
	public int getFermentationValue() {
		return fermentationValue;
	}

	@Override
	public float getModifier() {
		return modifier;
	}

	@Override
	public Fluid getOutput() {
		return output;
	}
}
