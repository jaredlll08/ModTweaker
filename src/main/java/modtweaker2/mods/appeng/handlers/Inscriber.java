package modtweaker2.mods.appeng.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.InputHelper.toStacks;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.utils.ArrayUtils;
import modtweaker2.utils.BaseUndoable;
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

	public static class Add extends BaseUndoable {

		IInscriberRecipe recipe;
		
		public Add(IInscriberRecipe recipe) {
			super("Applied Energistics Inscriber", true);
			this.recipe = recipe;
		}
		
		@Override
		public String getRecipeInfo(){
			return recipe.getOutput().toString();
		}

		@Override
		public void apply() {
			AEApi.instance().registries().inscriber().addRecipe(recipe);
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

	public static class Remove extends BaseUndoable {

		IInscriberRecipe recipe;
		
		public Remove(IInscriberRecipe recipe) {
			super("Applied Energistics Inscriber", false);
			this.recipe = recipe;
		}

		@Override
		public String getRecipeInfo(){
			return recipe.getOutput().toString();
		}

		
		public void apply() {
			AEApi.instance().registries().inscriber().removeRecipe(recipe);
		}

		@Override
		public void undo() {
			AEApi.instance().registries().inscriber().addRecipe(recipe);
		}
	}
}