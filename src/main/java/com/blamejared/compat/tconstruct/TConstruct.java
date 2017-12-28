package com.blamejared.compat.tconstruct;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;

import static crafttweaker.mc1120.commands.SpecialMessagesChat.*;

public class TConstruct {
    
    public static void registerCommands() {
        CTChatCommand.registerCommand(new CraftTweakerCommand("ticmat") {
            
            @Override
            protected void init() {
                setDescription(getClickableCommandText("\u00A72/ct ticmat", "/ct ticmat", true), getNormalMessage(" \u00A73Dumps all the Tinkers Construct material names to the crafttweaker log"));
            }
            
            @Override
            public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
                CraftTweakerAPI.logCommand("Tinkers Construct Materials:");
                for(Material material : TinkerRegistry.getAllMaterials()) {
                    StringBuilder builder = new StringBuilder("-").append(material.identifier).append("\n");
                    for(IMaterialStats stats : material.getAllStats()) {
                        for(String s : stats.getLocalizedInfo()) {
                            builder.append(s.replaceAll(TextFormatting.RESET.toString(), "").replaceAll("[^\\x00-\\x7F]", "")).append("\n");
                        }
                        
                    }
                    CraftTweakerAPI.logCommand(builder.toString());
                }
                
                sender.sendMessage(getLinkToCraftTweakerLog("Tinkers Construct Materials list generated", sender));
            }
        });
    }
}
