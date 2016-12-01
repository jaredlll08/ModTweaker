package modtweaker.mods.embers.handlers;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.helpers.StackHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.blamejared.mtlib.utils.BaseMapAddition;
import com.blamejared.mtlib.utils.BaseMapRemoval;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.oredict.IOreDictEntry;
import modtweaker.mods.embers.Embers;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import teamroots.embers.item.EnumStampType;
import teamroots.embers.recipe.FluidMixingRecipe;
import teamroots.embers.recipe.ItemMeltingOreRecipe;
import teamroots.embers.recipe.ItemMeltingRecipe;
import teamroots.embers.recipe.ItemStampingOreRecipe;
import teamroots.embers.recipe.ItemStampingRecipe;
import teamroots.embers.recipe.RecipeRegistry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.embers.Mixer")
public class Mixer {
	public static String name = "Embers Mixer";
	
	@ZenMethod
	public static void addRecipe(ILiquidStack input1, ILiquidStack input2, ILiquidStack input3, ILiquidStack input4, ILiquidStack output) {
		ArrayList<FluidStack> fluids = new ArrayList<FluidStack>();
		if (input1 != null){
			fluids.add(toFluid(input1));
		}
		if (input2 != null){
			fluids.add(toFluid(input2));
		}
		if (input3 != null){
			fluids.add(toFluid(input3));
		}
		if (input4 != null){
			fluids.add(toFluid(input4));
		}
		MineTweakerAPI.apply(new Add(new FluidMixingRecipe(fluids.toArray(new FluidStack[]{null,null,null,null}), toFluid(output))));
	}

	private static class Add extends BaseListAddition<FluidMixingRecipe> {
		public Add(FluidMixingRecipe recipe) {
			super(Mixer.name, RecipeRegistry.mixingRecipes);
			
			this.recipes.add(recipe);
		}

		@Override
		protected String getRecipeInfo(FluidMixingRecipe arg0) {
			return LogHelper.getStackDescription(arg0.output);
		}
	}

	@ZenMethod
	public static void remove(ILiquidStack output) {
		List<FluidMixingRecipe> recipes = new ArrayList<FluidMixingRecipe>();
		
		for (int i = 0; i < RecipeRegistry.mixingRecipes.size(); i ++){
			if (RecipeRegistry.mixingRecipes.get(i).output.getFluid().getName().equals(toFluid(output).getFluid().getName())){
				recipes.add(RecipeRegistry.mixingRecipes.get(i));
			}
		}
		
		if (!recipes.isEmpty()) {
			for (int i = 0; i < recipes.size(); i ++){
				MineTweakerAPI.apply(new Remove(recipes.get(i)));
			}
		} 
		else {
			LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", Mixer.name,
					output.toString()));
		}

	}

	private static class Remove extends BaseListRemoval<FluidMixingRecipe> {
		
		public Remove(FluidMixingRecipe recipe) {
			super(Mixer.name, RecipeRegistry.mixingRecipes);
			this.recipes.remove(recipe);
		}

		@Override
		protected String getRecipeInfo(FluidMixingRecipe arg0) {
			// TODO Auto-generated method stub
			return LogHelper.getStackDescription(arg0.output);
		}
	}
}
