package modtweaker2.mods.mekanism;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.mekanism.gas.GasBracketHandler;
import modtweaker2.mods.mekanism.handlers.ChemicalCrystallizer;
import modtweaker2.mods.mekanism.handlers.ChemicalDissolution;
import modtweaker2.mods.mekanism.handlers.ChemicalInfuser;
import modtweaker2.mods.mekanism.handlers.ChemicalInjection;
import modtweaker2.mods.mekanism.handlers.ChemicalOxidizer;
import modtweaker2.mods.mekanism.handlers.ChemicalWasher;
import modtweaker2.mods.mekanism.handlers.Combiner;
import modtweaker2.mods.mekanism.handlers.Compressor;
import modtweaker2.mods.mekanism.handlers.Crusher;
import modtweaker2.mods.mekanism.handlers.EnergizedSmelter;
import modtweaker2.mods.mekanism.handlers.Enrichment;
import modtweaker2.mods.mekanism.handlers.Infuser;
import modtweaker2.mods.mekanism.handlers.Purification;
import modtweaker2.mods.mekanism.handlers.Reaction;
import modtweaker2.mods.mekanism.handlers.Sawmill;
import modtweaker2.mods.mekanism.handlers.Separator;
import modtweaker2.mods.mekanism.handlers.SolarEvaporation;
import modtweaker2.mods.mekanism.handlers.SolarNeutronActivator;
import modtweaker2.utils.TweakerPlugin;

public class Mekanism extends TweakerPlugin {
    public Mekanism() {
        MineTweakerAPI.registerBracketHandler(new GasBracketHandler());
        MineTweakerAPI.registerClass(ChemicalCrystallizer.class);
        MineTweakerAPI.registerClass(ChemicalDissolution.class);
        MineTweakerAPI.registerClass(ChemicalInfuser.class);
        MineTweakerAPI.registerClass(ChemicalInjection.class);
        MineTweakerAPI.registerClass(ChemicalOxidizer.class);
        MineTweakerAPI.registerClass(ChemicalWasher.class);
        MineTweakerAPI.registerClass(Combiner.class);
        MineTweakerAPI.registerClass(Compressor.class);
        MineTweakerAPI.registerClass(Crusher.class);
        MineTweakerAPI.registerClass(EnergizedSmelter.class);
        MineTweakerAPI.registerClass(Enrichment.class);
        MineTweakerAPI.registerClass(Infuser.class);
        MineTweakerAPI.registerClass(Purification.class);
        MineTweakerAPI.registerClass(Reaction.class);
        MineTweakerAPI.registerClass(Sawmill.class);
        MineTweakerAPI.registerClass(Separator.class);
        MineTweakerAPI.registerClass(SolarEvaporation.class);
        MineTweakerAPI.registerClass(SolarNeutronActivator.class);
    }
}
