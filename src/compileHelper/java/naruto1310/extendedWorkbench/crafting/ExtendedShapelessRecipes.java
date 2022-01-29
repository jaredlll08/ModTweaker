package naruto1310.extendedWorkbench.crafting;

import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ExtendedShapelessRecipes implements IExtendedRecipe {

	public ExtendedShapelessRecipes(ItemStack output, List<ItemStack> input) {
		throw new UnsupportedOperationException();
	}

	public boolean matches(InventoryCrafting inventory, World world) {
		throw new UnsupportedOperationException();
	}

	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		throw new UnsupportedOperationException();
	}

	public int getRecipeSize() {
		throw new UnsupportedOperationException();
	}

	public ItemStack getRecipeOutput() {
		throw new UnsupportedOperationException();
	}

}
