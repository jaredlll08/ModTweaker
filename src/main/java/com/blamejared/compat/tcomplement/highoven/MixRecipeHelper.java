package com.blamejared.compat.tcomplement.highoven;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.blamejared.ModTweaker;
import com.blamejared.compat.tcomplement.highoven.recipes.MixRecipeTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.google.common.collect.ImmutableList;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.oredict.IOreDictEntry;
import knightminer.tcomplement.library.TCompRegistry;
import knightminer.tcomplement.library.steelworks.IMixRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenClass("mods.tcomplement.highoven.IMixRecipe")
@ZenRegister
@ModOnly("tcomplement")
public class MixRecipeHelper {

	private ILiquidStack output, input;
	private int temp;
	private Map<IIngredient, Integer> oxidizers, reducers, purifiers;

	public MixRecipeHelper(ILiquidStack output, ILiquidStack input, int temp) {
		this.output = output;
		this.input = input;
		this.temp = temp;
		this.oxidizers = new LinkedHashMap<IIngredient, Integer>();
		this.reducers = new LinkedHashMap<IIngredient, Integer>();
		this.purifiers = new LinkedHashMap<IIngredient, Integer>();
	}

	@ZenGetter("output")
	public ILiquidStack getOutput() {
		return this.output;
	}

	@ZenSetter("output")
	public void setOutput(ILiquidStack output) {
		this.output = output;
	}

	@ZenGetter("input")
	public ILiquidStack getInput() {
		return this.input;
	}

	@ZenSetter("input")
	public void setInput(ILiquidStack input) {
		this.input = input;
	}

	@ZenGetter("temp")
	public int getTemp() {
		return this.temp;
	}

	@ZenSetter("temp")
	public void setTemp(int temp) {
		this.temp = temp;
	}

	@ZenGetter("oxidizers")
	public List<IIngredient> getOxidizers() {
		return ImmutableList.copyOf(this.oxidizers.keySet());
	}

	@ZenGetter("reducers")
	public List<IIngredient> getReducers() {
		return ImmutableList.copyOf(this.reducers.keySet());
	}

	@ZenGetter("purifiers")
	public List<IIngredient> getPurifiers() {
		return ImmutableList.copyOf(this.purifiers.keySet());
	}

	@ZenMethod
	public int getOxidizerChance(IIngredient oxidizer) {
		if (this.oxidizers.containsKey(oxidizer)) {
			return this.oxidizers.get(oxidizer);
		} else {
			return -1;
		}
	}

	@ZenMethod
	public int getReducerChance(IIngredient reducer) {
		if (this.reducers.containsKey(reducer)) {
			return this.reducers.get(reducer);
		} else {
			return -1;
		}
	}

	@ZenMethod
	public int getPurifierChance(IIngredient purifier) {
		if (this.purifiers.containsKey(purifier)) {
			return this.purifiers.get(purifier);
		} else {
			return -1;
		}
	}

	@ZenMethod
	public MixRecipeHelper addOxidizer(IIngredient oxidizer, int consumeChance) {
		if (oxidizer instanceof IItemStack || oxidizer instanceof IOreDictEntry) {
			this.oxidizers.put(oxidizer, consumeChance);
		} else {
			CraftTweakerAPI.logWarning("addOxidizer only supports IItemStack or IOreDictEntry, ignored oxidizer "
					+ LogHelper.getStackDescription(oxidizer) + " of type " + oxidizer.getClass().getSimpleName());
		}
		return this;
	}

	@ZenMethod
	public MixRecipeHelper addReducer(IIngredient reducer, int consumeChance) {
		if (reducer instanceof IItemStack || reducer instanceof IOreDictEntry) {
			this.reducers.put(reducer, consumeChance);
		} else {
			CraftTweakerAPI.logWarning("addReducer only supports IItemStack or IOreDictEntry, ignored reducer "
					+ LogHelper.getStackDescription(reducer) + " of type " + reducer.getClass().getSimpleName());
		}
		return this;
	}

	@ZenMethod
	public MixRecipeHelper addPurifier(IIngredient purifier, int consumeChance) {
		if (purifier instanceof IItemStack || purifier instanceof IOreDictEntry) {
			this.purifiers.put(purifier, consumeChance);
		} else {
			CraftTweakerAPI.logWarning("addPurifier only supports IItemStack or IOreDictEntry, ignored purifier "
					+ LogHelper.getStackDescription(purifier) + " of type " + purifier.getClass().getSimpleName());
		}
		return this;
	}

	@ZenMethod
	public MixRecipeHelper removeOxidizer(IIngredient oxidizer) {
		this.oxidizers.remove(oxidizer);
		return this;
	}

	@ZenMethod
	public MixRecipeHelper removeOReducer(IIngredient reducer) {
		this.oxidizers.remove(reducer);
		return this;
	}

	@ZenMethod
	public MixRecipeHelper removePurifier(IIngredient purifier) {
		this.oxidizers.remove(purifier);
		return this;
	}

	@ZenMethod
	public MixRecipeHelper removeAllOxidizer() {
		this.oxidizers = new LinkedHashMap<IIngredient, Integer>();
		return this;
	}

	@ZenMethod
	public MixRecipeHelper removeAllReducer() {
		this.reducers = new LinkedHashMap<IIngredient, Integer>();
		return this;
	}

	@ZenMethod
	public MixRecipeHelper removeAllPurifier() {
		this.purifiers = new LinkedHashMap<IIngredient, Integer>();
		return this;
	}

	@ZenMethod
	public void register() {
		ModTweaker.LATE_ADDITIONS.add(
				new AddMixRecipe(this.output, this.input, this.temp + 300, this.oxidizers, this.reducers, this.purifiers));
	}

	private static class AddMixRecipe extends BaseAction {
		private ILiquidStack input, output;
		private int temp;
		private Map<IIngredient, Integer> oxydizers, reducers, purifiers;

		public AddMixRecipe(ILiquidStack output, ILiquidStack input, int temp, Map<IIngredient, Integer> oxydizers,
				Map<IIngredient, Integer> reducers, Map<IIngredient, Integer> purifiers) {
			super("HighOven.MixRecipe");
			this.input = input;
			this.output = output;
			this.temp = temp;
			this.oxydizers = new LinkedHashMap<>();
			this.oxydizers.putAll(oxydizers);
			this.reducers = new LinkedHashMap<>();
			this.reducers.putAll(reducers);
			this.purifiers = new LinkedHashMap<>();
			this.purifiers.putAll(purifiers);
		}

		@Override
		public void apply() {
			IMixRecipe recipe = TCompRegistry.registerMix(
					new MixRecipeTweaker(InputHelper.toFluid(this.input), InputHelper.toFluid(this.output), this.temp));
			for (Map.Entry<IIngredient, Integer> entry : this.oxydizers.entrySet()) {
				if (entry.getKey() instanceof IItemStack)
					recipe.addOxidizer(InputHelper.toStack((IItemStack) entry.getKey()), entry.getValue());
				if (entry.getKey() instanceof IOreDictEntry)
					recipe.addOxidizer(((IOreDictEntry) entry.getKey()).getName(), entry.getValue());
			}
			for (Map.Entry<IIngredient, Integer> entry : this.reducers.entrySet()) {
				if (entry.getKey() instanceof IItemStack)
					recipe.addReducer(InputHelper.toStack((IItemStack) entry.getKey()), entry.getValue());
				if (entry.getKey() instanceof IOreDictEntry)
					recipe.addReducer(((IOreDictEntry) entry.getKey()).getName(), entry.getValue());
			}
			for (Map.Entry<IIngredient, Integer> entry : this.purifiers.entrySet()) {
				if (entry.getKey() instanceof IItemStack)
					recipe.addPurifier(InputHelper.toStack((IItemStack) entry.getKey()), entry.getValue());
				if (entry.getKey() instanceof IOreDictEntry)
					recipe.addPurifier(((IOreDictEntry) entry.getKey()).getName(), entry.getValue());
			}
		}

		@Override
		public String describe() {
			return String.format("Adding %s Recipe(s) for %s", this.name, this.getRecipeInfo());
		}

		@Override
		protected String getRecipeInfo() {
			return LogHelper.getStackDescription(LogHelper.getStackDescription(output));
		}
	}
}
