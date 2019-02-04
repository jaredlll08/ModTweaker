package com.blamejared.compat.thaumcraft.handlers.handlers;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.BaseAction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchEntry;
import thaumcraft.common.lib.research.ResearchManager;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

@ZenClass("mods.thaumcraft.Research")
@ZenRegister
@ModOnly("thaumcraft")
public class Research {
    private static File globalDir = new File("scripts");
    private static JsonParser parser = new JsonParser();

    // Unfortunately, reflection is required to inject properly otherwise
    // we would have to reimplement all of the parsing functions. Ew.
    private static Method parseJson = null;
    private static Method categoryAdd = null;

    @ZenMethod
    public static void addResearch(String filename) {
        if (filename.isEmpty() || !globalDir.exists()) {
            CraftTweakerAPI.logError("Invalid location passed to addResearch or the scripts folder doesn't exist");
            return;
        }

        File resource = new File(globalDir, filename);

        if (!resource.exists()) {
            CraftTweakerAPI.logError("File specified for Thaumcraft research does not exist: " + filename);
            return;
        }

        ModTweaker.LATE_ADDITIONS.add(new AddEntries(resource));
    }

    @ZenMethod
    public static void addCategory(String key, String researchParent, String resourceIcon, String resourceBackground, String resourceBackground2) {
        if (key.isEmpty()) {
            CraftTweakerAPI.logError("Invalid Thaumcraft research category: cannot have a blank name.");
            return;
        }

        if (researchParent.isEmpty()) {
            researchParent = null;
        }

        if (resourceIcon.isEmpty()) {
            CraftTweakerAPI.logError("Invalid Thaumcraft research category: must provide an icon resource, eg. thaumcraft:textures/research/cat_artifice.png");
            return;
        }

        if (resourceBackground == null || resourceBackground.isEmpty()) {
            resourceBackground = "thaumcraft:textures/gui/gui_research_back_1.jpg";
        }

        if (resourceBackground2 != null && resourceBackground2.isEmpty()) {
            resourceBackground2 = null;
        }

        ModTweaker.LATE_ADDITIONS.add(new AddCategory(key, researchParent, resourceIcon, resourceBackground, resourceBackground2));
    }

    private static class AddCategory extends BaseAction {
        private String key;
        private String researchParent;
        private ResourceLocation resourceIcon;
        private ResourceLocation resourceBackground;
        private ResourceLocation resourceBackground2;

        AddCategory(String key, String researchParent, String resourceIcon, String resourceBackground, @Nullable String resourceBackground2) {
            super("ResearchCategory");

            this.key = key;
            this.researchParent = researchParent;
            this.resourceIcon = new ResourceLocation(resourceIcon);
            this.resourceBackground = new ResourceLocation(resourceBackground);
            this.resourceBackground2 = (resourceBackground2 != null) ? new ResourceLocation(resourceBackground2) : null;
        }

        @Override
        public void apply() {
            if (resourceBackground2 == null) {
                ResearchCategories.registerCategory(key, researchParent, new AspectList(), resourceIcon, resourceBackground);
            } else {
                ResearchCategories.registerCategory(key, researchParent, new AspectList(), resourceIcon, resourceBackground, resourceBackground2);
            }
        }
    }

    private static class AddEntries extends BaseAction {
        private File resource;

        AddEntries(File resource) {
            super("ResearchEntries");
            this.resource = resource;
        }

        @Override
        public void apply() {
            try {
                InputStream input = new FileInputStream(this.resource);

                InputStreamReader reader = new InputStreamReader(input);
                JsonObject obj = parser.parse(reader).getAsJsonObject();

                JsonArray entries = obj.get("entries").getAsJsonArray();

                for (JsonElement l : entries) {
                    try {
                        addResearchToCategory(parseResearchJson(l.getAsJsonObject()));
                    } catch (Exception e) {
                        CraftTweakerAPI.logError("Error loading individual research entry for Thaumcraft file " + this.resource.toString());
                    }
                }
            } catch (Exception e) {
                CraftTweakerAPI.logError("File specified for Thaumcraft research no longer exists or is invalid: " + this.resource.toString());
            }
        }

        private ResearchEntry parseResearchJson(JsonObject obj) throws Exception {
            if (parseJson == null) {
                parseJson = ResearchManager.class.getDeclaredMethod("parseResearchJson", JsonObject.class);
                parseJson.setAccessible(true);
            }

            return (ResearchEntry) parseJson.invoke(null, obj);
        }

        private void addResearchToCategory(ResearchEntry entry) throws Exception {
            if (categoryAdd == null) {
                categoryAdd = ResearchManager.class.getDeclaredMethod("addResearchToCategory", ResearchEntry.class);
                categoryAdd.setAccessible(true);
            }

            categoryAdd.invoke(null, entry);
        }

        @Override
        public String describe() {
            return "Adding new research entry for " + resource.toString();
        }
    }
}
