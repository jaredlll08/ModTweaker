package modtweaker.mods.extrautils.handlers;

import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import com.rwtema.extrautils2.tile.TileResonator;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

import java.util.*;

@ZenClass("mods.extrautils.Resonator")
public class Resonator {
	
	
	@ZenMethod
	public static void add(IItemStack output, IItemStack input, int energy, boolean addOwnerTag) {
		MineTweakerAPI.apply(new Add(new TileResonator.ResonatorRecipe(InputHelper.toStack(input), InputHelper.toStack(output), energy, addOwnerTag)));
	}
	
	@ZenMethod
	public static void remove(IItemStack output) {
		List<TileResonator.ResonatorRecipe> removed = new ArrayList<>();
		TileResonator.resonatorRecipes.forEach(i -> {
			if(i.output.isItemEqual(InputHelper.toStack(output))) {
				removed.add(i);
			}
		});
		
		if(!removed.isEmpty()) {
			MineTweakerAPI.apply(new Remove(removed));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", "Resonator", output.toString()));
		}
	}
	
	
	private static class Add extends BaseListAddition<TileResonator.ResonatorRecipe> {
		
		protected Add(TileResonator.ResonatorRecipe recipe) {
			super("Resonator", TileResonator.resonatorRecipes, Arrays.asList(recipe));
		}
		
		@Override
		public void apply() {
			super.apply();
			
			successful.forEach(i -> {
				if(i.input.getItemDamage() != 32767 && i.input.getHasSubtypes()) {
					TileResonator.ResonatorRecipe.SpecificItems.add(new ItemStack(i.input.getItem(), 1, i.input.getItemDamage()));
				} else {
					TileResonator.ResonatorRecipe.WildCardItems.add(i.input.getItem());
				}
			});
		}
		
		@Override
		public void undo() {
			super.undo();
			successful.forEach(i -> {
				if(i.input.getItemDamage() != 32767 && i.input.getHasSubtypes()) {
					TileResonator.ResonatorRecipe.SpecificItems.remove(new ItemStack(i.input.getItem(), 1, i.input.getItemDamage()));
				} else {
					TileResonator.ResonatorRecipe.WildCardItems.remove(i.input.getItem());
				}
			});
		}
		
		@Override
		protected String getRecipeInfo(TileResonator.ResonatorRecipe recipe) {
			return recipe.toString();
		}
	}
	
	private static class Remove extends BaseListRemoval<TileResonator.ResonatorRecipe> {
		
		protected Remove(List<TileResonator.ResonatorRecipe> recipes) {
			super("Resonator", TileResonator.resonatorRecipes, recipes);
		}
		
		@Override
		public void apply() {
			super.apply();
			successful.forEach(i -> {
				if(i.input.getItemDamage() != 32767 && i.input.getHasSubtypes()) {
					TileResonator.ResonatorRecipe.SpecificItems.remove(new ItemStack(i.input.getItem(), 1, i.input.getItemDamage()));
				} else {
					TileResonator.ResonatorRecipe.WildCardItems.remove(i.input.getItem());
				}
			});
		}
		
		@Override
		public void undo() {
			super.undo();
			
			successful.forEach(i -> {
				if(i.input.getItemDamage() != 32767 && i.input.getHasSubtypes()) {
					TileResonator.ResonatorRecipe.SpecificItems.add(new ItemStack(i.input.getItem(), 1, i.input.getItemDamage()));
				} else {
					TileResonator.ResonatorRecipe.WildCardItems.add(i.input.getItem());
				}
			});
		}
		
		@Override
		protected String getRecipeInfo(TileResonator.ResonatorRecipe recipe) {
			return recipe.toString();
		}
	}
}
