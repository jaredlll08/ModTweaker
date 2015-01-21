package modtweaker.mods.botania.lexicon.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker.mods.botania.BotaniaHelper;

public class LexiconPageLogger implements ICommandFunction{

    @Override
    public void execute(String[] arguments, IPlayer player) {
    	LexiconEntry entry=null;
    	if(arguments.length>0)
    	{
    		entry=BotaniaHelper.findEntry(arguments[0]);
    		if(entry==null)
    		{
    			MineTweakerAPI.getLogger().logError("Entry not found (" + arguments[0]+")");
    			return;
    		}
    	}
    	List<LexiconEntry> entries;
    	List<LexiconPage> pages=new ArrayList();
    	if(entry!=null)
        	pages.addAll(entry.pages);
    	else
    		for (LexiconEntry current_Entry : BotaniaAPI.getAllEntries())
            	pages.addAll(current_Entry.pages);
        System.out.println("Pages: " + pages.size());
        
        for (LexiconPage page : pages) {
       		System.out.println("Page " + page.getUnlocalizedName() + " (" + page.getClass() + ")");
       		MineTweakerAPI.logCommand(page.getUnlocalizedName() + " (" + page.getClass() + ")");
        }

        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}
