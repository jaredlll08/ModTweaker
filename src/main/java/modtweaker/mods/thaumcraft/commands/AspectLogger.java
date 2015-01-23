package modtweaker.mods.thaumcraft.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApi.EntityTags;
import thaumcraft.api.ThaumcraftApi.EntityTagsNBT;
import thaumcraft.api.aspects.AspectList;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker.mods.thaumcraft.ThaumcraftHelper;

public class AspectLogger implements ICommandFunction{

    @Override
    public void execute(String[] arguments, IPlayer player) {
    	List<String> lines=new ArrayList();
    	for(Entry<List, AspectList> entry : ThaumcraftApi.objectTags.entrySet())
    	{
    		List<String> itemList=new ArrayList<String>();
    		for(int i=0;i<entry.getKey().size();i+=2)
    		{
    			Item item=(Item)entry.getKey().get(i);
    			
    			itemList.add("<"+item.itemRegistry.getNameForObject(item)+":"+entry.getKey().get(i+1)+">");
    		}
    		lines.add(itemList.toString()+": "+ThaumcraftHelper.aspectsToString(entry.getValue()));
    	}
    	for (EntityTags tags : ThaumcraftApi.scanEntities)
    	{
    		List<String> tagsList=new ArrayList<String>();
    		for(EntityTagsNBT tag : tags.nbts)
    		{
    			tagsList.add(tag.name+":"+tag.value);
    		}
    		String stringTags="";
    		if(tags.nbts.length!=0)
    			stringTags=":"+tagsList.toString();
    		lines.add("<"+tags.entityName+stringTags+">"+": "+ThaumcraftHelper.aspectsToString(tags.aspects));
    	}
        System.out.println("Aspects: " + lines.size());
        for (String line : lines) {
            System.out.println("Aspect " + line);
            MineTweakerAPI.logCommand(line);
        }

        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}
