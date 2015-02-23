package modtweaker2.mods.auracascade.aura;

import java.util.List;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import minetweaker.IBracketHandler;
import minetweaker.MineTweakerAPI;
import minetweaker.annotations.BracketHandler;
import minetweaker.api.item.IngredientAny;
import pixlepix.auracascade.data.AuraQuantity;
import pixlepix.auracascade.data.EnumAura;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.util.ZenPosition;

@BracketHandler(priority = 100)
public class AuraBracketHandler implements IBracketHandler {

	public static IAuraStack getAura(String name) {
		EnumAura aura = EnumAura.valueOf(name);
		if (aura != null) {
			return new MCAuraStack(new AuraQuantity(aura, 1));
		} else {
			return null;
		}
	}

	private final IZenSymbol symbolAny;
	private final IJavaMethod method;

	public AuraBracketHandler() {
		symbolAny = MineTweakerAPI.getJavaStaticFieldSymbol(IngredientAny.class, "INSTANCE");
		method = MineTweakerAPI.getJavaMethod(AuraBracketHandler.class, "getAura", String.class);
	}

	@Override
	public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
		// any symbol
		if (tokens.size() == 1 && tokens.get(0).getValue().equals("*")) {
			return symbolAny;
		}

		if (tokens.size() > 2) {
			if (tokens.get(0).getValue().equals("aura") && tokens.get(1).getValue().equals(":")) {
				return find(environment, tokens, 2, tokens.size());
			}
		}

		return null;
	}

	private IZenSymbol find(IEnvironmentGlobal environment, List<Token> tokens, int startIndex, int endIndex) {
		StringBuilder valueBuilder = new StringBuilder();
//		}
		for (EnumAura a : EnumAura.values()) {
			for (int i = startIndex; i < endIndex; i++) {
				Token token = tokens.get(i);
				if (a.name().equals(token.getValue())) {
					return new AuraReferenceSymbol(environment, token.getValue());
				}
			}
		}

		return null;
	}

	private class AuraReferenceSymbol implements IZenSymbol {
		private final IEnvironmentGlobal environment;
		private final String name;

		public AuraReferenceSymbol(IEnvironmentGlobal environment, String name) {
			this.environment = environment;
			this.name = name;
		}

		@Override
		public IPartialExpression instance(ZenPosition position) {
			return new ExpressionCallStatic(position, environment, method, new ExpressionString(position, name));
		}
	}
}