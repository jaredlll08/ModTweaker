package com.blamejared.compat.tcomplement.highoven;

import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;

import com.blamejared.compat.mantle.RecipeMatchIIngredient;
import com.blamejared.mtlib.helpers.InputHelper;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.liquid.ILiquidStack;
import knightminer.tcomplement.library.events.TCompRegisterEvent;
import knightminer.tcomplement.library.steelworks.MixAdditive;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.mantle.util.RecipeMatch;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.tcomplement.highoven.MixRecipeManager")
@ZenRegister
@ModOnly("tcomplement")
public class MixRecipeManager {
	private boolean init;
	private FluidStack input, output;
	private List<SimpleEntry<RecipeMatch, MixAdditive>> additives;
	private List<SimpleEntry<RecipeMatch, MixAdditive>> removedAdditives;

	public MixRecipeManager(ILiquidStack output, ILiquidStack input) {
		this.init = false;
		this.input = InputHelper.toFluid(input);
		this.output = InputHelper.toFluid(output);
		this.additives = new LinkedList<>();
		this.removedAdditives = new LinkedList<>();
	}

	@ZenMethod
	public MixRecipeManager addOxidizer(IIngredient oxidizer) {
		this.additives.add(new SimpleEntry<>(new RecipeMatchIIngredient(oxidizer), MixAdditive.OXIDIZER));
		return this;
	}

	@ZenMethod
	public MixRecipeManager addReducer(IIngredient reducer) {
		this.additives.add(new SimpleEntry<>(new RecipeMatchIIngredient(reducer), MixAdditive.REDUCER));
		return this;
	}

	@ZenMethod
	public MixRecipeManager addPurifier(IIngredient purifier) {
		this.additives.add(new SimpleEntry<>(new RecipeMatchIIngredient(purifier), MixAdditive.PURIFIER));
		return this;
	}

	@ZenMethod
	public MixRecipeManager removeOxidizer(IIngredient oxidizer) {
		this.removedAdditives.add(new SimpleEntry<>(new RecipeMatchIIngredient(oxidizer), MixAdditive.OXIDIZER));
		return this;
	}

	@ZenMethod
	public MixRecipeManager removeReducer(IIngredient reducer) {
		this.removedAdditives.add(new SimpleEntry<>(new RecipeMatchIIngredient(reducer), MixAdditive.REDUCER));
		return this;
	}

	@ZenMethod
	public MixRecipeManager removePurifier(IIngredient purifier) {
		this.removedAdditives.add(new SimpleEntry<>(new RecipeMatchIIngredient(purifier), MixAdditive.PURIFIER));
		return this;
	}

	public void register() {
		if (!this.init) {
			MinecraftForge.EVENT_BUS.register(this);
			init = true;
		}
	}

	@SubscribeEvent
	public void onTinkerRegister(TCompRegisterEvent.HighOvenMixRegisterEvent event) {
		if (event.getRecipe().matches(this.input, this.output)) {
			this.additives.forEach((SimpleEntry<RecipeMatch, MixAdditive> entry) -> event.getRecipe()
					.addAdditive(entry.getValue(), entry.getKey()));
		}
	}

	@SubscribeEvent
	public void onTinkerRegister(TCompRegisterEvent.HighOvenMixAdditiveEvent event) {
		if (event.getRecipe().matches(input, output)) {
			for (SimpleEntry<RecipeMatch, MixAdditive> entry : removedAdditives) {
				if (event.getType() == entry.getValue() && entry.getKey()
						.matches((NonNullList<ItemStack>) event.getAdditive().getInputs()).isPresent()) {
					event.setCanceled(true);
					break;
				}
			}
		}
	}

}
