package modtweaker2.mods.botania;

import java.util.List;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BotaniaHelper {
	public static LexiconCategory findCatagory(String name)
	{
		List<LexiconCategory> catagories=BotaniaAPI.getAllCategories();
		for(int i=0;i<catagories.size();i++)
		{
			if(catagories.get(i).getUnlocalizedName().equalsIgnoreCase(name))
				return catagories.get(i);
		}
		return null;
	}

	public static LexiconEntry findEntry(String name)
	{
		List<LexiconEntry> entries=BotaniaAPI.getAllEntries();
		for(int i=0;i<entries.size();i++)
		{
			if(entries.get(i).getUnlocalizedName().equalsIgnoreCase(name))
				return entries.get(i);
		}
		return null;
	}

	public static KnowledgeType findKnowledgeType(String name)
	{
		if(BotaniaAPI.knowledgeTypes.containsKey(name))
			return BotaniaAPI.knowledgeTypes.get(name);
		return null;
	}
}
