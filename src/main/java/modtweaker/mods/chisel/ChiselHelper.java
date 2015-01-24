package modtweaker.mods.chisel;

import static modtweaker.helpers.InputHelper.toStack;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import minetweaker.api.item.IItemStack;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;
import com.google.common.collect.Lists;

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

	public static ICarvingGroup makeGroup(String name)
	{
		return new CarvingGroup(name);
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

	static class CarvingGroup implements ICarvingGroup
	{
		private String name;
		private String sound;
		private String oreName;

		private List<ICarvingVariation> variations = Lists.newArrayList();

		public CarvingGroup(String name) {
			this.name = name;
		}

		public List<ICarvingVariation> getVariations() {
			return Lists.newArrayList(variations);
		}

		@Override
		public void addVariation(ICarvingVariation variation) {
			variations.add(variation);
			Collections.sort(variations, new Comparator<ICarvingVariation>() {

				@Override
				public int compare(ICarvingVariation o1, ICarvingVariation o2) {
					return CarvingUtils.compare(o1, o2);
				}
			});
		}
		
		@Override
		public boolean removeVariation(ICarvingVariation variation) {
			ICarvingVariation toRemove = null;
			for (ICarvingVariation v : variations) {
				if (v.getBlock() == variation.getBlock() && v.getBlockMeta() == variation.getBlockMeta()) {
					toRemove = v;
				}
			}
			return toRemove == null ? false : variations.remove(toRemove);
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getSound() {
			return sound;
		}

		@Override
		public void setSound(String sound) {
			this.sound = sound;
		}

		@Override
		public String getOreName() {
			return oreName;
		}

		@Override
		public void setOreName(String oreName) {
			this.oreName = oreName;
		}
	}
}
