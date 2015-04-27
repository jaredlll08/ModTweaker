package modtweaker2.mods.thaumcraft.handlers;

import static modtweaker2.helpers.InputHelper.toObjects;
import static modtweaker2.helpers.InputHelper.toShapedObjects;
import static modtweaker2.helpers.InputHelper.toStack;

import java.util.ArrayList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import modtweaker2.utils.TweakerPlugin;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;

@ZenClass("mods.thaumcraft.Arcane")
public class Arcane {
	@ZenMethod
	public static void addShaped(String key, IItemStack output, String aspects, IIngredient[][] ingredients) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new Add(new ShapedArcaneRecipe(key, toStack(output), ThaumcraftHelper.parseAspects(aspects), toShapedObjects(ingredients))));
	}

	@ZenMethod
	public static void addShapeless(String key, IItemStack output, String aspects, IIngredient[] ingredients) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new Add(new ShapelessArcaneRecipe(key, toStack(output), ThaumcraftHelper.parseAspects(aspects), toObjects(ingredients))));
	}

	private static class Add extends BaseListAddition {
		public Add(IArcaneRecipe recipe) {
			super("Thaumcraft Arcane Worktable", ThaumcraftApi.getCraftingRecipes(), recipe);
		}

		@Override
		public String getRecipeInfo() {
			Object out = ((IArcaneRecipe) recipe).getRecipeOutput();
			if (out instanceof ItemStack) {
				return ((ItemStack) out).getDisplayName();
			} else
				return super.getRecipeInfo();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new Remove(toStack(output)));
	}

	private static class Remove extends BaseListRemoval {
		public Remove(ItemStack stack) {
			super("Thaumcraft Arcane Worktable", ThaumcraftApi.getCraftingRecipes(), stack);
		}

		@Override
		public void apply() {
			ArrayList<IArcaneRecipe> recipesToRemove = new ArrayList<IArcaneRecipe>();
			for (Object o : ThaumcraftApi.getCraftingRecipes()) {
				if (o != null && o instanceof IArcaneRecipe) {
					IArcaneRecipe r = (IArcaneRecipe) o;
					if (r.getRecipeOutput() != null && r.getRecipeOutput().isItemEqual(stack)) {
						recipesToRemove.add(r);
					}
				}
			}
			for (IArcaneRecipe r : recipesToRemove) {
				ThaumcraftApi.getCraftingRecipes().remove(r);
			}

		}

		@Override
		public String getRecipeInfo() {
			return stack.getDisplayName();
		}
	}
}
