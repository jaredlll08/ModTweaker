package com.blamejared.compat.tcomplement.highoven;

import java.util.LinkedList;
import java.util.List;

import com.blamejared.ModTweaker;
import com.blamejared.compat.mantle.RecipeMatchIIngredient;
import com.blamejared.compat.tcomplement.highoven.recipes.HeatRecipeTweaker;
import com.blamejared.compat.tcomplement.highoven.recipes.HighOvenFuelTweaker;
import com.blamejared.compat.tcomplement.highoven.recipes.MixRecipeTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.mc1120.item.MCItemStack;
import knightminer.tcomplement.library.TCompRegistry;
import knightminer.tcomplement.library.events.TCompRegisterEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.util.Pair;

@ZenClass("mods.tcomplement.highoven.HighOven")
@ZenRegister
@ModOnly("tcomplement")
public class HighOven {

	public static final List<IIngredient> REMOVED_FUELS = new LinkedList<>();
	public static final List<Pair<FluidStack, FluidStack>> REMOVED_HEAT_RECIPES = new LinkedList<>();
	public static final List<Pair<FluidStack, FluidStack>> REMOVED_MIX_RECIPES = new LinkedList<>();

	private static boolean init = false;

	private static void init() {
		if (!init) {
			MinecraftForge.EVENT_BUS.register(new HighOven());
			init = true;
		}
	}

	@ZenMethod
	public static void removeFuel(IIngredient stack) {
		init();
		CraftTweakerAPI.apply(new HighOven.RemoveFuel(stack));
	}

	@ZenMethod
	public static void addFuel(IIngredient fuel, int burnTime, int tempRate) {
		init();
		ModTweaker.LATE_ADDITIONS.add(new HighOven.AddFuel(fuel, burnTime, tempRate));
	}

	private static class AddFuel extends BaseAction {
		private IIngredient fuel;
		private int time;
		private int rate;

		public AddFuel(IIngredient fuel, int time, int rate) {
			super("HighOven.Fuel");
			this.fuel = fuel;
			this.time = time;
			this.rate = rate;
		}

		@Override
		public void apply() {
			TCompRegistry.registerFuel(new HighOvenFuelTweaker(new RecipeMatchIIngredient(fuel), time, rate));
		}

		@Override
		public String describe() {
			return String.format("Adding %s as high oven fuel", this.getRecipeInfo());
		}

		@Override
		public String getRecipeInfo() {
			return LogHelper.getStackDescription(fuel);
		}
	}

	private static class RemoveFuel extends BaseAction {
		private IIngredient fuel;

		public RemoveFuel(IIngredient fuel) {
			super("HighOven.Fuel");
			this.fuel = fuel;
		};

		@Override
		public void apply() {
			REMOVED_FUELS.add(fuel);
		}

		@Override
		public String describe() {
			return String.format("Removing %s high oven fuel entry", this.getRecipeInfo());
		}

		@Override
		public String getRecipeInfo() {
			return LogHelper.getStackDescription(fuel);
		}

	}

	@ZenMethod
	public static void removeHeatRecipe(ILiquidStack output, @Optional ILiquidStack input) {
		init();
		CraftTweakerAPI.apply(new RemoveHeat(input, output));
	}

	@ZenMethod
	public static void addHeatRecipe(ILiquidStack output, ILiquidStack input, int temp) {
		init();
		ModTweaker.LATE_ADDITIONS
				.add(new HighOven.AddHeat(InputHelper.toFluid(output), InputHelper.toFluid(input), temp));
	}

	private static class AddHeat extends BaseAction {
		private FluidStack output, input;
		private int temp;

		public AddHeat(FluidStack output, FluidStack input, int temp) {
			super("HighOven.HeatRecipe");
			this.output = output;
			this.input = input;
			this.temp = temp;
		}

		@Override
		public void apply() {
			TCompRegistry.registerHeatRecipe(new HeatRecipeTweaker(input, output, temp));
		}

		@Override
		public String describe() {
			return String.format("Adding %s Recipe(s) for %s", this.name, this.getRecipeInfo());
		}

		@Override
		protected String getRecipeInfo() {
			return LogHelper.getStackDescription(output);
		}
	}

	private static class RemoveHeat extends BaseAction {
		private ILiquidStack input;
		private ILiquidStack output;

		public RemoveHeat(ILiquidStack input, ILiquidStack output) {
			super("HighOven.HeatRecipe");
			this.input = input;
			this.output = output;
		}

		@Override
		public void apply() {
			REMOVED_HEAT_RECIPES.add(new Pair<>(InputHelper.toFluid(input), InputHelper.toFluid(output)));
		}

		@Override
		public String describe() {
			return String.format("Removing %s Recipe(s) for %s", this.name, this.getRecipeInfo());
		}

		@Override
		public String getRecipeInfo() {
			return LogHelper.getStackDescription(output)
					+ ((input == null) ? "" : (" from " + LogHelper.getStackDescription(input)));
		}

	}

	@ZenMethod
	public static void removeMixRecipe(ILiquidStack output, @Optional ILiquidStack input) {
		init();
		CraftTweakerAPI.apply(new RemoveMix(output, input));
	}

	@ZenMethod
	public static MixRecipeBuilder newMixRecipe(ILiquidStack output, ILiquidStack input, int temp) {
		init();
		return new MixRecipeBuilder(output, input, temp);
	}

	@ZenMethod
	public static MixRecipeManager manageMixRecipe(ILiquidStack output, ILiquidStack input) {
		init();
		return new MixRecipeManager(output, input);
	}

	private static class RemoveMix extends BaseAction {
		private ILiquidStack output;
		private ILiquidStack input;

		public RemoveMix(ILiquidStack output, ILiquidStack input) {
			super("HighOven.MixRecipe");
			this.output = output;
			this.input = input;
		}

		@Override
		public void apply() {
			REMOVED_MIX_RECIPES.add(new Pair<>(InputHelper.toFluid(input), InputHelper.toFluid(output)));
		}

		@Override
		public String describe() {
			return String.format("Removing %s Recipe(s) for %s", this.name, this.getRecipeInfo());
		}

		@Override
		protected String getRecipeInfo() {
			return LogHelper.getStackDescription(output)
					+ ((input == null) ? "" : (" from " + LogHelper.getStackDescription(input)));
		}
	}

	@SubscribeEvent
	public void onHighOvenFuelRegister(TCompRegisterEvent.HighOvenFuelRegisterEvent event) {
		if (event.getRecipe() instanceof HighOvenFuelTweaker) {
			return;
		}
		for (IIngredient entry : REMOVED_FUELS) {
			for (ItemStack fuel : event.getRecipe().getFuels()) {
				if (entry.matches(new MCItemStack(fuel))) {
					event.setCanceled(true);
					return;
				}
			}
		}
	}

	@SubscribeEvent
	public void onHighOvenHeatRegister(TCompRegisterEvent.HighOvenHeatRegisterEvent event) {
		if (event.getRecipe() instanceof HeatRecipeTweaker) {
			return;
		} else {
			for (Pair<FluidStack, FluidStack> entry : REMOVED_HEAT_RECIPES) {
				if (event.getRecipe().matches(entry.getKey(), entry.getValue())) {
					event.setCanceled(true);
					return;
				}
			}
		}
	}

	@SubscribeEvent
	public void onHighOvenMixRegister(TCompRegisterEvent.HighOvenMixRegisterEvent event) {
		if (event.getRecipe() instanceof MixRecipeTweaker) {
			return;
		} else {
			for (Pair<FluidStack, FluidStack> entry : REMOVED_MIX_RECIPES) {
				if (event.getRecipe().matches(entry.getKey(), entry.getValue())) {
					event.setCanceled(true);
					return;
				}
			}
		}
	}
}
