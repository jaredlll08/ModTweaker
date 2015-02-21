package modtweaker2.mods.chisel;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.chisel.handlers.Groups;

public class Chisel {
    public Chisel() {
        MineTweakerAPI.registerClass(Groups.class);
    }

}
