package modtweaker.mods.mekanism;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.mekanism.gas.GasBracketHandler;
import modtweaker.mods.mekanism.handlers.ChemicalCrystallizer;
import modtweaker.mods.mekanism.handlers.ChemicalDissolution;
import modtweaker.mods.mekanism.handlers.ChemicalInfuser;
import modtweaker.mods.mekanism.handlers.ChemicalInjection;
import modtweaker.mods.mekanism.handlers.ChemicalOxidizer;
import modtweaker.mods.mekanism.handlers.ChemicalWasher;
import modtweaker.mods.mekanism.handlers.Combiner;
import modtweaker.mods.mekanism.handlers.Compressor;
import modtweaker.mods.mekanism.handlers.Crusher;
import modtweaker.mods.mekanism.handlers.Enrichment;
import modtweaker.mods.mekanism.handlers.Infuser;
import modtweaker.mods.mekanism.handlers.Purification;
import modtweaker.mods.mekanism.handlers.Reaction;
import modtweaker.mods.mekanism.handlers.Sawmill;
import modtweaker.mods.mekanism.handlers.Separator;
import modtweaker.util.TweakerPlugin;

public class Mekanism extends TweakerPlugin {
    public Mekanism() {
        MineTweakerAPI.registerBracketHandler(new GasBracketHandler());
        MineTweakerAPI.registerClass(Combiner.class);
        MineTweakerAPI.registerClass(Compressor.class);
        MineTweakerAPI.registerClass(Crusher.class);
        MineTweakerAPI.registerClass(Enrichment.class);
        MineTweakerAPI.registerClass(Infuser.class);
        MineTweakerAPI.registerClass(Purification.class);
        MineTweakerAPI.registerClass(Reaction.class);
        MineTweakerAPI.registerClass(Sawmill.class);
        MineTweakerAPI.registerClass(Separator.class);
        MineTweakerAPI.registerClass(ChemicalCrystallizer.class);
        MineTweakerAPI.registerClass(ChemicalDissolution.class);
        MineTweakerAPI.registerClass(ChemicalInfuser.class);
        MineTweakerAPI.registerClass(ChemicalInjection.class);
        MineTweakerAPI.registerClass(ChemicalOxidizer.class);
        MineTweakerAPI.registerClass(ChemicalWasher.class);
    }
}
