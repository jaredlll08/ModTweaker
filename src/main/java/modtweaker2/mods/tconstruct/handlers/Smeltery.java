package modtweaker2.mods.tconstruct.handlers;

import static modtweaker2.helpers.InputHelper.isABlock;
import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toFluids;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mantle.utils.ItemMetaWrapper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.tconstruct.TConstructHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.crafting.AlloyMix;

@ZenClass("mods.tconstruct.Smeltery")
public class Smeltery {
    
    public static final String nameFuel = "TConstruct Smeltery - Fuel";
    public static final String nameMelting = "TConstruct Smeltery - Melting";
    public static final String nameAlloy = "TConstruct Smeltery - Alloy";

	/********************************************** TConstruct Alloy Recipes **********************************************/

	// Adding a TConstruct Alloy recipe
	@ZenMethod
	public static void addAlloy(ILiquidStack output, ILiquidStack[] input) {
        if(input == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", nameAlloy));
            return;
        }
        
		MineTweakerAPI.apply(new AddAlloy(new AlloyMix(toFluid(output), new ArrayList<FluidStack>(Arrays.asList(toFluids(input))))));
	}

	// Passes the list to the base list implementation, and adds the recipe
	private static class AddAlloy extends BaseListAddition<AlloyMix> {
		public AddAlloy(AlloyMix recipe) {
			super("TConstruct Smeltery - Alloy", TConstructHelper.alloys);
			this.recipes.add(recipe);
		}

		@Override
		protected String getRecipeInfo(AlloyMix recipe) {
		    return InputHelper.getStackDescription(recipe.result);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Removing a TConstruct Alloy recipe
	@ZenMethod
	public static void removeAlloy(IIngredient output) {
	    List<AlloyMix> recipes = new LinkedList<AlloyMix>();
	    
	    for(AlloyMix r : TConstructHelper.alloys) {
	        if(r != null && matches(output, toILiquidStack(r.result))) {
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
	private static class RemoveAlloy extends BaseListRemoval<AlloyMix> {
		public RemoveAlloy(List<AlloyMix> recipes) {
			super(nameAlloy, TConstructHelper.alloys, recipes);
		}

        @Override
        protected String getRecipeInfo(AlloyMix recipe) {
            return InputHelper.getStackDescription(recipe.result);
        }
	}

	/********************************************** TConstruct Melting Recipes **********************************************/

	// Adding a TConstruct Melting recipe
	@ZenMethod
	public static void addMelting(IIngredient input, ILiquidStack output, int temp, @Optional IItemStack block) {
	    
        if(input == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", nameMelting));
            return;
        }
	    
	    List<MeltingRecipe> recipes = new LinkedList<MeltingRecipe>();

	    for(IItemStack in : input.getItems()) {
	        if(block == null && !isABlock(toStack(in))) {
	            LogHelper.logWarning(String.format("Item %s is not a block and no block renderer is provided for %s recipe. Input ignored!", in.toString(), nameMelting));
	        } else {
	            recipes.add(new MeltingRecipe(toStack(in), toFluid(output), temp, block == null ? toStack(in) : toStack(block)));
	        }
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
                tconstruct.library.crafting.Smeltery.addMelting(recipe.input, recipe.getRendererBlock(), recipe.getRendererMeta(), recipe.temperature, recipe.fluid);
                successful.add(recipe);
            }
		}

		@Override
		public void undo() {
		    for(MeltingRecipe recipe : successful) {
		        TConstructHelper.smeltingList.remove(recipe.meta);
		        TConstructHelper.temperatureList.remove(recipe.meta);
		        TConstructHelper.renderIndex.remove(recipe.meta);
		    }
		}
		
        @Override
        public String getRecipeInfo(MeltingRecipe recipe) {
            return InputHelper.getStackDescription(recipe.input);
        }
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Removing a TConstruct Melting recipe
	@ZenMethod
	public static void removeMelting(IIngredient input) {
	    List<MeltingRecipe> recipes = new LinkedList<MeltingRecipe>();
	    
	    for(ItemMetaWrapper meta : TConstructHelper.smeltingList.keySet()) {
	        ItemStack in = new ItemStack(meta.item, 1, meta.meta);
	        if(matches(input, toIItemStack(in))) {
	            recipes.add(new MeltingRecipe(
	                    in,
	                    TConstructHelper.smeltingList.get(meta),
	                    TConstructHelper.temperatureList.get(meta),
	                    TConstructHelper.renderIndex.get(meta)));
	        }
	    }
	    
	    if(!recipes.isEmpty()) {
	        MineTweakerAPI.apply(new RemoveMelting(recipes));
	    } else {
	        LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", nameMelting, input.toString()));
	    }
	}

	private static class RemoveMelting extends BaseListRemoval<MeltingRecipe> {
		
	    public RemoveMelting(List<MeltingRecipe> recipes) {
			super(nameMelting, null, recipes);
		}

		@Override
		public void apply() {
		    for(MeltingRecipe recipe : recipes) {
		        TConstructHelper.smeltingList.remove(recipe.meta);
		        TConstructHelper.temperatureList.remove(recipe.meta);
		        TConstructHelper.renderIndex.remove(recipe.meta);
		        
		        successful.add(recipe);
		    }
		}

        @Override
		public void undo() {
		    for(MeltingRecipe recipe : successful) {
		        tconstruct.library.crafting.Smeltery.addMelting(recipe.input, recipe.getRendererBlock(), recipe.getRendererMeta(), recipe.temperature, recipe.fluid);
		    }
		}

		@Override
		public String getRecipeInfo(MeltingRecipe recipe) {
			return InputHelper.getStackDescription(recipe.input);
		}
	}
	
	protected static class MeltingRecipe {
	    public final ItemMetaWrapper meta;
	    public final ItemStack input;
	    public final FluidStack fluid;
	    public final Integer temperature;
	    public final ItemStack renderer;
	    
	    protected MeltingRecipe(ItemStack input, FluidStack fluid, int temperature, ItemStack renderer) {
	        this.input = input;
	        this.fluid = fluid;
	        this.temperature = temperature;
	        this.renderer = renderer;
	        this.meta = new ItemMetaWrapper(input);
	    }
	    
	    public Block getRendererBlock() {
	        return Block.getBlockFromItem(renderer.getItem());
	    }
	    
	    public int getRendererMeta() {
	        return renderer.getItemDamage();
	    }
	}
	
	/********************************************** TConstruct Fuel Recipes **********************************************/
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@ZenMethod
    public static void removeFuel(IIngredient input) {
	    Map<Fluid, Integer[]> recipes = new HashMap<Fluid, Integer[]>();
	    
	    for(Entry<Fluid, Integer[]> fuel : TConstructHelper.fuelList.entrySet()) {
	        if(fuel != null && fuel.getKey() != null && matches(input, toILiquidStack(new FluidStack(fuel.getKey(), 1)))) {
	            recipes.put(fuel.getKey(), fuel.getValue());
	        }
	    }
	    
	    if(!recipes.isEmpty()) {
	        MineTweakerAPI.apply(new RemoveFuel(recipes));
	    } else {
	        LogHelper.logWarning(String.format("No %s Recipe for %s found. Command ignored!", Smeltery.nameFuel, input.toString()));
	    }
    }
	
	public static class RemoveFuel extends BaseMapRemoval<Fluid, Integer[]> {
	    public RemoveFuel(Map<Fluid, Integer[]> recipes) {
	        super(Smeltery.nameFuel, TConstructHelper.fuelList, recipes);
        }
	    
	    @Override
	    public String getRecipeInfo(Entry<Fluid, Integer[]> recipe) {
	        return InputHelper.getStackDescription(new FluidStack(recipe.getKey(), 1));
	    }
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@ZenMethod
	public static void addFuel(ILiquidStack liquid, int power, int duration) {
        if(liquid == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", nameFuel));
            return;
        }
        
	    Map<Fluid, Integer[]> recipes = new HashMap<Fluid, Integer[]>();
	    
	    recipes.put(toFluid(liquid).getFluid(), new Integer[] {power, duration});
	    
	    MineTweakerAPI.apply(new AddFuel(recipes));
	}
	
	public static class AddFuel extends BaseMapAddition<Fluid, Integer[]> {
	    public AddFuel(Map<Fluid, Integer[]> recipes) {
	        super(Smeltery.nameFuel, TConstructHelper.fuelList, recipes);
	    }
	    
        @Override
        public String getRecipeInfo(Entry<Fluid, Integer[]> recipe) {
            return InputHelper.getStackDescription(new FluidStack(recipe.getKey(), 1));
        }
	}
}
