package modtweaker2.mods.minestuck.handlers;

import java.util.List;

import net.minecraft.item.ItemStack;
import minetweaker.api.item.IItemStack;
import modtweaker2.util.BaseDescriptionAddition;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.minestuck.Combinations")
public class Combinations {
	
	@ZenMethod
	public void addRecipe(IItemStack input1, IItemStack input2, boolean mode, IItemStack output) {
		
	}
	
	@ZenMethod
	public void removeRecipe(IItemStack input1, IItemStack input2, boolean mode) {
		
	}
	
	private static class setRecipe extends BaseDescriptionAddition {
		
//		private final List<Object> inputs;
//		private final ItemStack output;
		private ItemStack outputOld;
		
		public setRecipe() {
			super("Combination Recipe");
		}
		
		@Override
		public void apply() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void undo() {
			// TODO Auto-generated method stub
			
		}
		
	}
}