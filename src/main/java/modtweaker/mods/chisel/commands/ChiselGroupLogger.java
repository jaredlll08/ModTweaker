package modtweaker.mods.chisel.commands;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import team.chisel.api.carving.CarvingUtils;

import java.util.List;


public class ChiselGroupLogger implements ICommandFunction {

    @Override
    public void execute(String[] arguments, IPlayer player) {
        List<String> keys = CarvingUtils.getChiselRegistry().getSortedGroupNames();
        System.out.println("Chisel Groups: " + keys.size());
        for (String key : keys) {
            System.out.println("Chisel Group " + key);
            MineTweakerAPI.logCommand(key);
        }

        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}
