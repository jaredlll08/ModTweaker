package com.blamejared.compat.thaumcraft.handlers.brackets;

import com.blamejared.compat.thaumcraft.handlers.aspects.*;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.zenscript.IBracketHandler;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.*;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;
import thaumcraft.api.aspects.Aspect;

import java.util.*;

@BracketHandler(priority = 100)
@ZenRegister
@ModOnly("thaumcraft")
public class BracketHandlerAspect implements IBracketHandler {
    
    private static final Map<String, Aspect> aspects = new HashMap<>();
    
    private final IJavaMethod method;
    
    public BracketHandlerAspect() {
        method = CraftTweakerAPI.getJavaMethod(BracketHandlerAspect.class, "getAspect", String.class);
    }
    
    
    @SuppressWarnings("unchecked")
    public static void rebuildRegistry() {
        aspects.clear();
        Aspect.aspects.forEach(aspects::put);
        
    }
    
    public static CTAspectStack getAspect(String name) {
        return new CTAspectStack(new CTAspect(Aspect.aspects.get(name)), 1);
    }
    
    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        
        int fromIndex = 0;
        int toIndex = tokens.size();
        
        if(tokens.size() > 2) {
            if(tokens.get(0).getValue().equals("aspect") && tokens.get(1).getValue().equals(":")) {
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
        if(!aspects.containsKey(name)) {
            rebuildRegistry();
        }
        if(aspects.containsKey(name)) {
            return position -> new ExpressionCallStatic(position, environment, method, new ExpressionString(position, valueBuilder.toString()));
        }
        
        return null;
    }
}