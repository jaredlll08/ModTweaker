package modtweaker2.mods.fsp;

import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;

public class FSPHelper {
    public static CrucibleLiquid getLiquid(String name) {
        for (CrucibleLiquid l : SteamcraftRegistry.liquids) {
            if (l.name.equals(name)) {
                return l;
            }
        }

        return null;
    }
}
