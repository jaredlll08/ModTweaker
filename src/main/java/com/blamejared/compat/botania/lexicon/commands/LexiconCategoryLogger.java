package com.blamejared.compat.botania.lexicon.commands;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import crafttweaker.mc1120.commands.SpecialMessagesChat;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;

public class LexiconCategoryLogger extends CraftTweakerCommand {
    
    public LexiconCategoryLogger() {
        super("botlexcats");
    }
    
    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] arguments) {
        CraftTweakerAPI.logCommand("Botania Lexicon Categories");
        for(LexiconCategory category : BotaniaAPI.getAllCategories()) {
            CraftTweakerAPI.logCommand(category.getUnlocalizedName());
        }
        
        if(sender != null) {
            sender.sendMessage(SpecialMessagesChat.getLinkToCraftTweakerLog("List generated;", sender));
        }
    }
    
    @Override
    protected void init() {
    }
}
