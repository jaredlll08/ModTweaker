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

import com.bioxx.tfc.api.Crafting.BarrelManager;
import com.bioxx.tfc.api.Crafting.BarrelRecipe;

@ZenClass("mods.tfcraft.Barrel")
public class Barrel {
	
	/**
	 * Note: by default the parameter <code>sealed</code> should be <code>true</code>
	 * <p>And, the parameter <code>time</code> uses the unit of "hour" in TFCraft
	 * @param techLv Set to 0 if you want vessel to be able to use your new recipe, otherwise you will set to 1
	 */
	@ZenMethod
	public static void add(IItemStack inputItem, ILiquidStack inputFluid, IItemStack outputItem, ILiquidStack outputFluid, int time, int techLv, boolean sealed){
		MineTweakerAPI.apply(new AddRecipe(new BarrelRecipe(toStack(inputItem), toFluid(inputFluid), toStack(outputItem), toFluid(outputFluid), time).setSealedRecipe(sealed).setMinTechLevel(techLv)));
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
				if (recipe.getInItem() != null && recipe.getRecipeOutIS() == stack){
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
