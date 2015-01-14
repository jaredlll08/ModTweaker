package modtweaker.mods.forestry.handlers;

import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.InputHelper;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.FlowerManager;
import forestry.apiculture.genetics.BeeMutation;
import forestry.factory.gadgets.MachineFermenter.Recipe;
import forestry.factory.gadgets.MachineFermenter.RecipeManager;

@ZenClass("mods.forestry.Bees")
public class Bees {

	@ZenMethod
	public static void addFlower(IItemStack stack) {
		MineTweakerAPI.apply(new Add(InputHelper.toStack(stack)));
	}

	private static class Add extends BaseListAddition {
		public Add(ItemStack recipe) {
			super("Forestry Bees Flowers", FlowerManager.plainFlowers, recipe);
		}

		@Override
		public String getRecipeInfo() {
			return ((Recipe) recipe).output.getLocalizedName();
		}
	}

	@ZenMethod
	public static void removeFlower(IItemStack stack) {
		MineTweakerAPI.apply(new Remove(FlowerManager.plainFlowers, InputHelper.toStack(stack)));
	}

	private static class Remove extends BaseListRemoval {

		public Remove(List list, ItemStack stack) {
			super(list, stack);

		}

		@Override
		public void apply() {
			for (ItemStack r : FlowerManager.plainFlowers) {
				if (r != null && r.isItemEqual(stack)) {
					recipe = r;
					break;
				}
			}
			FlowerManager.plainFlowers.remove(recipe);

		}

	}

	@ZenMethod
	public static void clearFlowerList() {
		MineTweakerAPI.apply(new Clear());
	}

	private static class Clear extends BaseListRemoval {

		public Clear() {
			super(FlowerManager.plainFlowers, new ItemStack(Blocks.air));
		}

		@Override
		public void apply() {
			FlowerManager.plainFlowers.clear();
		}

	}
}
