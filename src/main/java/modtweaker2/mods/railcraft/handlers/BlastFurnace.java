package modtweaker2.mods.railcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;

import java.util.ArrayList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import mods.railcraft.api.crafting.IBlastFurnaceRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import modtweaker2.mods.railcraft.RailcraftHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.railcraft.BlastFurnace")
public class BlastFurnace {
	@ZenMethod
	public static void addRecipe(IItemStack input, boolean matchDamage, boolean matchNBT, int cookTime, IItemStack output) {
		MineTweakerAPI.apply(new Add(RailcraftHelper.getBlastFurnaceRecipe(toStack(input), matchDamage, matchNBT, cookTime, toStack(output))));
	}

	private static class Add extends BaseListAddition {
		public Add(IBlastFurnaceRecipe recipe) {
			super("Blast Furnace", RailcraftHelper.furnace, recipe);
		}

		@Override
		public String getRecipeInfo() {
			return ((IBlastFurnaceRecipe) recipe).getOutput().getDisplayName();
		}
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(toStack(output)));
	}

	private static class Remove extends BaseListRemoval {
		public Remove(ItemStack stack) {
			super("Blast Furnace", RailcraftHelper.furnace, stack);
		}

		@Override
		public void apply() {
			ArrayList<IBlastFurnaceRecipe> recipesToRemove = new ArrayList<IBlastFurnaceRecipe>();
			for (IBlastFurnaceRecipe r : RailcraftHelper.furnace) {
					if (r.getOutput() != null && stack.isItemEqual(r.getOutput())) {
						recipesToRemove.add(r);
					}
			}
			for (IBlastFurnaceRecipe r : recipesToRemove)
				RailcraftCraftingManager.blastFurnace.getRecipes().remove(r);
		}

		@Override
		public String getRecipeInfo() {
			return stack.getDisplayName();
		}
	}
}
