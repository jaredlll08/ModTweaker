package com.blamejared.compat.tcomplement.highoven;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.blamejared.ModTweaker;
import com.blamejared.compat.mantle.RecipeMatchIIngredient;
import com.blamejared.compat.tcomplement.highoven.recipes.HeatRecipeTweaker;
import com.blamejared.compat.tcomplement.highoven.recipes.HighOvenFuelTweaker;
import com.blamejared.compat.tcomplement.highoven.recipes.MixRecipeTweaker;
import com.blamejared.compat.tconstruct.recipes.MeltingRecipeTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.mc1120.item.MCItemStack;
import knightminer.tcomplement.library.TCompRegistry;
import knightminer.tcomplement.library.events.TCompRegisterEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
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
	public static final Map<ILiquidStack, IItemStack> REMOVED_OVERRIDES = new LinkedHashMap<>();
	public static final List<Pair<FluidStack, FluidStack>> REMOVED_HEAT_RECIPES = new LinkedList<>();
	public static final List<Pair<FluidStack, FluidStack>> REMOVED_MIX_RECIPES = new LinkedList<>();

	private static boolean init = false;

	private static void init() {
		if (!init) {
			MinecraftForge.EVENT_BUS.register(new HighOven());
			init = true;
		}
	}

	/*-------------------------------------------------------------------------*\
	| High Oven Fuels                                                           |
	\*-------------------------------------------------------------------------*/
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
			super("High Oven fuel");
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
			return String.format("Adding %s as %s", this.getRecipeInfo(), this.name);
		}

		@Override
		public String getRecipeInfo() {
			return LogHelper.getStackDescription(fuel);
		}
	}

	private static class RemoveFuel extends BaseAction {
		private IIngredient fuel;

		public RemoveFuel(IIngredient fuel) {
			super("High Oven fuel");
			this.fuel = fuel;
		};

		@Override
		public void apply() {
			REMOVED_FUELS.add(fuel);
		}

		@Override
		public String describe() {
			return String.format("Removing %s as %s", this.getRecipeInfo(), this.name);
		}

		@Override
		public String getRecipeInfo() {
			return LogHelper.getStackDescription(fuel);
		}

	}

	/*-------------------------------------------------------------------------*\
	| High Oven Melting                                                         |
	\*-------------------------------------------------------------------------*/

	@ZenMethod
	public static void addMeltingOverride(ILiquidStack output, IIngredient input, @Optional int temp) {
		init();
		ModTweaker.LATE_ADDITIONS
				.add(new HighOven.AddMelting(InputHelper.toFluid(output), input, (temp == 0 ? -1 : temp)));
	}

	@ZenMethod
	public static void removeMeltingOverride(ILiquidStack output, @Optional IItemStack input) {
		init();
		CraftTweakerAPI.apply(new HighOven.RemoveMelting(output, input));
	}

	private static class AddMelting extends BaseAction {
		private IIngredient input;
		private FluidStack output;
		private int temp;

		public AddMelting(FluidStack output, IIngredient input, int temp) {
			super("High Oven melting override");
			this.input = input;
			this.output = output;
			this.temp = temp;
		}

		@Override
		public void apply() {
			if (temp > 0) {
				TCompRegistry.registerHighOvenOverride(
						new MeltingRecipeTweaker(new RecipeMatchIIngredient(input, output.amount), output, temp));
			} else {
				TCompRegistry.registerHighOvenOverride(
						new MeltingRecipeTweaker(new RecipeMatchIIngredient(input, output.amount), output));
			}

		}

		@Override
		public String describe() {
			return String.format("Adding %s for %s", this.name, this.getRecipeInfo());
		}

		@Override
		protected String getRecipeInfo() {
			return LogHelper.getStackDescription(input) + ", now yields " + LogHelper.getStackDescription(output);
		}

	}

	private static class RemoveMelting extends BaseAction {
		private ILiquidStack output;
		private IItemStack input;

		public RemoveMelting(ILiquidStack output, IItemStack input) {
			super("High Oven melting override");
			this.input = input;
			this.output = output;
		}

		@Override
		public void apply() {
			REMOVED_OVERRIDES.put(output, input);
		}

		@Override
		public String describe() {
			return String.format("Removing %s Recipe(s) for %s", this.name, this.getRecipeInfo());
		}

		@Override
		protected String getRecipeInfo() {
			return LogHelper.getStackDescription(output);
		}
	}
	/*-------------------------------------------------------------------------*\
	| High Oven Heat                                                            |
	\*-------------------------------------------------------------------------*/

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
			super("High Oven Heat");
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
			return String.format("Adding %s Recipe for %s", this.name, this.getRecipeInfo());
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
			super("High Oven Heat");
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

	/*-------------------------------------------------------------------------*\
	| High Oven Mix                                                             |
	\*-------------------------------------------------------------------------*/
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
			super("High Oven Mix");
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

	/*-------------------------------------------------------------------------*\
	| Event handlers                                                            |
	\*-------------------------------------------------------------------------*/

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

	@SubscribeEvent
	public void onHighOvenOverrideRegister(TCompRegisterEvent.HighOvenOverrideRegisterEvent event) {
		if (event.getRecipe() instanceof MeltingRecipeTweaker) {
			return;
		}
		for (Map.Entry<ILiquidStack, IItemStack> entry : REMOVED_OVERRIDES.entrySet()) {
			if (event.getRecipe().getResult().isFluidEqual(((FluidStack) entry.getKey().getInternal()))) {
				if (entry.getValue() != null) {
					if (event.getRecipe().input
							.matches(NonNullList.withSize(1, (ItemStack) entry.getValue().getInternal())).isPresent()) {
						event.setCanceled(true);
					}
				} else
					event.setCanceled(true);
			}
		}
	}
}
