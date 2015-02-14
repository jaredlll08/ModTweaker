package modtweaker.mods.appeng.handlers;

import static modtweaker.helpers.InputHelper.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseListAddition;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenMethodStatic;
import appeng.recipes.handlers.Inscribe;
import appeng.recipes.handlers.Inscribe.InscriberRecipe;

@ZenClass("mods.appeng.inscriber")
public class Inscriber {

	@ZenMethod
	public static void addRecipe(IItemStack[] imprintable, IItemStack plateA, IItemStack plateB, IItemStack out, boolean usePlates) {
		MineTweakerAPI.apply(new Add(new InscriberRecipe(toStacks(imprintable), toStack(plateA), toStack(plateB), toStack(out), usePlates)));
		Inscribe.inputs.add(toStack(imprintable[0]));
		Inscribe.inputs.add(toStack(imprintable[1]));
		Inscribe.inputs.add(toStack(plateA));
		Inscribe.inputs.add(toStack(plateB));
		Inscribe.plates.add(toStack(plateA));
		Inscribe.plates.add(toStack(plateB));
		
	}

	public static class Add extends BaseListAddition {

		public Add(InscriberRecipe recipe) {
			super(recipe.toString(), Inscribe.recipes, recipe);
		}

		@Override
		public void apply() {
			Inscribe.recipes.add((InscriberRecipe) recipe);
		}

	}

}
