package modtweaker2.mods.extraUtils.handlers;

import java.util.LinkedList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.rwtema.extrautils.tileentity.enderconstructor.EnderConstructorRecipesHandler;

@ZenClass("mods.extraUtils.QED")
public class QED {

	@ZenMethod
	public static void addShapedRecipe(IItemStack output, IItemStack[][] recipe) {
		MineTweakerAPI.apply(new Add(new ShapedOreRecipe(InputHelper.toStack(output), InputHelper.toShapedObjects(recipe))));
	}
	
	private static class Add extends BaseListAddition<IRecipe> {

		public Add(IRecipe recipe) {
			super("QED", EnderConstructorRecipesHandler.recipes);
			recipes.add(recipe);
		}

		@Override
		protected String getRecipeInfo(IRecipe recipe) {
			return recipe.toString();
		}

	}

	@ZenMethod
	public static void removeRecipe(IItemStack toRemove) {
		if (toRemove == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", "QED"));
			return;
		}
		LinkedList<IRecipe> recipes = new LinkedList<IRecipe>();

		for (IRecipe recipe : EnderConstructorRecipesHandler.recipes) {
			if (recipe != null && recipe.getRecipeOutput() != null && recipe.getRecipeOutput().isItemEqual(InputHelper.toStack(toRemove))) {
				recipes.add(recipe);
			}
		}

		MineTweakerAPI.apply(new Remove(recipes));

	}

	public static class Remove extends BaseListRemoval<IRecipe> {

		protected Remove(LinkedList<IRecipe> stacks) {
			super("QED", EnderConstructorRecipesHandler.recipes, stacks);

		}

		@Override
		protected String getRecipeInfo(IRecipe recipe) {
			return recipe.toString();
		}

	}

}
