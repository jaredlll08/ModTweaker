package modtweaker2.mods.appeng.handlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import appeng.api.AEApi;
import appeng.api.features.IGrinderEntry;
import appeng.core.features.registries.entries.AppEngGrinderRecipe;

@ZenClass("mods.appeng.Grinder")
public class Grind {

	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, int energy) {
		MineTweakerAPI.apply(new Add(new AppEngGrinderRecipe(InputHelper.toStack(input), InputHelper.toStack(output), energy)));
	}

	private static class Add extends BaseListAddition {

		public Add(AppEngGrinderRecipe recipe) {
			super(AEApi.instance().registries().grinder().getRecipes(), recipe);
		}

		@Override
		public void apply() {
			AEApi.instance().registries().grinder().getRecipes().add((AppEngGrinderRecipe) recipe);
		}
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(InputHelper.toStack(output)));
	}

	private static class Remove extends BaseListRemoval {

		public Remove(ItemStack stack) {
			super(AEApi.instance().registries().grinder().getRecipes(), stack);
		}

		@Override
		public void apply() {
			for (IGrinderEntry r : AEApi.instance().registries().grinder().getRecipes()) {
				if (r.getOutput().isItemEqual(stack)) {
					recipes.add(r);
				}
			}
			super.apply();
		}

	}

}