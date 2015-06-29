package modtweaker2.mods.tconstruct.handlers;

import static modtweaker2.helpers.InputHelper.isABlock;
import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toFluids;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mantle.utils.ItemMetaWrapper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.tconstruct.TConstructHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import modtweaker2.utils.BaseUndoable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.crafting.AlloyMix;

@ZenClass("mods.tconstruct.Smeltery")
public class Smeltery {
    
    public static final String nameFuel = "TConstruct Smeltery - Fuel";

	/********************************************** TConstruct Alloy Recipes **********************************************/

	// Adding a TConstruct Alloy recipe
	@ZenMethod
	public static void addAlloy(ILiquidStack output, ILiquidStack[] input) {
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
	public static void removeAlloy(ILiquidStack output) {
		MineTweakerAPI.apply(new RemoveAlloy((toFluid(output))));
	}

	// Removes a recipe, apply is never the same for anything, so will always
	// need to override it
	private static class RemoveAlloy extends BaseListRemoval<AlloyMix> {
		public RemoveAlloy(FluidStack output) {
			super("Smeltery - Alloy", TConstructHelper.alloys);
			
            for (AlloyMix r : TConstructHelper.alloys) {
                if (r.result != null && output != null && r.result.getFluid() == output.getFluid()) {
                    recipes.add(r);
                }
            }
		}

        @Override
        protected String getRecipeInfo(AlloyMix recipe) {
            return InputHelper.getStackDescription(recipe.result);
        }
	}

	/********************************************** TConstruct Melting Recipes **********************************************/

	// Adding a TConstruct Melting recipe
	@ZenMethod
	public static void addMelting(IItemStack input, ILiquidStack output, int temp, @Optional IItemStack block) {
		if (block == null)
			block = input;
		if (isABlock(block)) {
			Block theBlock = Block.getBlockFromItem(toStack(block).getItem());
			int theMeta = toStack(block).getItemDamage();
			MineTweakerAPI.apply(new AddMelting(toStack(input), theBlock, theMeta, temp, toFluid(output)));
		}
	}

	@ZenMethod
	public static void addMelting(IOreDictEntry input, ILiquidStack output, int temp, @Optional IItemStack block) {
		for (ItemStack stack : OreDictionary.getOres(input.getName())) {
			addMelting(MineTweakerMC.getIItemStack(stack), output, temp, block);
		}
	}

	// Takes all the variables and saves them in place
	private static class AddMelting extends BaseUndoable {
		private final ItemStack input;
		private final Block block;
		private final int meta;
		private final int temp;
		private final FluidStack output;

		public AddMelting(ItemStack input, Block block, int meta, int temp, FluidStack output) {
			super("Smeltery - Melting");
			this.input = input;
			this.block = block;
			this.meta = meta;
			this.temp = temp;
			this.output = output;
		}

		// Adds the Melting recipe
		@Override
		public void apply() {
			tconstruct.library.crafting.Smeltery.instance.addMelting(input, block, meta, temp, output);
		}

		// Removes the Melting recipe from the hashmaps
		@Override
		public void undo() {
			ItemMetaWrapper in = new ItemMetaWrapper(input);
			TConstructHelper.smeltingList.remove(in);
			TConstructHelper.temperatureList.remove(in);
			TConstructHelper.renderIndex.remove(in);
		}
		
		@Override
		public String getRecipeInfo() {
			return input.getDisplayName();
		}
		
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Removing a TConstruct Melting recipe
	@ZenMethod
	public static void removeMelting(IItemStack input) {
		MineTweakerAPI.apply(new RemoveMelting((toStack(input))));
	}

	@ZenMethod
	public static void removeMelting(IOreDictEntry input) {
		for (ItemStack stack : OreDictionary.getOres(input.getName())) {
			removeMelting(MineTweakerMC.getIItemStack(stack));
		}
	}

	// Removes a recipe, apply is never the same for anything, so will always
	// need to override it
	private static class RemoveMelting extends BaseUndoable {
		private final ItemStack input;
		private FluidStack fluid;
		private Integer temp;
		private ItemStack renderer;

		public RemoveMelting(ItemStack input) {
			super("Smeltery - Melting");
			this.input = input;
		}

		// Gets the current values, and saves, them removes them from the
		// hashmaps
		@Override
		public void apply() {
			ItemMetaWrapper in = new ItemMetaWrapper(input);
			fluid = TConstructHelper.smeltingList.get(in);
			temp = TConstructHelper.temperatureList.get(in);
			renderer = TConstructHelper.renderIndex.get(in);
			TConstructHelper.smeltingList.remove(in);
			TConstructHelper.temperatureList.remove(in);
			TConstructHelper.renderIndex.remove(in);
		}

		// Reads the Melting recipe
		@Override
		public void undo() {
			// tconstruct.library.crafting.Smeltery.instance.addMelting(input,
			// Block.getBlockFromItem(renderer.getItem()),
			// renderer.getItemDamage(), temp, fluid);
		}

		@Override
		public String getRecipeInfo() {
			return input.getDisplayName();
		}
	}
	
	/********************************************** TConstruct Fuel Recipes **********************************************/
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@ZenMethod
    public static void removFuel(IIngredient input) {
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
