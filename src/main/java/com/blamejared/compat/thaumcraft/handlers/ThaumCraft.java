package com.blamejared.compat.thaumcraft.handlers;

import com.blamejared.compat.thaumcraft.handlers.aspects.*;
import com.blamejared.compat.thaumcraft.handlers.brackets.BracketHandlerAspect;
import com.blamejared.mtlib.helpers.LogHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.*;
import crafttweaker.zenscript.GlobalRegistry;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thaumcraft.api.aspects.*;
import thaumcraft.api.crafting.*;
import thaumcraft.api.internal.*;

import static crafttweaker.mc1120.commands.SpecialMessagesChat.*;

public class ThaumCraft {
    
    
    public static void preInit() {
        GlobalRegistry.registerBracketHandler(new BracketHandlerAspect());
    }
    
    public static void registerCommands() {
        CTChatCommand.registerCommand(new CraftTweakerCommand("thaumcraftDump") {
            
            @Override
            protected void init() {
                setDescription(getClickableCommandText("\u00A72/ct thaumcraftDump", "/ct thaumcraftDump", true), getNormalMessage(" \u00A73Dumps all the Thaumcraft information to the crafttweaker log"));
            }
            
            @Override
            public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
                CraftTweakerAPI.logCommand("Thaumcraft dump:");
                CraftTweakerAPI.logCommand("-Smelting Bonus:");
                CommonInternals.smeltingBonus.forEach(b -> CraftTweakerAPI.logCommand(String.format("--in: %s, out: %s, change: %s", LogHelper.getStackDescription(b.in), LogHelper.getStackDescription(b.out), b.chance)));
                CraftTweakerAPI.logCommand("-Warp");
                CommonInternals.warpMap.forEach((key, val) -> CraftTweakerAPI.logCommand(String.format("--Item: %s has warp: %s", key, val)));
                CraftTweakerAPI.logCommand("-Aspects");
                Aspect.aspects.forEach((key, val) -> CraftTweakerAPI.logCommand("--Aspect " + val.getName()));
                CraftTweakerAPI.logCommand("-Recipes(Grouped)");
                CommonInternals.craftingRecipeCatalog.forEach((key, val) -> {
                    if(val instanceof CrucibleRecipe) {
                        ItemStack recipeOutput = ((CrucibleRecipe) val).getRecipeOutput();
                        CraftTweakerAPI.logCommand(String.format("--Crucible: name: %s, group: %s, research: %s, output %s", key, val.getGroup(), val.getResearch(), LogHelper.getStackDescription(recipeOutput)));
                    } else if(val instanceof InfusionRecipe) {
                        Object recipeOutput = ((InfusionRecipe) val).getRecipeOutput();
                        CraftTweakerAPI.logCommand(String.format("--Infusion: name: %s, group: %s, research: %s, output %s", key, val.getGroup(), val.getResearch(), LogHelper.getStackDescription(recipeOutput)));
                    } else {
                        CraftTweakerAPI.logCommand(String.format("--Unknown: name: %s, group: %s, research: %s", key, val.getGroup(), val.getResearch()));
                    }
                });
                CraftTweakerAPI.logCommand("-ArcaneWorkbench");
                for(IRecipe recipe : ForgeRegistries.RECIPES) {
                    if(recipe instanceof IArcaneRecipe) {
                        CraftTweakerAPI.logCommand(String.format("--Output: %s, name %s", LogHelper.getStackDescription(recipe.getRecipeOutput()), recipe.getRegistryName()));
                    }
                }
                CraftTweakerAPI.logCommand("-Lootbags");
                CraftTweakerAPI.logCommand("--Common");
                WeightedRandomLoot.lootBagCommon.forEach(re -> CraftTweakerAPI.logCommand(LogHelper.getStackDescription(re.item) + ""));
                CraftTweakerAPI.logCommand("--Uncommon");
                WeightedRandomLoot.lootBagUncommon.forEach(re -> CraftTweakerAPI.logCommand(LogHelper.getStackDescription(re.item) + ""));
                CraftTweakerAPI.logCommand("--Rare");
                WeightedRandomLoot.lootBagRare.forEach(re -> CraftTweakerAPI.logCommand(LogHelper.getStackDescription(re.item) + ""));
                
                sender.sendMessage(getLinkToCraftTweakerLog("Thaumcraft dump generated", sender));
            }
        });
    }
    
    public static Aspect getAspect(CTAspect aspect) {
        return aspect.getInternal();
    }
    
    public static Aspect getAspect(CTAspectStack aspect) {
        return aspect.getInternal().getInternal();
    }
    
    public static AspectList getAspects(CTAspectStack[] aspects) {
        AspectList list = new AspectList();
        for(CTAspectStack aspect : aspects) {
            list.add(getAspect(aspect), aspect.getAmount());
        }
        return list;
    }
}
