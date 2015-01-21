package modtweaker.mods.botania.commands;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;

public class BotaniaBrewLogger implements ICommandFunction{

    @Override
    public void execute(String[] arguments, IPlayer player) {
    	Set<String> brew_keys=BotaniaAPI.brewMap.keySet();
        System.out.println("Brews: " + brew_keys.size());
        for (String key : brew_keys) {
            System.out.println("Brew " + key);
            MineTweakerAPI.logCommand(key);
        }

        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}
