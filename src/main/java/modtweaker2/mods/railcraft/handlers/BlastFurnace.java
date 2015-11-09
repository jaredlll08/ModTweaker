package modtweaker2.mods.railcraft.handlers;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import mods.railcraft.api.crafting.IBlastFurnaceRecipe;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.StackHelper;
import modtweaker2.mods.railcraft.RailcraftHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.LinkedList;
import java.util.List;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

@ZenClass("mods.railcraft.BlastFurnace")
public class BlastFurnace {

	public static final String name = "Railcraft Blast Furnace";
	public static final String nameFuel = "Railcraft Blast Furnace (Fuel)";

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Add a recipe for the Blast Furnace
	 *
	 * @param output        ItemStack result
	 * @param input         ItemStack input
	 * @param cookTime      time per item to process
	 * @param matchEmptyNBT Set this to true, if you want to match only input items strictly without NBT Data)
	 */
	@ZenMethod
	public static void addRecipe(IItemStack output, IItemStack input, int cookTime, @Optional Boolean matchEmptyNBT) {
		if (matchEmptyNBT == null)
			if (toStack(input).stackTagCompound == null)
				matchEmptyNBT = false;
			else
				matchEmptyNBT = true;

		if (input.getDamage() == OreDictionary.WILDCARD_VALUE)
			MineTweakerAPI.apply(new Add(RailcraftHelper.getBlastFurnaceRecipe(toStack(input), false, matchEmptyNBT, cookTime, toStack(output))));
		else
			MineTweakerAPI.apply(new Add(RailcraftHelper.getBlastFurnaceRecipe(toStack(input), true, matchEmptyNBT, cookTime, toStack(output))));
	}

	@Deprecated
	@ZenMethod
	public static void addRecipe(IItemStack input, boolean matchDamage, boolean matchNBT, int cookTime, IItemStack output) {
		if (input == null || output == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}

		MineTweakerAPI.apply(new Add(RailcraftHelper.getBlastFurnaceRecipe(toStack(input), matchDamage, matchNBT, cookTime, toStack(output))));
	}

	private static class Add extends BaseListAddition<IBlastFurnaceRecipe> {
		@SuppressWarnings("unchecked")
		public Add(IBlastFurnaceRecipe recipe) {
			super(BlastFurnace.name, (List<IBlastFurnaceRecipe>) RailcraftHelper.furnace);
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(IBlastFurnaceRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient output) {
		if (output == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}

		List<IBlastFurnaceRecipe> recipes = new LinkedList<IBlastFurnaceRecipe>();

		for (IBlastFurnaceRecipe r : RailcraftHelper.furnace) {
			if (r != null && r.getOutput() != null && matches(output, toIItemStack(r.getOutput()))) {
				recipes.add(r);
			}
		}

		if (!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", BlastFurnace.name, output.toString()));
		}
	}

	private static class Remove extends BaseListRemoval<IBlastFurnaceRecipe> {
		@SuppressWarnings("unchecked")
		public Remove(List<IBlastFurnaceRecipe> recipes) {
			super(BlastFurnace.name, (List<IBlastFurnaceRecipe>) RailcraftHelper.furnace, recipes);
		}

		@Override
		public String getRecipeInfo(IBlastFurnaceRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@ZenMethod
	public static void addFuel(IIngredient itemInput) {
		if (itemInput == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}

		List<ItemStack> fuels = new LinkedList<ItemStack>();

		for (IItemStack item : itemInput.getItems()) {

			boolean match = false;

			for (ItemStack fuel : RailcraftHelper.fuels) {
				if (StackHelper.matches(item, InputHelper.toIItemStack(fuel))) {
					match = true;
					break;
				}
			}

			if (!match && TileEntityFurnace.isItemFuel(InputHelper.toStack(item))) {
				fuels.add(InputHelper.toStack(item));
			}
		}

		if (!fuels.isEmpty()) {
			MineTweakerAPI.apply(new AddFuels(fuels));
		} else {
			LogHelper.logWarning(String.format("No %s added.", nameFuel));
		}
	}
	
	private static class AddFuels extends BaseListAddition<ItemStack> {
		public AddFuels(List<ItemStack> fuels) {
			super(BlastFurnace.nameFuel, RailcraftHelper.fuels, fuels);
		}

		@Override
		protected String getRecipeInfo(ItemStack recipe) {
			return LogHelper.getStackDescription(recipe);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@ZenMethod
	public static void removeFuel(IIngredient itemInput) {
		List<ItemStack> fuels = new LinkedList<ItemStack>();

		for (ItemStack fuel : RailcraftHelper.fuels) {
			if (StackHelper.matches(itemInput, InputHelper.toIItemStack(fuel))) {
				fuels.add(fuel);
			}
		}

		if (!fuels.isEmpty()) {
			MineTweakerAPI.apply(new RemoveFuels(fuels));
		} else {
			LogHelper.logWarning(String.format("No %s found for argument %s.", nameFuel, itemInput.toString()));
		}
	}
	
	private static class RemoveFuels extends BaseListRemoval<ItemStack> {
		public RemoveFuels(List<ItemStack> fuels) {
			super(BlastFurnace.nameFuel, RailcraftHelper.fuels, fuels);
		}

		@Override
		protected String getRecipeInfo(ItemStack recipe) {
			return LogHelper.getStackDescription(recipe);
		}
	}
}
