package modtweaker2.mods.auracascade.handlers;

import static modtweaker2.helpers.InputHelper.toStack;

import java.util.ArrayList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.auracascade.AuraCascadeHelper;
import modtweaker2.mods.auracascade.aura.IAuraStack;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import pixlepix.auracascade.data.recipe.PylonRecipe;
import pixlepix.auracascade.data.recipe.PylonRecipeComponent;
import pixlepix.auracascade.data.recipe.PylonRecipeRegistry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.auraCascade.Pylon")
public class Pylon {

	@ZenMethod
	public static void addRecipe(IItemStack ouput, IAuraStack stack, IItemStack input) {
		addRecipe(ouput, stack, input, stack, input, stack, input, stack, input);
	}

	@ZenMethod
	public static void addRecipe(IItemStack ouput, IAuraStack aura1, IItemStack input1, IAuraStack aura2, IItemStack input2, IAuraStack aura3, IItemStack input3, IAuraStack aura4, IItemStack input4) {
		MineTweakerAPI.apply(new Add(new PylonRecipe(toStack(ouput), new PylonRecipeComponent(AuraCascadeHelper.toAura(aura1), toStack(input1)), new PylonRecipeComponent(AuraCascadeHelper.toAura(aura2), toStack(input2)), new PylonRecipeComponent(AuraCascadeHelper.toAura(aura3), toStack(input3)), new PylonRecipeComponent(AuraCascadeHelper.toAura(aura1), toStack(input4)))));
	}

	private static class Add extends BaseListAddition {

		public Add(PylonRecipe recipe) {
			super(PylonRecipeRegistry.recipes, recipe);
		}

	}

	@ZenMethod
	public static void removeRecipe(IItemStack ouput) {
		MineTweakerAPI.apply(new Remove(toStack(ouput)));
	}

	private static class Remove extends BaseListRemoval {
		public Remove(ItemStack stack) {
			super(PylonRecipeRegistry.recipes, stack);
		}

		@Override
		public void apply() {
			for (PylonRecipe r : PylonRecipeRegistry.recipes) {
				if (r.result.isItemEqual(stack)) {
					recipes.add(r);
				}
			}

			super.apply();
		}

	}
}
