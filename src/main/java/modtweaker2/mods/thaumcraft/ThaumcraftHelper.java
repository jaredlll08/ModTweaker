package modtweaker2.mods.thaumcraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modtweaker2.helpers.ReflectionHelper;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApi.EntityTags;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;

public class ThaumcraftHelper {
    public static HashMap<Object, Integer> warpList;

    static {
        try {
            warpList = ReflectionHelper.getStaticObject(ThaumcraftApi.class, "warpMap");
        } catch (Exception e) {}
    }

    private ThaumcraftHelper() {}

    public static AspectList parseAspects(String aspects) {
        return parseAspects(new AspectList(), aspects);
    }

    public static AspectList parseAspects(AspectList list, String str) {
    	AspectList output=new AspectList();
        if (list != null)
        	for(Aspect aspect : list.getAspectsSortedAmount())
        	{
        		if(aspect!=null)
        			output.add(aspect, list.getAmount(aspect));
        	}
        if (str == null || str.equals("")) return list;
        String[] aspects = str.split(",");
        for (String aspect : aspects) {
            if (aspect.startsWith(" ")) aspect = aspect.replaceFirst(" ", "");
            String[] aspct = aspect.split("\\s+");
            if (aspct.length == 2) output.add(Aspect.aspects.get(aspct[0]), Integer.parseInt(aspct[1]));
        }

        return output;
    }

    public static AspectList removeAspects(AspectList list, String str) {
    	AspectList output=new AspectList();
        if (list != null)
        	for(Aspect aspect : list.getAspectsSortedAmount())
        	{
        		if(aspect!=null)
        			output.add(aspect, list.getAmount(aspect));
        	}
        String[] aspects = str.split(",");
        for (String aspect : aspects) {
            if (aspect.startsWith(" ")) aspect = aspect.replaceFirst(" ", "");
            String[] aspct = aspect.split("\\s+");
            if (aspct.length == 2) {
            	output.remove(Aspect.aspects.get(aspct[0]), Integer.parseInt(aspct[1]));
            }
        }

        return output;
    }

    public static String getResearchTab(String key) {
        for (String tab : ResearchCategories.researchCategories.keySet()) {
            for (String research : ResearchCategories.researchCategories.get(tab).research.keySet()) {
                if (research.equals(key)) return tab;
            }
        }
        return null;
    }

	public static AspectList getEntityAspects(String name) {
		for(EntityTags tag : ThaumcraftApi.scanEntities)
		{
			if(tag.entityName==name && tag.nbts.length==0)
				return tag.aspects;
		}
		return null;
	}

	public static void removeEntityAspects(String name) {
		List<EntityTags> tags = new ArrayList(ThaumcraftApi.scanEntities);
		for(EntityTags tag : tags)
		{
			if(tag.entityName==name && tag.nbts.length==0)
				ThaumcraftApi.scanEntities.remove(tag);
		}
	}
	
	public static String aspectsToString(AspectList aspects)
	{
		System.out.println(aspects);
		String output="";
		for(Aspect aspect : aspects.getAspectsSortedAmount())
		{
			if(aspect!=null)
				output+=aspect.getName()+" "+aspects.getAmount(aspect)+",";
		}
		return output;
	}
}
