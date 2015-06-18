package modtweaker2.mods.appeng.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.InputHelper.toStacks;

import java.util.ArrayList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.utils.ArrayUtils;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import appeng.api.AEApi;
import appeng.api.features.IInscriberRecipe;
import appeng.api.features.InscriberProcessType;
import appeng.core.features.registries.entries.InscriberRecipe;
import appeng.recipes.handlers.Inscribe;

@ZenClass("mods.appeng.Inscriber")
public class Inscriber {
	@ZenMethod
	public static void addRecipe(IItemStack[] imprintable, IItemStack plateA, IItemStack plateB, IItemStack out, String type) {
		MineTweakerAPI.apply(new Add(new InscriberRecipe(ArrayUtils.toArrayList(toStacks(imprintable)), toStack(out), toStack(plateA), toStack(plateB), InscriberProcessType.valueOf(type))));
		for (IItemStack stack : imprintable) {
			AEApi.instance().registries().inscriber().getInputs().add(toStack(stack));
			
		}
		if (plateA != null)
			AEApi.instance().registries().inscriber().getOptionals().add(toStack(plateA));

		if (plateB != null)
			AEApi.instance().registries().inscriber().getOptionals().add(toStack(plateB));
		
	}

	public static class Add extends BaseListAddition {

		public Add(IInscriberRecipe recipe) {
			super(recipe.toString(), AEApi.instance().registries().inscriber().getRecipes(), recipe);
		}

		@Override
		public void apply() {
			AEApi.instance().registries().inscriber().addRecipe((IInscriberRecipe) recipe);
		}

	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(toStack(output)));
	}

	public static class Remove extends BaseListRemoval {
		public Remove(ItemStack stack) {
			super("Applied Energistics 2 Inscriber", AEApi.instance().registries().inscriber().getRecipes(), stack);
		}

		@Override
		public void apply() {
			for (IInscriberRecipe recipe : AEApi.instance().registries().inscriber().getRecipes()) {
				if (recipe != null && recipe.getOutput() != null && recipe.getOutput().isItemEqual(stack)) {
					recipes.add(recipe);
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
