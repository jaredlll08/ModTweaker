package modtweaker.mods.tconstruct.handlers;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.*;
import mezz.jei.api.recipe.IRecipeWrapper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.*;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker.mods.tconstruct.TConstructHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.*;
import slimeknights.tconstruct.plugin.jei.*;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.tconstruct.Smeltery")
public class Smeltery {
	
	public static final String nameFuel = "TConstruct Smeltery - Fuel";
	public static final String nameMelting = "TConstruct Smeltery - Melting";
	public static final String nameAlloy = "TConstruct Smeltery - Alloy";
	
	/**********************************************
	 * TConstruct Alloy Recipes
	 **********************************************/
	
	// Adding a TConstruct Alloy recipe
	@ZenMethod
	public static void addAlloy(ILiquidStack output, ILiquidStack[] input) {
		if(input == null || output == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", nameAlloy));
			return;
		}
		
		MineTweakerAPI.apply(new AddAlloy(new AlloyRecipe(toFluid(output), toFluids(input))));
	}
	
	// Passes the list to the base list implementation, and adds the recipe
	private static class AddAlloy extends BaseListAddition<AlloyRecipe> {
		
		public AddAlloy(AlloyRecipe recipe) {
			super("TConstruct Smeltery - Alloy", TConstructHelper.alloys);
			this.recipes.add(recipe);
		}
		
		@Override
		protected String getRecipeInfo(AlloyRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getResult());
		}
        
    }
	
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Removing a TConstruct Alloy recipe
	@ZenMethod
	public static void removeAlloy(IIngredient output) {
		List<AlloyRecipe> recipes = new LinkedList<AlloyRecipe>();
		
		for(AlloyRecipe r : TConstructHelper.alloys) {
			if(r != null && matches(output, toILiquidStack(r.getResult()))) {
				recipes.add(r);
			}
		}
		
		if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new RemoveAlloy(recipes));
		} else {
			LogHelper.logError(String.format("No %s recipes found for %s. Command ignored!", nameAlloy, output.toString()));
		}
		
	}
	
	// Removes a recipe, apply is never the same for anything, so will always
	// need to override it
	private static class RemoveAlloy extends BaseListRemoval<AlloyRecipe> {
		
		public RemoveAlloy(List<AlloyRecipe> recipes) {
			super(nameAlloy, TConstructHelper.alloys, recipes);
		}
		
		
		@Override
		protected String getRecipeInfo(AlloyRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getResult());
		}
		
	}
	
	/**********************************************
	 * TConstruct Melting Recipes
	 **********************************************/
	
	// Adding a TConstruct Melting recipe
	@ZenMethod
	public static void addMelting(ILiquidStack output, IIngredient input, int temp) {
		if(input == null || output == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", nameMelting));
			return;
		}
		
		List<MeltingRecipe> recipes = new LinkedList<>();
		
		for(IItemStack in : input.getItems()) {
				recipes.add(new MeltingRecipe(toStack(in), toFluid(output), temp));
		}
		
		if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new AddMelting(recipes));
		} else {
			LogHelper.logError(String.format("No %s recipes could be added for input %s.", nameMelting, input.toString()));
		}
	}
	
	private static class AddMelting extends BaseListAddition<MeltingRecipe> {
		
		public AddMelting(List<MeltingRecipe> recipes) {
			super(nameMelting, null, recipes);
		}
		
		@Override
		public void apply() {
			for(MeltingRecipe recipe : recipes) {
				TinkerRegistry.registerMelting(new slimeknights.tconstruct.library.smeltery.MeltingRecipe(RecipeMatch.of(recipe.input), recipe.fluid, recipe.temp));
				successful.add(recipe);
				MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new slimeknights.tconstruct.library.smeltery.MeltingRecipe(RecipeMatch.of(recipe.input), recipe.fluid));
			}
		}
		
		@Override
		public void undo() {
			for(MeltingRecipe recipe : successful) {
				TConstructHelper.smeltingList.remove(recipe);
				MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new slimeknights.tconstruct.library.smeltery.MeltingRecipe(RecipeMatch.of(recipe.input), recipe.fluid, recipe.temp));
			}
		}
		
		@Override
		public String getRecipeInfo(MeltingRecipe recipe) {
			return LogHelper.getStackDescription(recipe.input);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Removing a TConstruct Melting recipe
	@ZenMethod
	public static void removeMelting(IItemStack input) {
		List<slimeknights.tconstruct.library.smeltery.MeltingRecipe> recipes = new LinkedList<slimeknights.tconstruct.library.smeltery.MeltingRecipe>();
		
		for(slimeknights.tconstruct.library.smeltery.MeltingRecipe meta : TConstructHelper.smeltingList) {
			if(meta.input.matches(toStacks(input.getItems().toArray(new IItemStack[]{}))) != null) {
				recipes.add(meta);
			}
		}
		
		if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new RemoveMelting(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", nameMelting, input.toString()));
		}
	}
	
	private static class RemoveMelting extends BaseListRemoval<slimeknights.tconstruct.library.smeltery.MeltingRecipe> {
		
		public RemoveMelting(List<slimeknights.tconstruct.library.smeltery.MeltingRecipe> recipes) {
			super(nameMelting, TConstructHelper.smeltingList, recipes);
		}
		
		@Override
		public void apply() {
			for(slimeknights.tconstruct.library.smeltery.MeltingRecipe recipe : recipes) {
				TConstructHelper.smeltingList.remove(recipe);
				successful.add(recipe);
				MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new SmeltingRecipeWrapper(recipe));
			}
		}
		
		@Override
		public void undo() {
			for(slimeknights.tconstruct.library.smeltery.MeltingRecipe recipe : successful) {
				TinkerRegistry.registerMelting(recipe);
				MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(new SmeltingRecipeWrapper(recipe));
			}
		}
		
		@Override
		public String getRecipeInfo(slimeknights.tconstruct.library.smeltery.MeltingRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getResult());
		}
	}
	
	protected static class MeltingRecipe {
		
		public final ItemStack input;
		public final FluidStack fluid;
		public final int temp;
        
        public MeltingRecipe(ItemStack input, FluidStack fluid, int temp) {
            this.input = input;
            this.fluid = fluid;
            this.temp = temp;
        }
    }
	
	/**********************************************
	 * TConstruct Fuel Recipes
	 **********************************************/
	
	@ZenMethod
	public static void addFuel(ILiquidStack liquid) {
		if(liquid == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", nameFuel));
			return;
		}
		
		List<FluidStack> stacks = new ArrayList<FluidStack>();
		stacks.add(toFluid(liquid));
		MineTweakerAPI.apply(new AddFuel(stacks));
	}
	
	public static class AddFuel extends BaseListAddition<FluidStack> {
		
		public AddFuel(List<FluidStack> recipes) {
			super(Smeltery.nameFuel, TConstructHelper.fuelList, recipes);
		}
		
		@Override
		public String getRecipeInfo(FluidStack recipe) {
			return LogHelper.getStackDescription(recipe);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@ZenMethod
	public static void removeFuel(IIngredient input) {
		List<FluidStack> recipes = new ArrayList<FluidStack>();
		
		for(FluidStack fuel : TConstructHelper.fuelList) {
			if(fuel != null && matches(input, toILiquidStack(fuel))) {
				recipes.add(fuel);
			}
		}
		
		if(!recipes.isEmpty()) {
			MineTweakerAPI.apply(new RemoveFuel(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe for %s found. Command ignored!", Smeltery.nameFuel, input.toString()));
		}
	}
	
	public static class RemoveFuel extends BaseListRemoval<FluidStack> {
		
		public RemoveFuel(List<FluidStack> recipes) {
			super(Smeltery.nameFuel, TConstructHelper.fuelList, recipes);
		}
		
		@Override
		protected String getRecipeInfo(FluidStack recipe) {
			return LogHelper.getStackDescription(recipe);
		}
	}
}
