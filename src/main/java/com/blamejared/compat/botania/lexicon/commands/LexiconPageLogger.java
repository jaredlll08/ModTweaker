package com.blamejared.compat.botania.lexicon.commands;

import java.util.ArrayList;
import java.util.List;

import com.blamejared.compat.botania.BotaniaHelper;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;

public class LexiconPageLogger extends CraftTweakerCommand{

    public LexiconPageLogger() {
		super("botlexpages");
	}

	@Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] arguments) {
    	LexiconEntry entry=null;
    	if(arguments.length>0)
    	{
    		entry= BotaniaHelper.findEntry(arguments[0]);
    		if(entry==null)
    		{
    			CraftTweakerAPI.getLogger().logError("Entry not found (" + arguments[0]+")");
    			return;
    		}
    	}
    	List<LexiconPage> pages=new ArrayList<>();
    	if(entry!=null)
        	pages.addAll(entry.pages);
    	else
    		for (LexiconEntry current_Entry : BotaniaAPI.getAllEntries())
            	pages.addAll(current_Entry.pages);
        
    	CraftTweakerAPI.logCommand("Botania Lexicon Pages");
        for (LexiconPage page : pages) {
       		CraftTweakerAPI.logCommand(page.getUnlocalizedName() + " (" + page.getClass() + ")");
        }

        if (sender != null) {
            sender.sendMessage(new TextComponentString("List generated; see crafttweaker.log in your minecraft dir"));
        }
    }

	@Override
	protected void init() {
	}
}
