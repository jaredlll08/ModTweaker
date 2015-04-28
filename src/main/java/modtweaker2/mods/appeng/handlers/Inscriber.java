package modtweaker2.mods.appeng.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.InputHelper.toStacks;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.utils.ArrayUtils;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import appeng.api.AEApi;
import appeng.api.features.IInscriberRecipe;
import appeng.api.features.InscriberProcessType;
import appeng.core.features.registries.entries.InscriberRecipe;

@ZenClass("mods.appeng.Inscriber")
public class Inscriber {
	@ZenMethod
	public static void addRecipe(IItemStack[] imprintable, IItemStack plateA, IItemStack plateB, IItemStack out, String type) {
		MineTweakerAPI.apply(new Add(new InscriberRecipe(ArrayUtils.toArrayList(toStacks(imprintable)), toStack(out), toStack(plateA), toStack(plateB), InscriberProcessType.valueOf(type))));
		
	}

	public static class Add implements IUndoableAction {

		IInscriberRecipe recipe;
		
		public Add(IInscriberRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		public void apply() {
			AEApi.instance().registries().inscriber().addRecipe(recipe);
		}

		@Override
		public boolean canUndo() {
			return recipe != null;
		}

		@Override
		public String describe() {
			return "Added Applied Energistics Inscriber recipe to get " + recipe.getOutput().toString();
		}

		@Override
		public String describeUndo() {
			return "Undoing Applied Energistics Inscriber recipe to get " + recipe.getOutput().toString();
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}

		@Override
		public void undo() {
			AEApi.instance().registries().inscriber().removeRecipe(recipe);
		}

	}

	@ZenMethod
	public static void removeRecipe(IItemStack[] imprintable, IItemStack plateA, IItemStack plateB, IItemStack out, String type) {
		MineTweakerAPI.apply(new Remove(new InscriberRecipe(ArrayUtils.toArrayList(toStacks(imprintable)), toStack(out), toStack(plateA), toStack(plateB), InscriberProcessType.valueOf(type))));
	}

	public static class Remove implements IUndoableAction {

		IInscriberRecipe recipe;
		
		public Remove(IInscriberRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		public void apply() {
			AEApi.instance().registries().inscriber().removeRecipe(recipe);
		}

		@Override
		public boolean canUndo() {
			return recipe != null;
		}

		@Override
		public String describe() {
			return "Remove Applied Energistics Inscriber recipe to get " + recipe.getOutput().toString();
		}

		@Override
		public String describeUndo() {
			return "Undid the removing of Applied Energistics Inscriber recipe to get " + recipe.getOutput().toString();
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}

		@Override
		public void undo() {
			AEApi.instance().registries().inscriber().addRecipe(recipe);
		}

	}

}
