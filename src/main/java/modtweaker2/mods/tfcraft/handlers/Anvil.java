package modtweaker2.mods.tfcraft.handlers;

import static modtweaker2.helpers.StackHelper.areEqual;
import static modtweaker2.helpers.InputHelper.toStack;
import static com.bioxx.tfc.api.Crafting.AnvilReq.getReqFromInt;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.mods.tfcraft.TFCHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.bioxx.tfc.api.Crafting.AnvilManager;
import com.bioxx.tfc.api.Crafting.AnvilRecipe;


@ZenClass("mods.tfcraft.Anvil")
public class Anvil {
	
	@ZenMethod
	public static void add(IItemStack input1, IItemStack input2, String plan, int value, boolean flux, int req, IItemStack output){
		MineTweakerAPI.apply(new Add(new AnvilRecipe(toStack(input1), toStack(input2), plan, value, flux, req, toStack(output))));
	}
	
	/**
	 * For parameter <code>req</code>:<p> 1==copper;<p> 2==bronze;<p> 3==Wrought Iron;<p>
	 * 4==Steel;<p> 5==Black Steel;<p> 6==Red Steel;<p> 7==Blue Steel;<p> default is stone.
	 */
	@ZenMethod
	public static void addWeld(IItemStack input1, IItemStack input2, int req, IItemStack output){
		MineTweakerAPI.apply(new AddWeld(new AnvilRecipe(toStack(input1), toStack(input2), true, getReqFromInt(req), toStack(output))));
	}
	
	@ZenMethod
	public static void remove(IItemStack stack){
		MineTweakerAPI.apply(new Remove(toStack(stack)));
	}
	
	@ZenMethod
	public static void removeWeld(IItemStack stack){
		MineTweakerAPI.apply(new RemoveWeld(toStack(stack)));
	}
	
	private static class Add extends BaseListAddition{
		public Add(AnvilRecipe recipe) {
			super("Anvil-Regular", TFCHelper.anvilRecipes, recipe);
		}
	}
	
	private static class AddWeld extends BaseListAddition{
		public AddWeld(AnvilRecipe recipe) {
			super("Anvil-Weld", TFCHelper.anvilWeldRecipes, recipe);
		}
	}
	
	private static class Remove extends BaseListRemoval{
		public Remove(ItemStack out){
			super("Anvil-Remove-Regular", TFCHelper.anvilRecipes, out);
		}

		@Override
		public void apply() {
			for (AnvilRecipe recipe : AnvilManager.getInstance().getRecipeList()){
				if (recipe.getCraftingResult() != null && areEqual(recipe.getCraftingResult(), stack)){
					recipes.add(recipe);
				}
			}
			super.apply();
		}
	}
	
	private static class RemoveWeld extends BaseListRemoval{
		public RemoveWeld(ItemStack out){
			super("Anvil-Remove-Weld", TFCHelper.anvilWeldRecipes, out);
		}
		
		@Override
		public void apply() {
			for (AnvilRecipe recipe : AnvilManager.getInstance().getRecipeList()){
				if (recipe.getCraftingResult() != null && recipe.getCraftingResult() == stack){
					recipes.add(recipe);
				}
			}
			super.apply();
		}
	}
}
