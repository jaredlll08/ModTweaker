package modtweaker2.mods.thaumcraft.aspect;

import java.util.List;

import minetweaker.IBracketHandler;
import minetweaker.MineTweakerAPI;
import minetweaker.annotations.BracketHandler;
import minetweaker.api.item.IngredientAny;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.util.ZenPosition;
import thaumcraft.api.aspects.Aspect;

@BracketHandler(priority = 100)
public class AspectBracketHandler implements IBracketHandler {

    private final IZenSymbol symbolAny;
    private final IJavaMethod method;
    
    public AspectBracketHandler() {
        this.symbolAny = MineTweakerAPI.getJavaStaticFieldSymbol(IngredientAny.class, "INSTANCE");
        this.method = MineTweakerAPI.getJavaMethod(AspectBracketHandler.class, "getAspect", String.class);
    }
    
    public static IAspectStack getAspect(String name) {
        Aspect aspect = Aspect.getAspect(name.toLowerCase());
        if (aspect != null) {
            return new MCAspectStack(new AspectStack(aspect, 1));
        } else {
            return null;
        }
    }
    
    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        if (tokens.size() == 1 && tokens.get(0).getValue().equals("*")) {
            return symbolAny;
        }
        
        if (tokens.size() > 2) {
            if (tokens.get(0).getValue().equals("aspect") && tokens.get(1).getValue().equals(":")) {
                return find(environment, tokens, 2, tokens.size());
            }
        }

        return null;
    }

    private IZenSymbol find(IEnvironmentGlobal environment, List<Token> tokens, int startIndex, int endIndex) {
        StringBuilder value = new StringBuilder();

        for (int i = startIndex; i < endIndex; i++) {
            Token token = tokens.get(i);
            value.append(token.getValue());
        }

        Aspect aspect = Aspect.getAspect(value.toString().toLowerCase());
        
        if(aspect != null) {
            return new AspectReferenceSymbol(environment, value.toString());
        }

        return null;
    }

    private class AspectReferenceSymbol implements IZenSymbol {
        private final IEnvironmentGlobal environment;
        private final String name;

        public AspectReferenceSymbol(IEnvironmentGlobal environment, String name) {
            this.environment = environment;
            this.name = name;
        }

        @Override
        public IPartialExpression instance(ZenPosition position) {
            return new ExpressionCallStatic(position, environment, method, new ExpressionString(position, name));
        }
    }
}
