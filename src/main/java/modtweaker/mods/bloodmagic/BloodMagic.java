package modtweaker.mods.bloodmagic;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.bloodmagic.handlers.SoulForge;

public class BloodMagic
{
    public BloodMagic()
    {
        MineTweakerAPI.registerClass(SoulForge.class);
    }
}
