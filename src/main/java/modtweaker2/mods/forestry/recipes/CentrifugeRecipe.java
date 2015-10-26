package modtweaker2.mods.forestry.recipes;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.ItemStack;

import forestry.api.recipes.ICentrifugeRecipe;

public class CentrifugeRecipe implements ICentrifugeRecipe {

	private final int processingTime;
	private final ItemStack input;
	private final Map<ItemStack, Float> outputs;

	public CentrifugeRecipe(int processingTime, ItemStack input, Map<ItemStack, Float> outputs) {
		this.processingTime = processingTime;
		this.input = input;
		this.outputs = outputs;

		for (ItemStack item : outputs.keySet()) {
			if (item == null) {
				throw new IllegalArgumentException("Tried to register a null product of " + input);
			}
		}
	}

	@Override
	public ItemStack getInput() {
		return input;
	}

	@Override
	public int getProcessingTime() {
		return processingTime;
	}

	@Override
	public Collection<ItemStack> getProducts(Random random) {
		List<ItemStack> products = new ArrayList<ItemStack>();

		for (Map.Entry<ItemStack, Float> entry : this.outputs.entrySet()) {
			float probability = entry.getValue();

			if (probability >= 1.0) {
				products.add(entry.getKey().copy());
			} else if (random.nextFloat() < probability) {
				products.add(entry.getKey().copy());
			}
		}

		return products;
	}

	@Override
	public Map<ItemStack, Float> getAllProducts() {
		return ImmutableMap.copyOf(outputs);
	}
}
