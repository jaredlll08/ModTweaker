package modtweaker.mods.tconstruct.commands;

import com.blamejared.mtlib.commands.CommandLoggerMulti;
import com.blamejared.mtlib.helpers.LogHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker.mods.tconstruct.TConstructHelper;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TConstructLogger extends CommandLoggerMulti {
    private static final List<String> validArguments = new LinkedList<String>();

    static {
        validArguments.add("Casting");
        validArguments.add("Drying");
        validArguments.add("Smeltery");
    }

    @Override
    public void execute(String[] arguments, IPlayer player) {
//            if(args.isEmpty() || args.contains("Smeltery")) {
//                for(MeltingRecipe recipe : TConstructHelper.smeltingList) {
//                    int temperature = recipe.getTemperature();
//                    ItemStack renderItem = TConstructHelper.renderIndex.get(recipe.getKey());
//
//                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Smeltery.addMelting(%s, %s, %d, %s);",
//                            LogHelper.getStackDescription(new ItemStack(recipe.getKey().item, 1, recipe.getKey().meta)),
//                            LogHelper.getStackDescription(recipe.getValue()),
//                            temperature,
//                            LogHelper.getStackDescription(renderItem)));
//                }


//                for(FluidStack fuel : TConstructHelper.fuelList) {
//                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Smeltery.addFuel(%s, %d, %d);",
//                            LogHelper.getStackDescription(new FluidStack(fuel.getFluid(), 1)),
//                            fuel[0],
//                            fuel.getValue()[1]));
//                }
        super.execute(arguments, player);
    }


    @Override
    public Map<String, ICommandFunction> getLists() {
        Map<String, ICommandFunction> logs = new HashMap<>();
        logs.put("casting", (strings, player) -> {
            TConstructHelper.basinCasting.forEach(recipe -> {
                if(recipe instanceof CastingRecipe) {
                    if(((CastingRecipe) recipe).cast != null) {
                        for(ItemStack item : ((CastingRecipe) recipe).cast.getInputs()) {
                            MineTweakerAPI.logCommand(String.format("mods.tconstruct.Casting.addBasinRecipe(%s, %s, %s, %s, %d);",
                                    LogHelper.getStackDescription(((CastingRecipe) recipe).getResult()),
                                    LogHelper.getStackDescription(((CastingRecipe) recipe).getFluid()),
                                    LogHelper.getStackDescription(item),
                                    recipe.consumesCast(),
                                    recipe.getTime()));
                        }
                    } else {
                        MineTweakerAPI.logCommand(String.format("mods.tconstruct.Casting.addBasinRecipe(%s, %s, %s, %s, %d);",
                                LogHelper.getStackDescription(((CastingRecipe) recipe).getResult()),
                                LogHelper.getStackDescription(((CastingRecipe) recipe).getFluid()),
                                LogHelper.getStackDescription((Object) null),
                                recipe.consumesCast(),
                                recipe.getTime()));
                    }
                }


            });
            TConstructHelper.tableCasting.forEach(recipe ->
            {
                if(recipe instanceof CastingRecipe) {
                    if(((CastingRecipe) recipe).cast != null) {
                        for(ItemStack item : ((CastingRecipe) recipe).cast.getInputs()) {
                            MineTweakerAPI.logCommand(String.format("mods.tconstruct.Casting.addTableRecipe(%s, %s, %s, %s, %d);",
                                    LogHelper.getStackDescription(((CastingRecipe) recipe).getResult()),
                                    LogHelper.getStackDescription(((CastingRecipe) recipe).getFluid()),
                                    LogHelper.getStackDescription(item),
                                    recipe.consumesCast(),
                                    recipe.getTime()));
                        }
                    } else {
                        MineTweakerAPI.logCommand(String.format("mods.tconstruct.Casting.addTableRecipe(%s, %s, %s, %s, %d);",
                                LogHelper.getStackDescription(((CastingRecipe) recipe).getResult()),
                                LogHelper.getStackDescription(((CastingRecipe) recipe).getFluid()),
                                LogHelper.getStackDescription((Object) null),
                                recipe.consumesCast(),
                                recipe.getTime()));
                    }
                }
            });
        });

        logs.put("drying", (args, player) -> TinkerRegistry.getAllDryingRecipes().
                forEach(recipe -> {
                    for(ItemStack item : recipe.input.getInputs()) {
                        MineTweakerAPI.logCommand(String.format("mods.tconstruct.Drying.addRecipe(%s, %s, %d);",
                                LogHelper.getStackDescription(item),
                                LogHelper.getStackDescription(recipe.getResult()),
                                recipe.time));
                    }
                }));

        logs.put("alloys", (strings, player) -> TConstructHelper.alloys.forEach(recipe -> MineTweakerAPI.logCommand(String.format("mods.tconstruct.Smeltery.addAlloy(%s, %s);",
                LogHelper.getStackDescription(recipe.getResult()),
                LogHelper.getListDescription(recipe.getFluids())))));
        return logs;
    }
}
