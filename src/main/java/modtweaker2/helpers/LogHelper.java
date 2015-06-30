package modtweaker2.helpers;

import java.util.List;
import java.util.ListIterator;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import net.minecraft.item.ItemStack;

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
    
    public static String getArrayDescription(List<ItemStack> stacks) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(ItemStack stack : stacks) {
            sb.append(InputHelper.getStackDescription(stack)).append(", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append(']');
        
        return sb.toString();
    }

    public static List<String> toLowerCase(List<String> stringList) {
        ListIterator<String> iterator = stringList.listIterator();
        
        while(iterator.hasNext()) {
            iterator.set(iterator.next().toLowerCase());
        }
        
        return stringList;
    }
    
    public static String join(List<String> list, String conjunction) {
        StringBuilder sb = new StringBuilder();
        
        if(conjunction == null) {
            conjunction = ", ";
        }
        
        if(list != null && !list.isEmpty()) {
            for(String string : list) {
                sb.append(string).append(conjunction);
            }
            
            sb.setLength(sb.length() - conjunction.length());
        }
        
        return sb.toString();
    }
}
