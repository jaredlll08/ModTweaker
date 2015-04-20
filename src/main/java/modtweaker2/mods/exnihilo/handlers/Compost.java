package modtweaker2.mods.exnihilo.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.CompostRegistry;
import exnihilo.registries.helpers.Color;

@ZenClass("mods.exnihilo.Composting")
public class Compost {
    //Adding a Ex Nihilo Composting recipe
    @ZenMethod
    public static void addRecipe(IItemStack input, double value, @Optional String hex) {
        hex = (hex == null || hex.equals("")) ? "35A82A" : hex;
        MineTweakerAPI.apply(new Add(toStack(input).getItem(), toStack(input).getItemDamage(), Math.min(1.0F, (float) value), new Color(hex)));
    }

    //Passes the list to the map list implementation, and adds the recipe
    private static class Add implements IUndoableAction 
    {
    	private Item item;
    	private int meta;
    	private float value;
    	private Color color;
    	
        public Add(Item item, int meta, float value, Color color) {
        	this.item = item;
        	this.meta = meta;
        	this.value = value;
        	this.color = color;
        }

		@Override
		public void apply() {
			CompostRegistry.register(item, meta, value, color);
		}

		@Override
		public boolean canUndo() {
			return false;
		}

		@Override
		public String describe() {
			return "Adding Composting Recipe using " + item.getUnlocalizedName();
		}

		@Override
		public String describeUndo() {
			return null;
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}

		@Override
		public void undo() {
			
		}

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Ex Nihilo Composting recipe
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input)));
    }

    //Removes a recipe, will always remove the key, so all should be good
    private static class Remove implements IUndoableAction 
    {
    	private ItemStack stack;
    	
        public Remove(ItemStack stack) {
        	this.stack = stack;
        }

		@Override
		public void apply() {
			CompostRegistry.unregister(stack.getItem(), stack.getItemDamage());
		}

		@Override
		public boolean canUndo() {
			return false;
		}

		@Override
		public String describe() {
			return "Removing Composting Recipe using " + stack.getUnlocalizedName();
		}

		@Override
		public String describeUndo() {
			return null;
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}

		@Override
		public void undo() {
		}
    }
}
