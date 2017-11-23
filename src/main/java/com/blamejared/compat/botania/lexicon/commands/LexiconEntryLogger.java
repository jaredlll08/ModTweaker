package com.blamejared.compat.botania.lexicon.commands;

import com.blamejared.compat.botania.BotaniaHelper;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;

public class LexiconEntryLogger extends CraftTweakerCommand{

	public LexiconEntryLogger() {
		super("botlexentries");
	}
	
    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] arguments) {
    	LexiconCategory category=null;
    	if(arguments.length>0)
    	{
    		category= BotaniaHelper.findCatagory(arguments[0]);
    		if(category==null)
    		{
    			CraftTweakerAPI.getLogger().logError("Category not found (" + arguments[0]+")");
    			return;
    		}
    	}
        CraftTweakerAPI.logCommand("Botania Lexicon Entries");
        for (LexiconEntry entry : BotaniaAPI.getAllEntries()) {
        	if(category==null || entry.category==category)
        	{
        		CraftTweakerAPI.logCommand(entry.getUnlocalizedName());
        	}
        }

        if (sender != null) {
            sender.sendMessage(new TextComponentString("List generated; see crafttweaker.log in your minecraft dir"));
        }
    }
    
	@Override
	protected void init() {
	}
}
