package com.blamejared.compat.botania.expansion;


import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;
import stanhebben.zenscript.parser.expression.ParsedExpression;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.*;
import vazkii.botania.common.item.ItemLexicon;

@ZenExpansion("crafttweaker.item.IItemStack")
@ModOnly("botania")
@ZenRegister
public class KnowledgeAddition {
    
    @ZenMethod
    public static void addBotaniaKnowledge(IItemStack stack, String knowledge) {
        ItemStack mcItemStack = CraftTweakerMC.getItemStack(stack);
        if(!mcItemStack.isEmpty()) {
            if(mcItemStack.getItem() instanceof ItemLexicon) {
                ILexicon lexicon = (ILexicon) mcItemStack.getItem();
                if(BotaniaAPI.knowledgeTypes.containsKey(knowledge)) {
                    KnowledgeType knowledgeType = BotaniaAPI.knowledgeTypes.get(knowledge);
                    if(!lexicon.isKnowledgeUnlocked(mcItemStack, knowledgeType)) {
                        ((ItemLexicon) mcItemStack.getItem()).unlockKnowledge(mcItemStack, knowledgeType);
                    }
                } else {
                    CraftTweakerAPI.logError("Invalid knowledge type!");
                }
            } else {
                CraftTweakerAPI.logError("Invalid item! Knowledge can only be applied to the Botania Lexicon!");
            }
        }
    }
}