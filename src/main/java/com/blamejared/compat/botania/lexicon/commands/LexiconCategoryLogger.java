package com.blamejared.compat.botania.lexicon.commands;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;

public class LexiconCategoryLogger extends CraftTweakerCommand{

	public LexiconCategoryLogger() {
		super("botlexcats");
	}
	
	
    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] arguments) {
    	CraftTweakerAPI.logCommand("Botania Lexicon Categories");
        for (LexiconCategory category : BotaniaAPI.getAllCategories()) {
            CraftTweakerAPI.logCommand(category.getUnlocalizedName());
        }

        if (sender != null) {
            sender.sendMessage(new TextComponentString("List generated; see crafttweaker.log in your minecraft dir"));
        }
    }
    
	@Override
	protected void init() {
	}
}
