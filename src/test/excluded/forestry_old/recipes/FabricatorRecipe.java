package modtweaker.mods.forestry.recipes;

import forestry.api.recipes.*;
import forestry.core.recipes.ShapedRecipeCustom;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class FabricatorRecipe implements IFabricatorRecipe {
    
    private final ItemStack plan;
    private final FluidStack molten;
    private final IDescriptiveRecipe internal;
    
    public FabricatorRecipe(ItemStack plan, FluidStack molten, ItemStack result, Object[] ingredients) {
        this(plan, molten, new ShapedRecipeCustom(result, ingredients));
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
