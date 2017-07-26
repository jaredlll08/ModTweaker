package com.blamejared.compat.tconstruct.commands;

import com.blamejared.mtlib.commands.CommandLoggerMulti;
import com.blamejared.mtlib.helpers.LogHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import com.blamejared.compat.tconstruct.TConstructHelper;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;

import java.util.*;

public class TConstructLogger extends CommandLoggerMulti {

    private static final List<String> validArguments = new LinkedList<>();

    static {
        validArguments.add("casting");
        validArguments.add("drying");
        validArguments.add("alloys");
        validArguments.add("modifiers");
        validArguments.add("materials");
    }

    @Override
    public void execute(String[] arguments, IPlayer player) {
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
            TConstructHelper.tableCasting.forEach(recipe -> {
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

        logs.put("alloys", (strings, player) -> TConstructHelper.alloys.
                forEach(recipe -> MineTweakerAPI.logCommand(String.format("mods.tconstruct.Smeltery.addAlloy(%s, %s);",
                LogHelper.getStackDescription(recipe.getResult()),
                LogHelper.getListDescription(recipe.getFluids())))));

        logs.put("modifiers", (args, player) -> TConstructHelper.modifiers.keySet().
                forEach(entryName -> MineTweakerAPI.logCommand(entryName)));

        logs.put("materials", (args, player) -> {
            Collection<Material> materials = TinkerRegistry.getAllMaterials();
            materials.forEach(entry -> {
                String identifier = entry.getIdentifier();

                if(entry.hasStats(MaterialTypes.HEAD)) {
                    HeadMaterialStats statsHead = entry.getStats(MaterialTypes.HEAD);
                    MineTweakerAPI.logCommand(String.format("<material:%s>.durabilityHead = %d;",
                            identifier,
                            statsHead.durability
                    ));
                    MineTweakerAPI.logCommand(String.format("<material:%s>.miningSpeedHead = %f;",
                            identifier,
                            statsHead.miningspeed
                    ));
                    MineTweakerAPI.logCommand(String.format("<material:%s>.attackHead = %f;",
                            identifier,
                            statsHead.attack
                    ));
                    MineTweakerAPI.logCommand(String.format("<material:%s>.harvestLevelHead = %d;",
                            identifier,
                            statsHead.harvestLevel
                    ));
                }
                if(entry.hasStats(MaterialTypes.HANDLE)) {
                    HandleMaterialStats statsHandle = entry.getStats(MaterialTypes.HANDLE);
                    MineTweakerAPI.logCommand(String.format("<material:%s>.durabilityHandle = %d;",
                            identifier,
                            statsHandle.durability
                    ));
                    MineTweakerAPI.logCommand(String.format("<material:%s>.modifierHandle = %f;",
                            identifier,
                            statsHandle.modifier
                    ));
                }
                if(entry.hasStats(MaterialTypes.EXTRA)) {
                    ExtraMaterialStats statsExtra = entry.getStats(MaterialTypes.EXTRA);
                    MineTweakerAPI.logCommand(String.format("<material:%s>.durabilityExtra = %d;",
                            identifier,
                            statsExtra.extraDurability
                    ));
                }
                if(entry.hasStats(MaterialTypes.BOW)) {
                    BowMaterialStats statsBow = entry.getStats(MaterialTypes.BOW);
                    MineTweakerAPI.logCommand(String.format("<material:%s>.drawspeedBow = %f;",
                            identifier,
                            statsBow.drawspeed
                    ));
                    MineTweakerAPI.logCommand(String.format("<material:%s>.rangeBow = %f;",
                            identifier,
                            statsBow.range
                    ));
                    MineTweakerAPI.logCommand(String.format("<material:%s>.bonusDamageBow = %f;",
                            identifier,
                            statsBow.bonusDamage
                    ));
                }
                if(entry.hasStats(MaterialTypes.BOWSTRING)) {
                    BowStringMaterialStats statsBowString = entry.getStats(MaterialTypes.BOWSTRING);
                    MineTweakerAPI.logCommand(String.format("<material:%s>.modifierBowString = %f;",
                            identifier,
                            statsBowString.modifier
                    ));
                }
                if(entry.hasStats(MaterialTypes.SHAFT)) {
                    ArrowShaftMaterialStats statsArrowShaft = entry.getStats(MaterialTypes.SHAFT);
                    MineTweakerAPI.logCommand(String.format("<material:%s>.modifierArrowShaft = %f;",
                            identifier,
                            statsArrowShaft.modifier
                    ));
                    MineTweakerAPI.logCommand(String.format("<material:%s>.bonusAmmoArrowShaft = %d;",
                            identifier,
                            statsArrowShaft.bonusAmmo
                    ));
                }
                if(entry.hasStats(MaterialTypes.FLETCHING)) {
                    FletchingMaterialStats statsFletching = entry.getStats(MaterialTypes.FLETCHING);
                    MineTweakerAPI.logCommand(String.format("<material:%s>.accuracyFletching = %f;",
                            identifier,
                            statsFletching.accuracy
                    ));
                    MineTweakerAPI.logCommand(String.format("<material:%s>.modifierFletching = %f;",
                            identifier,
                            statsFletching.modifier
                    ));
                }

            });
        });

        return logs;
    }
}