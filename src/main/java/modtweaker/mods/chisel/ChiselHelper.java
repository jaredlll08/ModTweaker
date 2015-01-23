package modtweaker.mods.chisel;

import static modtweaker.helpers.InputHelper.toStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import minetweaker.api.item.IItemStack;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;

public class ChiselHelper {

	public static ICarvingGroup getGroup(String name)
	{
		return CarvingUtils.getChiselRegistry().getGroup(name);
	}
	
	public static ICarvingGroup getGroup(IItemStack stack)
	{
		return CarvingUtils.getChiselRegistry().getGroup(Block.getBlockFromItem(toStack(stack).getItem()), stack.getDamage());
	}
	
	public static ICarvingVariation getVariation(IItemStack stack)
	{
		ICarvingGroup g = getGroup(stack);
		if (g != null) {
			for (ICarvingVariation v : g.getVariations()) {
				if (v.getBlock() == Block.getBlockFromItem(toStack(stack).getItem()) && v.getBlockMeta() == stack.getDamage()) {
					return v;
				}
			}
		}
		return null;
	}

	public static ICarvingVariation makeVariation(IItemStack stack)
	{
		return new CarvingVariation(Block.getBlockFromItem(toStack(stack).getItem()), stack.getDamage());
	}

	public static boolean groupContainsVariation(ICarvingGroup group, ICarvingVariation variation)
	{
		for(ICarvingVariation otherVariation : group.getVariations())
		{
			if(otherVariation.getBlock()==variation.getBlock() && otherVariation.getBlockMeta()==variation.getBlockMeta())
				return true;
		}
		return false;
	}
	
	static class CarvingVariation implements ICarvingVariation
	{
		Block block;
		int meta;
		
		public CarvingVariation(Block block, int meta)
		{
			this.block=block;
			this.meta=meta;
		}
		
		@Override
		public Block getBlock() {
			return block;
		}

		@Override
		public int getBlockMeta() {
			return meta;
		}

		@Override
		public int getItemMeta() {
			return meta;
		}

		@Override
		public int getOrder() {
			return 99;
		}
	}
}
