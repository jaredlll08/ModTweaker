package modtweaker2;

import static modtweaker2.helpers.LogHelper.print;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import modtweaker2.utils.TweakerPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientEvents {
	public static int cooldown;
	public static boolean active;
public static String notice = EnumChatFormatting.DARK_RED + "This item's recipe has been changed by ModTweaker.";
	@SubscribeEvent
	public void onDrawTooltip(ItemTooltipEvent event) {

		IPlayer player = MineTweakerMC.getIPlayer(event.entityPlayer);
		if (player != null) {
			IItemStack hand = MineTweakerMC.getIItemStack(event.itemStack);
			if (hand != null) {

				if (active) {

					String print = hand.toString();
					event.toolTip.add(print);
					if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSprint)) {
						if (cooldown <= 0) {
							cooldown = 30;
							print(print + "  --  " + hand.getDisplayName());
						} else
							cooldown--;
					} else
						cooldown--;
				}

				for (ItemStack stack : TweakerPlugin.changed) {
					if (stack.isItemEqual(event.itemStack) && event.toolTip.contains(notice)) {
						event.toolTip.add(notice);
					}
				}
			}
		}
	}

}
