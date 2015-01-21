package modtweaker.mods.botania.lexicon.commands;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;

public class LexiconKnowledgeTypesLogger implements ICommandFunction{

    @Override
    public void execute(String[] arguments, IPlayer player) {
    	Set<String> types=BotaniaAPI.knowledgeTypes.keySet();
        System.out.println("Knowledge Types: " + types.size());
        for (String key : types) {
            System.out.println("Knowledge Type " + key);
            MineTweakerAPI.logCommand(key);
        }

        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}
