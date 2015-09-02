package modtweaker2.mods.railcraft.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import mods.railcraft.api.crafting.IBlastFurnaceRecipe;
import mods.railcraft.api.crafting.ICokeOvenRecipe;
import mods.railcraft.api.crafting.IRockCrusherRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.ReflectionHelper;
import modtweaker2.helpers.StringHelper;
import modtweaker2.mods.railcraft.RailcraftHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class RailcraftLogger implements ICommandFunction {

    private static final List<String> validArguments = new LinkedList<String>();
    
    static {
        validArguments.add("BlastFurnace");
        validArguments.add("CokeOven");
        validArguments.add("RockCrusher");
        validArguments.add("RollingMachine");
    }
    
    @Override
    public void execute(String[] arguments, IPlayer player) {
        List<String> args = StringHelper.toLowerCase(Arrays.asList(arguments));
        
        if(!validArguments.containsAll(args)) {
            if(player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("Invalid arguments for command. Valid arguments: " + StringHelper.join(validArguments, ", ")));
            }
        } else {
            if(args.isEmpty() || args.contains("BlastFurnace")) {
                for(IBlastFurnaceRecipe recipe : RailcraftHelper.furnace) {
                    MineTweakerAPI.logCommand(String.format("mods.railcraft.BlastFurnace.addRecipe(%s, %s, %s, %d, %s);",
                            LogHelper.getStackDescription(recipe.getInput()),
                            ReflectionHelper.<Boolean>getObject(recipe, "matchDamage"),
                            ReflectionHelper.<Boolean>getObject(recipe, "matchNBT"),
                            recipe.getCookTime(),
                            LogHelper.getStackDescription(recipe.getOutput())));
                }
            }
            
            if(args.isEmpty() || args.contains("CokeOven")) {
                for(ICokeOvenRecipe recipe : RailcraftCraftingManager.cokeOven.getRecipes()) {
                    MineTweakerAPI.logCommand(String.format("mods.railcraft.CokeOven.addRecipe(%s, %s, %s, %s, %s, %d);",
                            LogHelper.getStackDescription(recipe.getInput()),
                            ReflectionHelper.<Boolean>getObject(recipe, "matchDamage"),
                            ReflectionHelper.<Boolean>getObject(recipe, "matchNBT"),
                            LogHelper.getStackDescription(recipe.getOutput()),
                            LogHelper.getStackDescription(recipe.getFluidOutput()),
                            recipe.getCookTime()));
                }
            }
            
            if(args.isEmpty() || args.contains("RockCrusher")) {
                for(IRockCrusherRecipe recipe : RailcraftHelper.crusher) {
                    MineTweakerAPI.logCommand(String.format("mods.railcraft.RockCrusher.addRecipe(%s, %s, %s, %s);",
                            LogHelper.getStackDescription(recipe.getInput()),
                            ReflectionHelper.<Boolean>getObject(recipe, "matchDamage"),
                            ReflectionHelper.<Boolean>getObject(recipe, "matchNBT"),
                            getRockCrusherOutputDescription(recipe.getOutputs())));
                }
            }
            
            if(args.isEmpty() || args.contains("RollingMachine")) {
                for(IRecipe recipe : RailcraftHelper.rolling) {
                    MineTweakerAPI.logCommand(String.format("mods.railcraft.RollingMachine.addRecipe(%s, %s);",
                            LogHelper.getStackDescription(recipe.getRecipeOutput()),
                            LogHelper.getCraftingDescription(recipe)));
                }
            }
            
            if (player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
            }
        }
    }

    private Object getRockCrusherOutputDescription(List<Entry<ItemStack, Float>> outputs) {
        if(outputs.isEmpty()) {
            return "[], []";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(Entry<ItemStack, Float> entry : outputs) {
            sb.append(LogHelper.getStackDescription(entry.getKey())).append(", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append("], [");
        for(Entry<ItemStack, Float> entry : outputs) {
            sb.append(entry.getValue()).append(", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append("]");
        
        return sb.toString();
    }
}
