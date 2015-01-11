package modtweaker.mods.thaumcraft;

import java.util.ArrayList;
import java.util.HashMap;

import modtweaker.helpers.ReflectionHelper;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;

public class ThaumcraftHelper {
    public static ArrayList recipes = null;
    public static HashMap<Object, Integer> warpList;

    static {
        try {
            recipes = ReflectionHelper.getStaticObject(ThaumcraftApi.class, "recipes");
            warpList = ReflectionHelper.getStaticObject(ThaumcraftApi.class, "warpMap");
        } catch (Exception e) {}
    }

    private ThaumcraftHelper() {}

    public static AspectList parseAspects(String aspects) {
        return parseAspects(new AspectList(), aspects);
    }

    public static AspectList parseAspects(AspectList list, String str) {
        if (list == null) list = new AspectList();
        if (str == null || str.equals("")) return list;
        String[] aspects = str.split(",");
        for (String aspect : aspects) {
            if (aspect.startsWith(" ")) aspect = aspect.replaceFirst(" ", "");
            String[] aspct = aspect.split("\\s+");
            if (aspct.length == 2) list.add(Aspect.aspects.get(aspct[0]), Integer.parseInt(aspct[1]));
        }

        return list;
    }

    public static AspectList removeAspects(AspectList list, String str) {
        String[] aspects = str.split(",");
        for (String aspect : aspects) {
            if (aspect.startsWith(" ")) aspect = aspect.replaceFirst(" ", "");
            String[] aspct = aspect.split("\\s+");
            if (aspct.length == 2) {
                list.remove(Aspect.aspects.get(aspct[0]), Integer.parseInt(aspct[1]));
            }
        }

        return list;
    }

    public static String getResearchTab(String key) {
        for (String tab : ResearchCategories.researchCategories.keySet()) {
            for (String research : ResearchCategories.researchCategories.get(tab).research.keySet()) {
                if (research.equals(key)) return tab;
            }
        }
        return null;
    }
}
