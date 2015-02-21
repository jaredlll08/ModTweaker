package modtweaker2;

import java.util.ArrayList;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import org.apache.commons.lang3.tuple.MutablePair;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TweakEventHandler {

	public static ArrayList<MutablePair<EntityLivingBase, ArrayList<ItemStack>>> drops = new ArrayList<MutablePair<EntityLivingBase, ArrayList<ItemStack>>>();
	public static ArrayList<EntityLiving> cancleDrops = new ArrayList<EntityLiving>();

	@SubscribeEvent
	public void handleDrops(LivingDropsEvent event) {
		if (cancleDrops.contains(event.entityLiving)) {
			event.setCanceled(true);
		}
		for (MutablePair<EntityLivingBase, ArrayList<ItemStack>> ent : drops) {
			if (ent.left.isEntityEqual(event.entityLiving)) {
				for (ItemStack stack : ent.right) {
					event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, stack));
				}
			}
		}
	}

}
