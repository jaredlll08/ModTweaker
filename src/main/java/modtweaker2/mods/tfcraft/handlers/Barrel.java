package modtweaker2.mods.tfcraft.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toStack;

import java.util.ArrayList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.mods.tfcraft.TFCHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.bioxx.tfc.api.Crafting.BarrelLiquidToLiquidRecipe;
import com.bioxx.tfc.api.Crafting.BarrelManager;
import com.bioxx.tfc.api.Crafting.BarrelRecipe;

@ZenClass("mods.tfcraft.Barrel")
public class Barrel {
	
	/**
	 * @param outputFluid If you want this recipe consume fluid, <code>outputFluid=inputFluid</code>
	 * @param time How long the recipe finished. Unit is TFC hour.
	 * @param techLv Set to 0 if you want vessel to be able to use your new recipe, otherwise you will set to 1
	 * @param removeFluid Set false if there is special need
	 * @param allowAny By default it is <code>true</code>
	 */
	@ZenMethod
	public static void addSealed(IItemStack inputItem, ILiquidStack inputFluid, IItemStack outputItem, ILiquidStack outputFluid, int time, int techLv, boolean removeFluid, boolean allowAny){
		MineTweakerAPI.apply(new AddRecipe(new BarrelRecipe(toStack(inputItem), toFluid(inputFluid), toStack(outputItem), toFluid(outputFluid), time).setMinTechLevel(techLv).setRemovesLiquid(removeFluid).setAllowAnyStack(allowAny)));
	}
	
	
	@ZenMethod
	public static void addUnsealed(IItemStack inputItem, ILiquidStack inputFluid, IItemStack outputItem, ILiquidStack outputFluid, int techLv, boolean removeFluid, boolean allowAny){
		MineTweakerAPI.apply(new AddRecipe(new BarrelRecipe(toStack(inputItem), toFluid(inputFluid), toStack(outputItem), toFluid(outputFluid)).setSealedRecipe(false).setMinTechLevel(techLv).setRemovesLiquid(removeFluid).setAllowAnyStack(allowAny)));
	}
	
	/**
	 * 
	 * @param inputInBarrel The input fluid which is in barrel
	 * @param input The input fluid which is in a fluid container, like a water bucket
	 * @param output The output fluid
	 */
	@ZenMethod
	public static void addFluidToFluid(ILiquidStack inputInBarrel, ILiquidStack input, ILiquidStack output){
		MineTweakerAPI.apply(new AddRecipe(new BarrelLiquidToLiquidRecipe(toFluid(inputInBarrel), toFluid(input), toFluid(output))));
	}
	
	@ZenMethod
	public static void remove(IItemStack output){
		MineTweakerAPI.apply(new RemoveRecipe(toStack(output)));
	}
	
	@ZenMethod
	public static void remove(ILiquidStack output){
		MineTweakerAPI.apply(new RemoveRecipe(toFluid(output)));
	}

	private static class AddRecipe extends BaseListAddition{
		public AddRecipe(BarrelRecipe recipe){
			super("Barrel", TFCHelper.barrelRecipes, recipe);
		}
	}
	
	private static class RemoveRecipe extends BaseListRemoval{
		public RemoveRecipe(ItemStack stack){
			super("Barrel-Remove", TFCHelper.barrelRecipes, stack);
		}
		
		public RemoveRecipe(FluidStack fluid){
			super("Barrel-Remove", TFCHelper.barrelRecipes, fluid);
		}

		@Override
		public void apply() {
			ArrayList<BarrelRecipe> toRemove = new ArrayList<BarrelRecipe>();
			for (BarrelRecipe recipe : BarrelManager.getInstance().getRecipes()){
				if (recipe.getRecipeOutIS() != null && recipe.getRecipeOutIS() == stack){
					toRemove.add(recipe);
				}
			}
			for (BarrelRecipe recipe : BarrelManager.getInstance().getRecipes()){
				if (recipe.getRecipeOutFluid() != null && recipe.getRecipeOutFluid() == fluid){
					toRemove.add(recipe);
				}
			}
			for (BarrelRecipe aRecipe : toRemove){
				TFCHelper.barrelRecipes.remove(aRecipe);
			}
		}
	}
}
