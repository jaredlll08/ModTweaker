package com.blamejared.compat.chisel;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;
import team.chisel.api.carving.CarvingUtils;

import static crafttweaker.mc1120.commands.SpecialMessagesChat.*;

public class Chisel {

    
    public static void registerCommands(){
        CTChatCommand.registerCommand(new CraftTweakerCommand("chiselGroups") {
        
            @Override
            protected void init() {
                setDescription(getClickableCommandText("\u00A72/ct chiselGroups", "/ct chiselGroups", true), getNormalMessage(" \u00A73Dumps all the Chisel group names to the crafttweaker log"));
            }
        
            @Override
            public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) {
                CraftTweakerAPI.logCommand("Chisel Group Names:");
                CarvingUtils.getChiselRegistry().getSortedGroupNames().forEach(str -> CraftTweakerAPI.logCommand("- " + str));
                sender.sendMessage(getLinkToCraftTweakerLog("Chisel Group Names list generated", sender));
            }
        });
    }
}
