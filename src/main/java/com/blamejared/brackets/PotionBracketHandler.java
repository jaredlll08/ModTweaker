package com.blamejared.brackets;

import com.blamejared.api.potions.*;
import minetweaker.*;
import minetweaker.annotations.BracketHandler;
import minetweaker.api.item.IngredientAny;
import net.minecraft.potion.Potion;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.*;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.util.ZenPosition;

import java.util.*;

@BracketHandler(priority = 100)
public class PotionBracketHandler implements IBracketHandler {
    
    private static final Map<String, Potion> potionNames = new HashMap();
    private final IZenSymbol symbolAny = MineTweakerAPI.getJavaStaticFieldSymbol(IngredientAny.class, "INSTANCE");
    private final IJavaMethod method;
    
    public PotionBracketHandler() {
        this.method = MineTweakerAPI.getJavaMethod(PotionBracketHandler.class, "getPotion", String.class);
    }
    
    public static Map<String, Potion> getPotionNames() {
        return potionNames;
    }
    
    public static void rebuildRegistry() {
        potionNames.clear();
        Potion.REGISTRY.getKeys().forEach(key ->{
            potionNames.put(key.getResourcePath(), Potion.getPotionFromResourceLocation(key.toString()));
        });
    }
    
    public static IPotion getPotion(String name) {
        Potion pot = potionNames.get(name);
        if(pot == null){
            return null;
        }
        return new MCPotion(pot);
    }
    
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        return tokens.size() > 2 && tokens.get(0).getValue().equals("potion") && tokens.get(1).getValue().equals(":") ? this.find(environment, tokens, 2, tokens.size()) : null;
    }
    
    private IZenSymbol find(IEnvironmentGlobal environment, List<Token> tokens, int startIndex, int endIndex) {
        StringBuilder valueBuilder = new StringBuilder();
        
        for(int i = startIndex; i < endIndex; ++i) {
            Token token = tokens.get(i);
            valueBuilder.append(token.getValue());
        }
        
        return new PotionReferenceSymbol(environment, valueBuilder.toString());
    }
    
    private class PotionReferenceSymbol implements IZenSymbol {
        
        private final IEnvironmentGlobal environment;
        private final String name;
        
        public PotionReferenceSymbol(IEnvironmentGlobal environment, String name) {
            this.environment = environment;
            this.name = name;
        }
        
        public IPartialExpression instance(ZenPosition position) {
            return new ExpressionCallStatic(position, this.environment, PotionBracketHandler.this.method, new ExpressionString(position, this.name));
        }
    }
}