package modtweaker2.mods.appeng.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.InputHelper.toStacks;

import java.util.ArrayList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.util.BaseListAddition;
import modtweaker2.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import appeng.recipes.handlers.Inscribe;
import appeng.recipes.handlers.Inscribe.InscriberRecipe;

@ZenClass("mods.appeng.Inscriber")
public class Inscriber {
	@ZenMethod
	public static void addRecipe(IItemStack[] imprintable, IItemStack plateA, IItemStack plateB, IItemStack out, boolean usePlates) {
		MineTweakerAPI.apply(new Add(new InscriberRecipe(toStacks(imprintable), toStack(plateA), toStack(plateB), toStack(out), usePlates)));
		for (IItemStack stack : imprintable) {
			Inscribe.INPUTS.add(toStack(stack));
		}
		Inscribe.PLATES.add(toStack(plateA));
		Inscribe.PLATES.add(toStack(plateB));
	}

	public static class Add extends BaseListAddition {

		public Add(InscriberRecipe recipe) {
			super(recipe.toString(), Inscribe.RECIPES, recipe);
		}

		@Override
		public void apply() {
			Inscribe.RECIPES.add((InscriberRecipe) recipe);
		}

	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(toStack(output)));
	}

	public static class Remove extends BaseListRemoval {

		public Remove(ItemStack stack) {

			super(stack.getUnlocalizedName(), Inscribe.RECIPES, stack);
		}

		@Override
		public void apply() {
			ArrayList<InscriberRecipe> recipesToRemove = new ArrayList<Inscribe.InscriberRecipe>();
			for (InscriberRecipe recipe : Inscribe.RECIPES) {
				if (recipe != null && recipe.output != null && recipe.output.isItemEqual(stack)) {
					recipesToRemove.add(recipe);
				}
			}
			for (InscriberRecipe recipe : recipesToRemove) {
				Inscribe.RECIPES.remove(recipe);
			}
		}

	}

}
