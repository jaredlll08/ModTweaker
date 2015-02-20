package modtweaker2.mods.mekanism.gas;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;

public class GasLogger implements ICommandFunction {
    public static final Comparator<Gas> COMPARATOR = new Compare();

    private static class Compare implements Comparator<Gas> {
        @Override
        public int compare(Gas o1, Gas o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    @Override
    public void execute(String[] arguments, IPlayer player) {
        List<Gas> gases = GasRegistry.getRegisteredGasses();
        System.out.println("Gases: " + GasRegistry.getRegisteredGasses().size());
        Collections.sort(gases, COMPARATOR);
        for (Gas gas : gases) {
            System.out.println("Gas " + gas.getName());
            MineTweakerAPI.logCommand("<" + gas.getName() + "> -- " + gas.getLocalizedName());
        }

        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}
