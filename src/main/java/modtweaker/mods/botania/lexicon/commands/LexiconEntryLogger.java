package modtweaker.mods.botania.lexicon.commands;

import java.util.Collections;
import java.util.List;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker.mods.botania.BotaniaHelper;

public class LexiconEntryLogger implements ICommandFunction{

    @Override
    public void execute(String[] arguments, IPlayer player) {
    	LexiconCategory category=null;
    	if(arguments.length>0)
    	{
    		category=BotaniaHelper.findCatagory(arguments[0]);
    		if(category==null)
    		{
    			MineTweakerAPI.getLogger().logError("Category not found (" + arguments[0]+")");
    			return;
    		}
    	}
    	List<LexiconEntry> entries=BotaniaAPI.getAllEntries();
        System.out.println("Entries: " + entries.size());
        for (LexiconEntry entry : entries) {
        	if(category==null || entry.category==category)
        	{
        		System.out.println("Entry " + entry.getUnlocalizedName());
        		MineTweakerAPI.logCommand(entry.getUnlocalizedName());
        	}
        }

        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}
