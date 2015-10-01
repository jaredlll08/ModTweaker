package modtweaker2.mods.extraUtils.handlers;

import java.util.LinkedList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.rwtema.extrautils.tileentity.enderconstructor.EnderConstructorRecipesHandler;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.StackHelper.matches;

@ZenClass("mods.extraUtils.QED")
public class QED {

	public static final String name = "ExtraUtilities Q.E.D.";

	@ZenMethod
	public static void addShapedRecipe(IItemStack output, IIngredient[][] recipe) {
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
	public static void removeRecipe(IIngredient output) {
		LinkedList<IRecipe> recipes = new LinkedList<IRecipe>();

		for (IRecipe recipe : EnderConstructorRecipesHandler.recipes) {
			if (recipe != null && recipe.getRecipeOutput() != null && matches(output, toIItemStack(recipe.getRecipeOutput()))) {
				recipes.add(recipe);
			}
		}

		if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", QED.name, LogHelper.getStackDescription(output)));
		}
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
