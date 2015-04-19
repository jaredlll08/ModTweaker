package modtweaker2.mods.tfcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import minetweaker.MineTweakerAPI;
import minetweaker.OneWayAction;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import com.bioxx.tfc.api.Crafting.LoomManager;
import com.bioxx.tfc.api.Crafting.LoomRecipe;

@ZenClass("mods.tfcraft.Loom")
public class Loom {

	//rl==resource location
	@ZenMethod
	public static void add(IItemStack input, IItemStack output, String rl){
		MineTweakerAPI.apply(new Add(toStack(input), toStack(output), new ResourceLocation(rl)));
	}
	
	private static class Add extends OneWayAction{

		ItemStack input, output;
		ResourceLocation location;
		
		public Add(ItemStack input2, ItemStack output2, ResourceLocation location){
			this.input = input2;
			this.output = output2;
			this.location = location;
			
			this.apply();
		}
		
		@Override
		public void apply() {
			LoomManager.getInstance().addRecipe(new LoomRecipe(input, output), location);
		}

		@Override
		public String describe() {
			return "Add loom recipe for " + output.toString();
		}

		@Override
		public Object getOverrideKey() {
			return null;
		}
		
	}
}
