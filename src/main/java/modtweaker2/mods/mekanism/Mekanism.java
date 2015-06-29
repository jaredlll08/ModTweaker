//package modtweaker2.mods.mekanism;
//
//import java.util.regex.Pattern;
//
//import minetweaker.MineTweakerAPI;
//import modtweaker2.mods.mekanism.gas.GasBracketHandler;
//import modtweaker2.mods.mekanism.handlers.ChemicalOxidizer;
//import modtweaker2.mods.mekanism.handlers.ChemicalWasher;
//import modtweaker2.mods.mekanism.handlers.Combiner;
//import modtweaker2.mods.mekanism.handlers.Compressor;
//import modtweaker2.mods.mekanism.handlers.Crusher;
//import modtweaker2.mods.mekanism.handlers.Enrichment;
//import modtweaker2.mods.mekanism.handlers.Purification;
//import modtweaker2.mods.mekanism.handlers.Reaction;
//import modtweaker2.mods.mekanism.handlers.Sawmill;
//import modtweaker2.mods.mekanism.handlers.Separator;
//import modtweaker2.mods.mekanism.handlers.v8.ChemicalCrystallizer;
//import modtweaker2.mods.mekanism.handlers.v8.ChemicalDissolution;
//import modtweaker2.mods.mekanism.handlers.v8.ChemicalInfuser;
//import modtweaker2.mods.mekanism.handlers.v8.ChemicalInjection;
//import modtweaker2.mods.mekanism.handlers.v8.Infuser;
//import modtweaker2.utils.TweakerPlugin;
//import cpw.mods.fml.common.Loader;
//import cpw.mods.fml.common.ModContainer;
//
//public class Mekanism extends TweakerPlugin {
//    public static final boolean v7 = isV7();
//    public Mekanism() {
//        MineTweakerAPI.registerBracketHandler(new GasBracketHandler());
//        MineTweakerAPI.registerClass(Combiner.class);
//        MineTweakerAPI.registerClass(Compressor.class);
//        MineTweakerAPI.registerClass(Crusher.class);
//        MineTweakerAPI.registerClass(Enrichment.class);
//        MineTweakerAPI.registerClass(Infuser.class);
//        MineTweakerAPI.registerClass(Purification.class);
//        MineTweakerAPI.registerClass(Reaction.class);
//        MineTweakerAPI.registerClass(Sawmill.class);
//        MineTweakerAPI.registerClass(Separator.class);
////        MineTweakerAPI.registerClass(ChemicalCrystallizer.class);
//        MineTweakerAPI.registerClass(ChemicalCrystallizer.class);
//        MineTweakerAPI.registerClass(ChemicalDissolution.class);
//        MineTweakerAPI.registerClass(ChemicalInfuser.class);
//        MineTweakerAPI.registerClass(ChemicalInjection.class);
//        MineTweakerAPI.registerClass(ChemicalOxidizer.class);
//        MineTweakerAPI.registerClass(ChemicalWasher.class);
//    }
//
//    public static boolean isV7(){
//        Pattern version = Pattern.compile("7.*");
//        for (ModContainer loadedMod : Loader.instance().getActiveModList())
//        {
//            if (loadedMod.getModId().equals("Mekanism") && version.matcher(loadedMod.getVersion()).find()) return true;
//        }
//        return false;
//    }
//}
