package com.blamejared.compat.botania.lexicon.commands;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import crafttweaker.mc1120.commands.SpecialMessagesChat;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import vazkii.botania.api.BotaniaAPI;

public class LexiconKnowledgeTypesLogger extends CraftTweakerCommand {
    
    public LexiconKnowledgeTypesLogger() {
        super("botlextypes");
    }
    
    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] arguments) {
        CraftTweakerAPI.logCommand("Botania Lexicon Knowledge Types");
        for(String key : BotaniaAPI.knowledgeTypes.keySet()) {
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
