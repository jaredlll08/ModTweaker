package modtweaker2.mods.exnihilo.handlers;

import static modtweaker2.helpers.InputHelper.isABlock;
import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toStack;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.CrucibleRegistry;
import exnihilo.registries.HeatRegistry;
import exnihilo.registries.helpers.Meltable;

@ZenClass("mods.exnihilo.Crucible")
public class Crucible {

    /************************************************ Crucible Melting ************************************************/
    //Adding a Ex Nihilo Crucible recipe
    @ZenMethod
    public static void addRecipe(IItemStack input, ILiquidStack fluid) {
        if (isABlock(input)) {
            Block theBlock = Block.getBlockFromItem(toStack(input).getItem());
            int theMeta = toStack(input).getItemDamage();
            MineTweakerAPI.apply(new AddRecipe(new Meltable(theBlock, theMeta, 2000, toFluid(fluid).getFluid(), toFluid(fluid).amount, theBlock)));
        }
    }

    //Passes the list to the map list implementation, and adds the recipe
    private static class AddRecipe extends BaseMapAddition {
        public AddRecipe(Meltable recipe) {
            super("ExNihilo Crucible", CrucibleRegistry.entries, recipe.block + ":" + recipe.meta, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return new ItemStack(((Meltable) recipe).block, 1, ((Meltable) recipe).meta).getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Ex Nihilo Crucible recipe
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        if (isABlock(output)) {
            MineTweakerAPI.apply(new RemoveRecipe(toStack(output)));
        }
    }

    //Removes a recipe, will always remove the key, so all should be good
    private static class RemoveRecipe extends BaseMapRemoval {
        public RemoveRecipe(ItemStack stack) {
            super("ExNihilo Crucible", CrucibleRegistry.entries, Block.getBlockFromItem(stack.getItem()) + ":" + stack.getItemDamage(), stack);
        }

        @Override
        public String getRecipeInfo() {
            return ((ItemStack) stack).getDisplayName();
        }
    }

    /********************************************** Crucible Heat Sources **********************************************/
    //Adding a Ex Nihilo Crucible heat source
    @ZenMethod
    public static void addHeatSource(IItemStack input, double value) {
        if (isABlock(input)) {
            Block theBlock = Block.getBlockFromItem(toStack(input).getItem());
            int theMeta = toStack(input).getItemDamage();
            MineTweakerAPI.apply(new AddHeatSource(theBlock, theMeta, (float) value));
        }
    }

    //Passes the list to the base map implementation, and adds the recipe
    private static class AddHeatSource implements IUndoableAction 
	{
		Block source;
		int sourceMeta;
		float value;

		public AddHeatSource(Block source, int sourceMeta, float value) {
			this.source = source;
			this.sourceMeta = sourceMeta;
			this.value = value;
		}

		public void apply() {
			HeatRegistry.register(source, sourceMeta, value);
		}

		public boolean canUndo() {
			return false;
		}

		public String describe() {
			return "Adding ExNihilo Heat source of " + source.getLocalizedName();
		}

		public String describeUndo() {
			return null;
		}

		public Object getOverrideKey() {
			return null;
		}

		public void undo() {
		}
	}


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Ex Nihilo Crucible heat source
    @ZenMethod
    public static void removeHeatSource(IItemStack output) {
        if (isABlock(output)) {
        	Block block = Block.getBlockFromItem(toStack(output).getItem());
            MineTweakerAPI.apply(new RemoveHeatSource(block));
        }
    }

    //Removes a recipe, will always remove the key, so all should be good
    private static class RemoveHeatSource implements IUndoableAction 
	{
    	Block block;

        public RemoveHeatSource(Block block) {
        	this.block = block;
        }

		public void apply() {
			HeatRegistry.unregister(block);
		}

		public boolean canUndo() {
			return false;
		}

		public String describe() {
			return "Removing ExNihilo Heat source of " + block.getLocalizedName();
		}

		public String describeUndo() {
			return null;
		}

		public Object getOverrideKey() {
			return null;
		}

		public void undo() {
		}
	}
}
