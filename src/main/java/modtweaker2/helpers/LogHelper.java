package modtweaker2.helpers;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;

public class LogHelper {
    public static void logPrinted(IPlayer player) {
        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }

    public static void log(IPlayer player, String message) {
        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage(message));
        }
    }

    public static void print(String string) {
        System.out.println(string);
        MineTweakerAPI.logCommand(string);
    }
    
    public static void logError(String message) {
        MineTweakerAPI.logError("[ModTweaker2] " + message);
    }
    
    public static void logError(String message, Throwable exception) {
        MineTweakerAPI.logError("[ModTweaker2] " + message, exception);
    }
    
    public static void logWarning(String message) {
        MineTweakerAPI.logWarning("[ModTweaker2] " + message);
    }
    
    public static void logInfo(String message) {
        MineTweakerAPI.logInfo("[ModTweaker2] " + message);
    }
}
