package modtweaker2.mods.exnihilo.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.utils.BaseUndoable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.CompostRegistry;
import exnihilo.registries.helpers.Color;
import exnihilo.registries.helpers.Compostable;

@ZenClass("mods.exnihilo.Composting")
public class Compost {
    //Adding a Ex Nihilo Composting recipe
    @ZenMethod
    public static void addRecipe(IItemStack input, double value, @Optional String hex) {
        hex = (hex == null || hex.equals("")) ? "35A82A" : hex;
        MineTweakerAPI.apply(new Add(toStack(input).getItem(), toStack(input).getItemDamage(), Math.min(1.0F, (float) value), new Color(hex)));
    }

    //Passes the list to the map list implementation, and adds the recipe
    private static class Add extends BaseUndoable 
    {
    	private Item item;
    	private int meta;
    	private float value;
    	private Color color;
    	
        public Add(Item item, int meta, float value, Color color) {
        	super("ExNihilo Compost", true);
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
		public void undo() {
			CompostRegistry.unregister(item, meta);
		}

		@Override
		public String getRecipeInfo() {
			return " compost from " + item.getUnlocalizedName();
		}

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Ex Nihilo Composting recipe
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove(toStack(input)));
    }

    //Removes a recipe, will always remove the key, so all should be good
    private static class Remove extends BaseUndoable 
    {
    	private ItemStack stack;
    	private int meta;
    	private float value;
    	private Color color;
    	
        public Remove(ItemStack stack) {
        	super("ExNihilo Compost", false);
        	this.stack = stack;
        }

		@Override
		public void apply() {
			Compostable comp = CompostRegistry.getItem(stack.getItem(), stack.getItemDamage());
			meta = comp.meta;
			value= comp.value;
			color = comp.color;
			CompostRegistry.unregister(stack.getItem(), stack.getItemDamage());
		}

		@Override
		public boolean canUndo() {
			return color != null;
		}
		
		@Override
		public void undo() {
			CompostRegistry.register(stack.getItem(), meta, value, color);
		}

		@Override
		public String getRecipeInfo() {
			return " compost from " + stack.getUnlocalizedName();
		}
    }
}
