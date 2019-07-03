package com.blamejared.compat.tcomplement.highoven;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.blamejared.ModTweaker;
import com.blamejared.compat.tcomplement.highoven.recipes.HeatRecipeTweaker;
import com.blamejared.compat.tcomplement.highoven.recipes.HighOvenFuelTweaker;
import com.blamejared.compat.tcomplement.highoven.recipes.MixRecipeTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.oredict.IOreDictEntry;
import knightminer.tcomplement.library.TCompRegistry;
import knightminer.tcomplement.library.events.TCompRegisterEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.mantle.util.RecipeMatch;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.tcomplement.highoven.HighOven")
@ZenRegister
@ModOnly("tcomplement")
public class HighOven {

	public static final List<IItemStack> REMOVED_FUELS = new LinkedList<>();
	public static final Map<ILiquidStack, ILiquidStack> REMOVED_HEAT_RECIPES = new LinkedHashMap<>();
	public static final List<ILiquidStack> REMOVED_MIX_RECIPES = new LinkedList<>();

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
	public static void addFuel(IItemStack fuel, int burnTime, int tempRate) {
		init();
		ModTweaker.LATE_ADDITIONS.add(new HighOven.AddFuel(InputHelper.toStack(fuel), burnTime, tempRate));
	}

	@ZenMethod
	public static void addFuel(IOreDictEntry fuel, int burnTime, int tempRate) {
		init();
		ModTweaker.LATE_ADDITIONS.add(new HighOven.AddFuel(fuel.getName(), burnTime, tempRate));
	}

	private static class AddFuel extends BaseAction {
		private ItemStack itemFuel;
		private String oreFuel;
		private int time;
		private int rate;

		public AddFuel(ItemStack fuel, int time, int rate) {
			super("HighOven.Fuel");
			this.itemFuel = fuel;
			this.oreFuel = null;
			this.time = time;
			this.rate = rate;
		}

		public AddFuel(String fuel, int time, int rate) {
			super("HighOven.Fuel");
			this.itemFuel = null;
			this.oreFuel = fuel;
			this.time = time;
			this.rate = rate;
		}

		@Override
		public void apply() {
			if (itemFuel != null)
				TCompRegistry.registerFuel(new HighOvenFuelTweaker(RecipeMatch.of(itemFuel), time, rate));
			if (oreFuel != null)
				TCompRegistry.registerFuel(new HighOvenFuelTweaker(RecipeMatch.of(oreFuel), time, rate));
		}

		@Override
		public String describe() {
			return String.format("Adding %s as high oven fuel", this.getRecipeInfo());
		}

		@Override
		public String getRecipeInfo() {
			if (itemFuel != null)
				return itemFuel.getItem().getUnlocalizedName();
			else
				return oreFuel;
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
				.add(new HighOven.AddHeat(InputHelper.toFluid(output), InputHelper.toFluid(input), temp + 300));
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
			REMOVED_HEAT_RECIPES.put(output, input);
		}

		@Override
		public String describe() {
			return String.format("Removing %s Recipe(s) for %s", this.name, this.getRecipeInfo());
		}

		@Override
		public String getRecipeInfo() {
			return LogHelper.getStackDescription(output);
		}

	}

	@ZenMethod
	public static void removeMixRecipe(ILiquidStack output) {
		init();
		CraftTweakerAPI.apply(new RemoveMix(output));
	}

	@ZenMethod
	public static MixRecipeHelper newMixRecipe(ILiquidStack output, ILiquidStack input, int temp) {
		init();
		return new MixRecipeHelper(output, input, temp);
	}

	private static class RemoveMix extends BaseAction {
		private ILiquidStack output;

		public RemoveMix(ILiquidStack output) {
			super("HighOven.MixRecipe");
			this.output = output;
		}

		@Override
		public void apply() {
			REMOVED_MIX_RECIPES.add(output);
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
			for (Map.Entry<ILiquidStack, ILiquidStack> entry : REMOVED_HEAT_RECIPES.entrySet()) {
				if (event.getRecipe().getOutput().isFluidEqual((FluidStack) entry.getKey().getInternal())) {
					if (entry.getValue() != null) {
						if (event.getRecipe().getInput().isFluidEqual((FluidStack) entry.getValue().getInternal())) {
							event.setCanceled(true);
						}
					} else {
						event.setCanceled(true);
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
			for (ILiquidStack entry : REMOVED_MIX_RECIPES) {
				if (event.getRecipe().getOutput().isFluidEqual((FluidStack) entry.getInternal())) {
					event.setCanceled(true);
				}
			}
		}
	}
}
