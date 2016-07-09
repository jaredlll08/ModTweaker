package modtweaker2.mods.forestry.recipes;

import forestry.api.recipes.IDescriptiveRecipe;
import forestry.api.recipes.IFabricatorRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class FabricatorRecipe implements IFabricatorRecipe {

    private final ItemStack plan;
    private final FluidStack molten;
    private final IDescriptiveRecipe internal;

    public FabricatorRecipe(ItemStack plan, FluidStack molten, ItemStack result, ItemStack[] remainingItems, Object[] ingredients) {
        this(plan, molten, new DescriptiveRecipe(3, 3, ingredients, result, remainingItems));
    }

    public FabricatorRecipe(ItemStack plan, FluidStack molten, IDescriptiveRecipe internal) {
        this.plan = plan;
        this.molten = molten;
        this.internal = internal;
    }


    @Override
    public Object[] getIngredients() {
        return internal.getIngredients();
    }

    @Override
    public int getWidth() {
        return internal.getWidth();
    }

    @Override
    public int getHeight() {
        return internal.getHeight();
    }

    @Override
    @Nullable
    public ItemStack getPlan() {
        return plan;
    }

    @Override
    public FluidStack getLiquid() {
        return molten;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return internal.getRecipeOutput();
    }

}
