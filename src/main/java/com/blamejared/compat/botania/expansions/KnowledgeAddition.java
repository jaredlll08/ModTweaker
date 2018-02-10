package com.blamejared.compat.botania.expansions;


import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.common.item.ItemLexicon;

@ZenExpansion("crafttweaker.item.IItemstack")
@ModOnly("botania")
@ZenRegister
public class KnowledgeAddition {
    @ZenMethod
    public void addBotaniaKnowledge(IItemStack stack, String knowledge){
        ILexicon lexicon = (ILexicon) stack;
        ItemStack mcItemStack = CraftTweakerMC.getItemStack(stack);
        if (stack == null || stack.isEmpty()){
            if (stack instanceof ItemLexicon){
                if (BotaniaAPI.knowledgeTypes.containsKey(knowledge)){
                KnowledgeType knowledgeType = BotaniaAPI.knowledgeTypes.get(knowledge);
                    if (!lexicon.isKnowledgeUnlocked(mcItemStack, knowledgeType)){
                        ((ItemLexicon) stack).unlockKnowledge(mcItemStack, knowledgeType);
                    }
                } else {
                    CraftTweakerAPI.logError("Provided String is not a valid Knowledge Type");
                }
            } else {
                CraftTweakerAPI.logError("Only Works With The Botania Lexicon");
            }
        }
    }
}