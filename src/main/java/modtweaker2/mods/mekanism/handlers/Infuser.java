//package modtweaker2.mods.mekanism.handlers;
//
//import static modtweaker2.helpers.InputHelper.toStack;
//import static modtweaker2.helpers.StackHelper.areEqual;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import mekanism.api.infuse.InfuseRegistry;
//import mekanism.common.recipe.RecipeHandler.Recipe;
//import mekanism.common.recipe.inputs.InfusionInput;
//import mekanism.common.recipe.machines.MetallurgicInfuserRecipe;
//import mekanism.common.recipe.outputs.MachineOutput;
//import minetweaker.MineTweakerAPI;
//import minetweaker.api.item.IItemStack;
//import modtweaker2.mods.mekanism.Mekanism;
//import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
//import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
//import stanhebben.zenscript.annotations.ZenClass;
//import stanhebben.zenscript.annotations.ZenMethod;
//
//@ZenClass("mods.mekanism.Infuser")
//public class Infuser {
//	@ZenMethod
//	public static void addRecipe(String type, int infuse, IItemStack input, IItemStack output) {
//        if (Mekanism.v7)
//        {
//            InfusionInput infuseIn = new InfusionInput(InfuseRegistry.get(type), infuse, toStack(input));
//            InfusionOutput infuseOut = new InfusionOutput(infuseIn, toStack(output));
//            
//            MineTweakerAPI.apply(new AddMekanismRecipe("METALLURGIC_INFUSER", Recipe.METALLURGIC_INFUSER.get(), infuseIn, infuseOut));
//        }
//        else
//        {
//            MetallurgicInfuserRecipe recipe = new MetallurgicInfuserRecipe(new mekanism.common.recipe.inputs.InfusionInput(InfuseRegistry.get(type), infuse, toStack(input)), toStack(output));
//            MineTweakerAPI.apply(new AddMekanismRecipe("METALLURGIC_INFUSER", Recipe.METALLURGIC_INFUSER.get(), recipe.getInput(), recipe));
//        }
//	}
//
//	@ZenMethod
//	public static void removeRecipe(IItemStack output) {
//        if (Mekanism.v7)
//        {
//            MineTweakerAPI.apply(new Remove("METALLURGIC_INFUSER", Recipe.METALLURGIC_INFUSER.get(), new InfusionOutput(null, toStack(output))));
//        }
//        else
//        {
//            throw new UnsupportedOperationException("Function not added to v8 compatibility yet");
//        }
//	}
//
//	private static class Remove extends RemoveMekanismRecipe {
//		public Remove(String string, Map map, Object key) {
//			super(string, map, key);
//		}
//        private List removed = new ArrayList();
//		@Override
//		public void apply() {
//			Iterator it = map.entrySet().iterator();
//			while (it.hasNext()) {
//                Map.Entry pairs = (Map.Entry)it.next();
//                InfusionInput key = (InfusionInput)pairs.getKey();
//                InfusionOutput value = (InfusionOutput)pairs.getValue();
//                if (key != null)
//                {
//                    if (this.key instanceof InfusionOutput && areEqual(value.resource, ((InfusionOutput)this.key).resource))
//                    {
//                        this.key = key;
//                        removed.add(value);
//                        it.remove();
//                    }
//                }
//			}
//		}
//
//        @Override
//        public void undo()
//        {
//            for (Object recipe : removed)
//            {
//                map.put(key, recipe);
//            }
//        }
//    }
//}
