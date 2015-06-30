package modtweaker2.mods.railcraft.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toStack;

import java.lang.reflect.Field;
import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import mods.railcraft.api.crafting.ICokeOvenRecipe;
import mods.railcraft.common.util.crafting.CokeOvenCraftingManager;
import mods.railcraft.common.util.crafting.CokeOvenCraftingManager.CokeOvenRecipe;
import modtweaker2.mods.railcraft.RailcraftHelper;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.railcraft.CokeOven")
public class CokeOven {
	@ZenMethod
	public static void addRecipe(IItemStack input, boolean matchDamage, boolean matchNBT, IItemStack output, ILiquidStack fluidOutput, int cookTime) {
		MineTweakerAPI.apply(new Add(toStack(input), matchDamage, matchNBT, toStack(output), toFluid(fluidOutput), cookTime));
	}

	private static class Add implements IUndoableAction {

		private ItemStack stack;
		private boolean matchDamage;
		private boolean matchNBT;
		private ItemStack stack2;
		private FluidStack fluid;
		private int cookTime;

		public Add(ItemStack stack, boolean matchDamage, boolean matchNBT, ItemStack stack2, FluidStack fluid, int cookTime) {
			this.stack = stack;
			this.matchDamage = matchDamage;
			this.matchNBT = matchNBT;
			this.stack2 = stack2;
			this.fluid = fluid;
			this.cookTime = cookTime;
		}

		@Override
		public void apply() {
			CokeOvenCraftingManager.getInstance().addRecipe(stack, matchDamage, matchNBT, stack2, fluid, cookTime);

		}

		@Override
		public boolean canUndo() {
			return false;
		}

		@Override
		public String describe() {
			return "Adding a Railcraft CokeOven recipie for " + stack.getUnlocalizedName() + " and " + stack2.getUnlocalizedName() + "to get " + fluid.getUnlocalizedName();
		}

		@Override
		public String describeUndo() {
			return null;
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}

		@Override
		public void undo() {

		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(toStack(output)));
	}

	private static class Remove extends BaseListRemoval {
		public Remove(ItemStack stack) {
			super("Coke Oven", RailcraftHelper.oven, stack);
		}

		@Override
		public void apply() {
			for (ICokeOvenRecipe r : RailcraftHelper.oven) {
				if (r.getOutput() != null && r.getOutput().isItemEqual(stack) && ItemStack.areItemStackTagsEqual(r.getOutput(), stack)) {
					recipes.add(r);
				}
			}
			super.apply();
		}

		@Override
		public String getRecipeInfo() {
			return stack.getDisplayName();
		}
	}
}
