package modtweaker.mods.bloodmagic;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.bloodmagic.handlers.AlchemyTable;
import modtweaker.mods.bloodmagic.handlers.Altar;
import modtweaker.mods.bloodmagic.handlers.SoulForge;

public class BloodMagic
{
    public BloodMagic()
    {
        MineTweakerAPI.registerClass(SoulForge.class);
        MineTweakerAPI.registerClass(AlchemyTable.class);
        MineTweakerAPI.registerClass(Altar.class);
    }
}
