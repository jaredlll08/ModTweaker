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
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenClass("mods.tcomplement.highoven.IMixRecipe")
@ZenRegister
@ModOnly("tcomplement")
public class IMixRecipe {

	private ILiquidStack output, input;
	private int temp;
	private Map<IIngredient, Integer> oxidizers, reducers, purifiers;

	public IMixRecipe(ILiquidStack output, ILiquidStack input, int temp) {
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
	public IMixRecipe addOxidizer(IIngredient oxidizer, int consumeChance) {
		if (oxidizer instanceof IItemStack || oxidizer instanceof IOreDictEntry)
			// TODO: wan about unsupported IIngredient type
			this.oxidizers.put(oxidizer, consumeChance);
		return this;
	}

	@ZenMethod
	public IMixRecipe addReducer(IIngredient reducer, int consumeChance) {
		if (reducer instanceof IItemStack || reducer instanceof IOreDictEntry)
			this.reducers.put(reducer, consumeChance);
		return this;
	}

	@ZenMethod
	public IMixRecipe addPurifier(IIngredient purifier, int consumeChance) {
		if (purifier instanceof IItemStack || purifier instanceof IOreDictEntry)
			this.purifiers.put(purifier, consumeChance);
		return this;
	}

	@ZenMethod
	public IMixRecipe removeOxidizer(IIngredient oxidizer) {
		this.oxidizers.remove(oxidizer);
		return this;
	}

	@ZenMethod
	public IMixRecipe removeOReducer(IIngredient reducer) {
		this.oxidizers.remove(reducer);
		return this;
	}

	@ZenMethod
	public IMixRecipe removePurifier(IIngredient purifier) {
		this.oxidizers.remove(purifier);
		return this;
	}

	@ZenMethod
	public IMixRecipe removeAllOxidizer() {
		this.oxidizers = new LinkedHashMap<IIngredient, Integer>();
		return this;
	}

	@ZenMethod
	public IMixRecipe removeAllReducer() {
		this.reducers = new LinkedHashMap<IIngredient, Integer>();
		return this;
	}

	@ZenMethod
	public IMixRecipe removeAllPurifier() {
		this.purifiers = new LinkedHashMap<IIngredient, Integer>();
		return this;
	}

	@ZenMethod
	public void register() {
		MixRecipeTweaker recipe = new MixRecipeTweaker(InputHelper.toFluid(this.input),
				InputHelper.toFluid(this.output), temp);
		for (Map.Entry<IIngredient, Integer> entry : this.oxidizers.entrySet()) {
			if (entry.getKey() instanceof IItemStack) {
				recipe.addOxidizer(InputHelper.toStack((IItemStack) entry.getKey()), entry.getValue());
			} else if (entry.getKey() instanceof IOreDictEntry) {
				recipe.addOxidizer(((IOreDictEntry) entry.getKey()).getName(), entry.getValue());
			} else {
				CraftTweakerAPI.logWarning("addOxidizer only supports IItemStack or IOreDictEntry, ignored oxidizer "
						+ LogHelper.getStackDescription(entry.getKey()) + " of type "
						+ entry.getKey().getClass().getSimpleName());
			}
		}
		for (Map.Entry<IIngredient, Integer> entry : this.reducers.entrySet()) {
			if (entry.getKey() instanceof IItemStack) {
				recipe.addReducer(InputHelper.toStack((IItemStack) entry.getKey()), entry.getValue());
			} else if (entry.getKey() instanceof IOreDictEntry) {
				recipe.addReducer(((IOreDictEntry) entry.getKey()).getName(), entry.getValue());
			} else {
				CraftTweakerAPI.logWarning("addReducer only supports IItemStack or IOreDictEntry, ignored reducer "
						+ LogHelper.getStackDescription(entry.getKey()) + " of type "
						+ entry.getKey().getClass().getSimpleName());
			}
		}
		for (Map.Entry<IIngredient, Integer> entry : this.purifiers.entrySet()) {
			if (entry.getKey() instanceof IItemStack) {
				recipe.addPurifier(InputHelper.toStack((IItemStack) entry.getKey()), entry.getValue());
			} else if (entry.getKey() instanceof IOreDictEntry) {
				recipe.addPurifier(((IOreDictEntry) entry.getKey()).getName(), entry.getValue());
			} else {
				CraftTweakerAPI.logWarning("addPurifier only supports IItemStack or IOreDictEntry, ignored purifier "
						+ LogHelper.getStackDescription(entry.getKey()) + " of type "
						+ entry.getKey().getClass().getSimpleName());
			}
		}
		if (!recipe.isValid()) {
			CraftTweakerAPI.logError(String.format("The mix recipe for %s is invalid, it won't work !", LogHelper.getStackDescription(this.output)));
		}
		ModTweaker.LATE_ADDITIONS.add(new AddMixRecipe(recipe));
	}

	private static class AddMixRecipe extends BaseAction {
		private MixRecipeTweaker recipe;

		public AddMixRecipe(MixRecipeTweaker recipe) {
			super("HighOven.MixRecipe");
			this.recipe = recipe;
		}

		@Override
		public void apply() {
			TCompRegistry.registerMix(recipe);
		}

		@Override
		public String describe() {
			return String.format("Adding %s Recipe(s) for %s", this.name, this.getRecipeInfo());
		}

		@Override
		protected String getRecipeInfo() {
			return LogHelper.getStackDescription(recipe.getOutput());
		}
	}

}
