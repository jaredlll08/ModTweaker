package modtweaker2.mods.mekanism.handlers.v8;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mekanism.api.infuse.InfuseRegistry;
import mekanism.api.infuse.InfuseType;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.inputs.InfusionInput;
import mekanism.common.recipe.machines.MetallurgicInfuserRecipe;
import mekanism.common.recipe.outputs.ItemStackOutput;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.Infuser")
public class Infuser {

	@ZenMethod
	public static void addRecipe(IItemStack output, IItemStack input, int amount, String infuseType) {
		InfuseType type = InfuseRegistry.get(infuseType);
		MineTweakerAPI.apply(new Add(new InfuserRecipe(InputHelper.toStack(output), InputHelper.toStack(input), amount, type)));
	}

	private static class Add extends BaseMapAddition {

		public InfuserRecipe recipe;

		public Add(InfuserRecipe recipe) {
			super("Metallurgic Infuser", Recipe.METALLURGIC_INFUSER.get(), new InfusionInput(recipe.type, recipe.amount, recipe.input), new ItemStackOutput(recipe.output));
			this.recipe = recipe;
		}

		@Override
		public void apply() {
			RecipeHandler.addMetallurgicInfuserRecipe(recipe.type, recipe.amount, recipe.input, recipe.output);

		}
	}

	private static class InfuserRecipe {
		public ItemStack output;
		public ItemStack input;
		public int amount;
		public InfuseType type;

		public InfuserRecipe(ItemStack output, ItemStack input, int amount, InfuseType type) {
			this.output = output;
			this.input = input;
			this.amount = amount;
			this.type = type;
		}

	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(InputHelper.toStack(output)));
	}

	private static class Remove extends BaseMapRemoval {

		public Remove(ItemStack stack) {
			super("Metallurgic Infuser", Recipe.METALLURGIC_INFUSER.get(), stack);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void apply() {
			List<InfusionInput> toRemove = new ArrayList<InfusionInput>();
			for (InfusionInput stack : (Set<InfusionInput>) Recipe.METALLURGIC_INFUSER.get().keySet()) {
				MetallurgicInfuserRecipe recipe = (MetallurgicInfuserRecipe) Recipe.METALLURGIC_INFUSER.get().get(stack);
				if (recipe.getOutput().output.isItemEqual((ItemStack) this.stack)) {
					toRemove.add(stack);
				}
			}
			for (InfusionInput stack : toRemove) {
				Recipe.METALLURGIC_INFUSER.get().remove(stack);
			}
		}

	}
}
