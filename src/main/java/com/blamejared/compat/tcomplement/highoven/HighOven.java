package com.blamejared.compat.tcomplement.highoven;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.blamejared.ModTweaker;
import com.blamejared.compat.tcomplement.Overrides;
import com.blamejared.compat.tcomplement.highoven.recipes.HeatRecipeTweaker;
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
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.tcomplement.highoven.HighOven")
@ZenRegister
@ModOnly("tcomplement")
public class HighOven {

	public static final Map<IItemStack, SimpleImmutableEntry<Integer, Integer>> REMOVED_FUELS = new LinkedHashMap<>();
	public static final Map<ILiquidStack, ILiquidStack> REMOVED_HEAT_RECIPES = new LinkedHashMap<>();
	public static final List<ILiquidStack> REMOVED_MIX_RECIPES = new LinkedList<>();

	private static boolean init = false;

	private static void init() {
		if (!init) {
			MinecraftForge.EVENT_BUS.register(new Overrides());
			init = true;
		}
	}

	@ZenMethod
	public static void removeFuel(IItemStack stack, int time, int rate) {
		init();
		CraftTweakerAPI.apply(new HighOven.RemoveFuel(stack, time, rate));
	}

	@ZenMethod
	public static void addFuel(IItemStack fuel, int burnTime, int tempRate) {
		ModTweaker.LATE_ADDITIONS.add(new HighOven.AddFuel(InputHelper.toStack(fuel), burnTime, tempRate));
	}

	@ZenMethod
	public static void addFuel(IOreDictEntry fuel, int burnTime, int tempRate) {
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
				TCompRegistry.registerFuel(itemFuel, time, rate);
			if (oreFuel != null)
				TCompRegistry.registerFuel(oreFuel, time, rate);
		}

		@Override
		public String describe() {
			return String.format("Adding %s recipe as high oven fuel", this.getRecipeInfo());
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
		private Integer time;
		private Integer rate;

		public RemoveFuel(IItemStack fuel, int time, int rate) {
			super("HighOven.Fuel");
			this.fuel = fuel;
			this.time = time;
			this.rate = rate;
		};

		@Override
		public void apply() {
			REMOVED_FUELS.put(fuel, new SimpleImmutableEntry<Integer, Integer>(time, rate));
		}

		@Override
		public String describe() {
			return String.format("Removing %s as high oven fuel", this.getRecipeInfo());
		}

		@Override
		public String getRecipeInfo() {
			return LogHelper.getStackDescription(fuel);
		}

	}

	@ZenMethod
	public static void removeHeatRecipe(ILiquidStack output, @Optional ILiquidStack input) {
		CraftTweakerAPI.apply(new RemoveHeat(input, output));
	}

	@ZenMethod
	public static void addHeatRecipe(ILiquidStack output, ILiquidStack input, int temp) {
		ModTweaker.LATE_ADDITIONS
				.add(new HighOven.AddHeat(new FluidStack(InputHelper.getFluid(output), output.getAmount() / 20),
						new FluidStack(InputHelper.getFluid(input), input.getAmount() / 20),
						temp + 300)
				);
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
		CraftTweakerAPI.apply(new RemoveMix(output));
	}

	@ZenMethod
	public static IMixRecipe newMixRecipe(ILiquidStack output, ILiquidStack input, int temp) {
		init();
		return new IMixRecipe(output, input, temp);
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
		CraftTweakerAPI.logInfo(String.format("Analysing if high oven fuel for %s time=%d, rate=%d whould be cancelled",
				LogHelper.getListDescription(event.getRecipe().getFuels()),
				event.getRecipe().getTime(),
				event.getRecipe().getRate()
				));
		for (Map.Entry<IItemStack, SimpleImmutableEntry<Integer, Integer>> entry : REMOVED_FUELS.entrySet()) {
			CraftTweakerAPI.logInfo(String.format("Trying against %s,%d,%d", LogHelper.getStackDescription(entry.getKey()), entry.getValue().getKey(), entry.getValue().getValue()));
			if (event.getRecipe().matches(InputHelper.toStack(entry.getKey()))) {
				if (event.getRecipe().getTime() == entry.getValue().getKey()
						&& event.getRecipe().getTime() == entry.getValue().getValue()) {
					event.setCanceled(true);
					CraftTweakerAPI.logInfo("cancelled fuel registration");
				} else {
					CraftTweakerAPI.logInfo("time or rate did not match");
				}
			} else {
				CraftTweakerAPI.logInfo("ItemStack did not match the entry");
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
