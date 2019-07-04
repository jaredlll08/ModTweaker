package com.blamejared.compat.tcomplement.highoven;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import knightminer.tcomplement.library.TCompRegistry;
import knightminer.tcomplement.library.events.TCompRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.tcomplement.highoven.HighOven")
@ZenRegister
@ModOnly("tcomplement")
public class HighOven {

	public static final List<IItemStack> REMOVED_FUELS = new LinkedList<>();
	public static final Map<FluidStack, Set<FluidStack>> REMOVED_HEAT_RECIPES = new LinkedHashMap<>();
	public static final Map<FluidStack, Set<FluidStack>> REMOVED_MIX_RECIPES = new LinkedHashMap<>();

	private static boolean init = false;

	private static void init() {
		if (!init) {
			MinecraftForge.EVENT_BUS.register(new HighOven());
			init = true;
		}
	}

	@ZenMethod
	public static void removeFuel(IItemStack stack) {
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
		private IItemStack fuel;

		public RemoveFuel(IItemStack fuel) {
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
			FluidStack output = InputHelper.toFluid(this.output);
			if (this.input == null) {
				REMOVED_HEAT_RECIPES.put(output, null);
			} else if (REMOVED_HEAT_RECIPES.containsKey(output)) {
				Set<FluidStack> inputs = REMOVED_HEAT_RECIPES.get(output);
				if (inputs != null)
					inputs.add(InputHelper.toFluid(input));
			} else {
				Set<FluidStack> inputs = new HashSet<>();
				inputs.add(InputHelper.toFluid(input));
				REMOVED_HEAT_RECIPES.put(output, inputs);
			}
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
	public static MixRecipeHelper newMixRecipe(ILiquidStack output, ILiquidStack input, int temp) {
		init();
		return new MixRecipeHelper(output, input, temp);
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
			FluidStack output = InputHelper.toFluid(this.output);
			if (this.input == null) {
				REMOVED_MIX_RECIPES.put(output, null);
			} else if (REMOVED_MIX_RECIPES.containsKey(output)) {
				Set<FluidStack> inputs = REMOVED_MIX_RECIPES.get(output);
				if (inputs != null)
					inputs.add(InputHelper.toFluid(input));
			} else {
				Set<FluidStack> inputs = new HashSet<>();
				inputs.add(InputHelper.toFluid(input));
				REMOVED_MIX_RECIPES.put(output, inputs);
			}
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
	public void onTinkerRegister(TCompRegisterEvent.HighOvenFuelRegisterEvent event) {
		if (event.getRecipe() instanceof HighOvenFuelTweaker) {
			return;
		}
		for (IItemStack entry : REMOVED_FUELS) {
			if (event.getRecipe().matches(InputHelper.toStack(entry))) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onTinkerRegister(TCompRegisterEvent.HighOvenHeatRegisterEvent event) {
		if (event.getRecipe() instanceof HeatRecipeTweaker) {
			return;
		} else {
			for (Map.Entry<FluidStack, Set<FluidStack>> entry : REMOVED_HEAT_RECIPES.entrySet()) {
				if (entry.getValue() == null && event.getRecipe().matches(null, entry.getKey())) {
					event.setCanceled(true);
				} else {
					for (FluidStack input : entry.getValue()) {
						if (event.getRecipe().matches(input, entry.getKey())) {
							event.setCanceled(true);
							break;
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onTinkerRegister(TCompRegisterEvent.HighOvenMixRegisterEvent event) {
		if (event.getRecipe() instanceof MixRecipeTweaker) {
			return;
		} else {
			for (Map.Entry<FluidStack, Set<FluidStack>> entry : REMOVED_MIX_RECIPES.entrySet()) {
				if (entry.getValue() == null && event.getRecipe().matches(null, entry.getKey())) {
					event.setCanceled(true);
				} else {
					for (FluidStack input : entry.getValue()) {
						if (event.getRecipe().matches(input, entry.getKey())) {
							event.setCanceled(true);
							break;
						}
					}
				}
			}
		}
	}
}
