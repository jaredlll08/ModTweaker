package com.blamejared.compat.bloodmagic;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IngredientOr;
import crafttweaker.zenscript.IBracketHandler;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.*;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;
import net.minecraft.item.crafting.Ingredient;
import com.blamejared.compat.bloodmagic.Orbs;

import java.util.*;

@BracketHandler(priority = 100)
@ZenRegister
@ModOnly("bloodmagic")
public class BracketHandlerOrb implements IBracketHandler {
    
    private final IJavaMethod method;

    public BracketHandlerOrb() {
        method = CraftTweakerAPI.getJavaMethod(BracketHandlerOrb.class, "getOrb", String.class);
    }

    public static IIngredient getOrb(String name) {
        return Orbs._getOrb(name);
    }

    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        
        int fromIndex = 0;
        int toIndex = tokens.size();

        if(tokens.size() > 2) {
            if(tokens.get(0).getValue().equals("orb") && tokens.get(1).getValue().equals(":")) {
                fromIndex = 2;
            }
        }
        
        return find(environment, tokens, fromIndex, toIndex);
    }

    private IZenSymbol find(IEnvironmentGlobal environment, List<Token> tokens, int startIndex, int endIndex) {
        StringBuilder valueBuilder = new StringBuilder();

        for(int i = startIndex; i < endIndex; i++) {
            Token token = tokens.get(i);
            valueBuilder.append(token.getValue());
        }

        String name = valueBuilder.toString();

        if (Orbs.orbExists(name)) {
            return position -> new ExpressionCallStatic(position, environment, method, new ExpressionString(position, name));
        }

        return null;
    }
}
