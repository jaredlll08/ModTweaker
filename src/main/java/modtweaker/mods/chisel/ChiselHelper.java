package modtweaker.mods.chisel;

import static modtweaker.helpers.InputHelper.toStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import minetweaker.api.item.IItemStack;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.cricketcraft.chisel.carving.Carving;

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
		return Carving.chisel.getVariation(Block.getBlockFromItem(toStack(stack).getItem()), stack.getDamage());
	}
}
