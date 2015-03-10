package modtweaker2.mods.minestuck.handlers;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.util.BaseDescriptionAddition;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.minestuck.Alchemy")
public class Alchemy {
	
	@ZenMethod
	public void setCost(IItemStack iStack, boolean useMeta, String cost) {
		ItemStack stack = InputHelper.toStack(iStack);
		MineTweakerAPI.apply(new SetCost(stack.getItem(), useMeta ? stack.getItemDamage() : OreDictionary.WILDCARD_VALUE, cost));
	}
	
	@ZenMethod
	public void setOreDictCost(String name, String cost) {
		MineTweakerAPI.apply(new SetCost(name, OreDictionary.WILDCARD_VALUE, cost));
	}
	
	@ZenMethod
	public void removeCost(IItemStack iStack, boolean useMeta) {
		ItemStack stack = InputHelper.toStack(iStack);
		MineTweakerAPI.apply(new SetCost(stack.getItem(), useMeta ? stack.getItemDamage() : OreDictionary.WILDCARD_VALUE, null));
	}
	
	@ZenMethod
	public void removeOreDictCost(String name) {
		MineTweakerAPI.apply(new SetCost(name, OreDictionary.WILDCARD_VALUE, null));
	}
	
	private static class SetCost extends BaseDescriptionAddition {
		
		private final List<Object> items;
		private final GristSet cost;
		private GristSet costOld;
		
		public SetCost(Object item, int metadata, String cost) {
			super("Grist Cost");
			items = Arrays.asList(item, metadata);
			this.cost = getGrist(cost);
		}
		
		@Override
		public void apply() {
			costOld = GristRegistry.getAllConversions().remove(items);
			if(cost != null)
				GristRegistry.getAllConversions().put(items, cost);
		}
		
		@Override
		public void undo() {
			if(costOld == null)
				GristRegistry.getAllConversions().remove(items);
			else GristRegistry.getAllConversions().put(items, costOld);
		}
		
		private static GristSet getGrist(String str)
		{
			if(str == null)
				return null;
			
			GristSet grist = new GristSet();
			String[] values = str.split(",");
			for (String value : values) {
				if (value.startsWith(" ")) value = value.replaceFirst(" ", "");
				String[] gristAmount = value.split("\\s+");
				if (gristAmount.length == 2)
					grist.addGrist(GristType.getTypeFromString(gristAmount[0]), Integer.parseInt(gristAmount[1]));
			}
			
			return grist;
		}
	}
}