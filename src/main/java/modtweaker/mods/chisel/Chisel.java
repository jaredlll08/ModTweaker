package modtweaker.mods.chisel;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.chisel.handlers.Groups;

public class Chisel {
    public Chisel() {
        MineTweakerAPI.registerClass(Groups.class);
    }

}
