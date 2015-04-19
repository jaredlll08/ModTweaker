package modtweaker2.mods.tfcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import minetweaker.MineTweakerAPI;
import minetweaker.OneWayAction;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRegistry;

@ZenClass("mods.tfcraft.Heat")
public class Heat {

	/**
	 * To add custom recipe in firepit, use this.
	 * @param input The input itemstack
	 * @param sh "Specific Heat", which means the multiplier of increasing speed of temp (by default the speed is 1C/gametick)
	 * @param mt "melt temperature"
	 * @param output The output itemstack when the item reach the certain temperature
	 * @see com.bioxx.tfc.Core.ItemHeat
	 */
	@ZenMethod
	public static void add(IItemStack input, double sh, double mt, IItemStack output){
		MineTweakerAPI.apply(new AddHeatIndex(toStack(input), sh, mt, toStack(output)));
	}
	
	private static class AddHeatIndex extends OneWayAction{

		ItemStack in, out;
		double sh, mt;
		
		public AddHeatIndex(ItemStack in, double sh, double mt, ItemStack out){
			this.in = in;
			this.sh = sh;
			this.mt = mt;
			this.out = out;
			
			this.apply();
		}
		
		@Override
		public void apply() {
			HeatRegistry.getInstance().addIndex(new HeatIndex(in, sh, mt, out));
		}

		@Override
		public String describe() {
			return "Add custom heat index for firepit";
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}

	}
	
}
