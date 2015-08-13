package modtweaker2.mods.exnihilo.handlers;

import static modtweaker2.helpers.InputHelper.isABlock;
import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.exnihilo.ExNihiloHelper;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.CrucibleRegistry;
import exnihilo.registries.helpers.Meltable;
import exnihilo.utils.ItemInfo;

@ZenClass("mods.exnihilo.Crucible")
public class Crucible {

    public static final String nameMelting = "ExNihilo Crucible (Melting)";
    public static final String nameHeatSource = "ExNihilo Crucible (Heat source)";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /************************************************ Crucible Melting ************************************************/
    //Adding a Ex Nihilo Crucible recipe
    @ZenMethod
    public static void addRecipe(IItemStack input, ILiquidStack fluid) {
        if(input == null || fluid == null) {
            LogHelper.logError(String.format("Required parameters missing for %s recipe.", nameMelting));
            return;
        }
        
        if(!isABlock(input)) {
            LogHelper.logError(String.format("Input item for %s recipe must be a block.", nameMelting));
            return;
        }
        
        Map<String, Meltable> recipes = new HashMap<String, Meltable>();
        Block theBlock = Block.getBlockFromItem(toStack(input).getItem());
        int theMeta = toStack(input).getItemDamage();
        
        recipes.put(
                theBlock + ":" + theMeta,
                new Meltable(theBlock, theMeta, 2000, toFluid(fluid).getFluid(), toFluid(fluid).amount, theBlock));

        MineTweakerAPI.apply(new AddRecipe(recipes));
    }

    //Passes the list to the map list implementation, and adds the recipe
    private static class AddRecipe extends BaseMapAddition<String, Meltable> {
        public AddRecipe(Map<String, Meltable> recipes) {
            super(nameMelting, CrucibleRegistry.entries, recipes);
        }
        
        @Override
        protected String getRecipeInfo(Entry<String, Meltable> recipe) {
            return LogHelper.getStackDescription(new ItemStack(recipe.getValue().block, 1, recipe.getValue().meta));
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Ex Nihilo Crucible recipe
    @ZenMethod
    public static void removeRecipe(IIngredient output) {
        Map<String, Meltable> recipes = new HashMap<String, Meltable>();
        
        for(Entry<String, Meltable> entry : CrucibleRegistry.entries.entrySet()) {
            FluidStack fluid = new FluidStack(entry.getValue().fluid, (int)entry.getValue().fluidVolume);
            if(matches(output, toILiquidStack(fluid))) {
                recipes.put(entry.getKey(), entry.getValue());
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveRecipe(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s. Command ignored!", Crucible.nameMelting, output.toString()));
        }
    }

    //Removes a recipe, will always remove the key, so all should be good
    private static class RemoveRecipe extends BaseMapRemoval<String, Meltable> {
        public RemoveRecipe(Map<String, Meltable> recipes) {
            super(nameMelting, CrucibleRegistry.entries, recipes);
        }

        @Override
        protected String getRecipeInfo(Entry<String, Meltable> recipe) {
            return LogHelper.getStackDescription(new ItemStack(recipe.getValue().block, 1, recipe.getValue().meta));
        }
    }

    /********************************************** Crucible Heat Sources **********************************************/
    //Adding a Ex Nihilo Crucible heat source
    @ZenMethod
    public static void addHeatSource(IItemStack input, double value) {
        if(input == null) {
            LogHelper.logError(String.format("Required parameters missing for %s recipe.", nameMelting));
            return;
        }
        
        if(!isABlock(input)) {
            LogHelper.logError(String.format("Input item for %s recipe must be a block.", nameMelting));
            return;
        }
        
        Map<ItemInfo, Float> recipes = new HashMap<ItemInfo, Float>();
        recipes.put(new ItemInfo(toStack(input)), (float)value);
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new AddHeatSource(recipes));
        }
    }

    //Passes the list to the base map implementation, and adds the recipe
    private static class AddHeatSource extends BaseMapAddition<ItemInfo, Float> 
	{
		public AddHeatSource(Map<ItemInfo, Float> recipes) {
		    super(Crucible.nameHeatSource, ExNihiloHelper.getHeatMap(), recipes);
		}

		@Override
		protected String getRecipeInfo(Entry<ItemInfo, Float> recipe) {
		    return LogHelper.getStackDescription(recipe.getKey().getStack());
		}
	}

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Ex Nihilo Crucible heat source
    @ZenMethod
    public static void removeHeatSource(IIngredient output) {
        Map<ItemInfo, Float> recipes = new HashMap<ItemInfo, Float>();
        
        for(Entry<ItemInfo, Float> entry : ExNihiloHelper.getHeatMap().entrySet()) {
            if(matches(output, toIItemStack(entry.getKey().getStack()))) {
                recipes.put(entry.getKey(), entry.getValue());
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveHeatSource(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipe found for %s. Command ignored!", Crucible.nameMelting, output.toString()));
        }
    }

    //Removes a recipe, will always remove the key, so all should be good
    private static class RemoveHeatSource extends BaseMapRemoval<ItemInfo, Float> 
	{
        public RemoveHeatSource(Map<ItemInfo, Float> recipes) {
        	super(Crucible.nameHeatSource, ExNihiloHelper.getHeatMap(), recipes);
        }

        @Override
        protected String getRecipeInfo(Entry<ItemInfo, Float> recipe) {
            return LogHelper.getStackDescription(recipe.getKey().getStack());
        }
	}
}
