package com.blamejared.compat.botania.commands;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import crafttweaker.mc1120.commands.SpecialMessagesChat;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import vazkii.botania.api.BotaniaAPI;

public class BotaniaBrewLogger extends CraftTweakerCommand {
    
    public BotaniaBrewLogger() {
        super("botbrews");
    }
    
    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] arguments) {
        for(String key : BotaniaAPI.brewMap.keySet()) {
            CraftTweakerAPI.logCommand(key);
        }
        
        if(sender != null) {
            sender.sendMessage(SpecialMessagesChat.getLinkToCraftTweakerLog("List generated;", sender));
        }
    }
    
    @Override
    protected void init() {
    }
}
