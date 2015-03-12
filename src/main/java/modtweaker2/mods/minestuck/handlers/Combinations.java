package modtweaker2.mods.minestuck.handlers;

import java.util.Arrays;
import java.util.List;

import com.mraof.minestuck.util.CombinationRegistry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.util.BaseDescriptionAddition;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.minestuck.Combinations")
public class Combinations {
	
	@ZenMethod
	public void addRecipe(IItemStack input1, boolean useDamage1, IItemStack input2, boolean useDamage2, boolean mode, IItemStack output) {
		ItemStack stack1 = InputHelper.toStack(input1);
		ItemStack stack2 = InputHelper.toStack(input2);
		MineTweakerAPI.apply(new SetRecipe(stack1.getItem(), useDamage1 ? stack1.getItemDamage() : OreDictionary.WILDCARD_VALUE, stack2.getItem(), useDamage2 ? stack2.getItemDamage() : OreDictionary.WILDCARD_VALUE, mode, InputHelper.toStack(output)));
	}
	
	@ZenMethod
	public void addOreDictRecipe(IItemStack input1, boolean useDamage, String input2, boolean mode, IItemStack output) {
		ItemStack stack = InputHelper.toStack(input1);
		MineTweakerAPI.apply(new SetRecipe(stack.getItem(), useDamage ? stack.getItemDamage() : OreDictionary.WILDCARD_VALUE, input2, OreDictionary.WILDCARD_VALUE, mode, InputHelper.toStack(output)));
	}
	
	@ZenMethod
	public void addFullOreDictRecipe(String input1, String input2, boolean mode, IItemStack output) {
		MineTweakerAPI.apply(new SetRecipe(input1, OreDictionary.WILDCARD_VALUE, input2, OreDictionary.WILDCARD_VALUE, mode, InputHelper.toStack(output)));
	}
	
	@ZenMethod
	public void removeRecipe(IItemStack input1, boolean useDamage1, IItemStack input2, boolean useDamage2, boolean mode) {
		ItemStack stack1 = InputHelper.toStack(input1);
		ItemStack stack2 = InputHelper.toStack(input2);
		MineTweakerAPI.apply(new SetRecipe(stack1.getItem(), useDamage1 ? stack1.getItemDamage() : OreDictionary.WILDCARD_VALUE, stack2.getItem(), useDamage2 ? stack2.getItemDamage() : OreDictionary.WILDCARD_VALUE, mode, null));
	}
	
	@ZenMethod
	public void removeOreDictRecipe(IItemStack input1, boolean useDamage, String input2, boolean mode) {
		ItemStack stack = InputHelper.toStack(input1);
		MineTweakerAPI.apply(new SetRecipe(stack.getItem(), useDamage ? stack.getItemDamage() : OreDictionary.WILDCARD_VALUE, input2, OreDictionary.WILDCARD_VALUE, mode, null));
	}
	
	@ZenMethod
	public void removeFullOreDictRecipe(String input1, String input2, boolean mode) {
		MineTweakerAPI.apply(new SetRecipe(input1, OreDictionary.WILDCARD_VALUE, input2, OreDictionary.WILDCARD_VALUE, mode, null));
	}
	
	private static class SetRecipe extends BaseDescriptionAddition {
		
		private final List<Object> inputs;
		private final ItemStack output;
		private ItemStack outputOld;
		
		public SetRecipe(Object input1, int meta1, Object input2, int meta2, boolean mode, ItemStack output) {
			super("Combination Recipe");
			int index = input1.hashCode() - input2.hashCode();
			if(index == 0)
				index = meta1 - meta2;
			if(index > 0)
				inputs = Arrays.asList(input1, meta1, input2, meta2, mode);
			else inputs = Arrays.asList(input2, meta2, input1, meta1, mode);
			this.output = output;
		}
		
		@Override
		public void apply() {
			outputOld = CombinationRegistry.getAllConversions().remove(inputs);
			if(output != null)
				CombinationRegistry.getAllConversions().put(inputs, output);
		}
		
		@Override
		public void undo() {
			if(outputOld == null)
				CombinationRegistry.getAllConversions().remove(inputs);
			else CombinationRegistry.getAllConversions().put(inputs, outputOld);
		}
		
	}
}