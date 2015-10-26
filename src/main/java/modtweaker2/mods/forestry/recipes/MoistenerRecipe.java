package modtweaker2.mods.forestry.recipes;

import net.minecraft.item.ItemStack;

import forestry.api.recipes.IMoistenerRecipe;

public class MoistenerRecipe implements IMoistenerRecipe {

	private final int timePerItem;
	private final ItemStack resource;
	private final ItemStack product;

	public MoistenerRecipe(ItemStack resource, ItemStack product, int timePerItem) {
		this.timePerItem = timePerItem;
		this.resource = resource;
		this.product = product;
	}

	public int getTimePerItem() {
		return timePerItem;
	}

	public ItemStack getResource() {
		return resource;
	}

	public ItemStack getProduct() {
		return product;
	}
}
