package modtweaker;

import modtweaker.mods.randomthings.gui.imbuing.ContainerImbuingStation;
import modtweaker.mods.randomthings.gui.imbuing.GuiImbuingStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by Jared.
 */
public class GuiHandlerMods implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 3:
                return new ContainerImbuingStation(player.inventory);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 3:
                return new GuiImbuingStation(new ContainerImbuingStation(player.inventory));
            default:
                return null;
        }
    }
}
