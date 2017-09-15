package forestry_old.recipes;

import com.google.common.collect.ImmutableMap;
import forestry.api.recipes.ICentrifugeRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.*;

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
	public NonNullList<ItemStack> getProducts(Random random) {
		NonNullList<ItemStack> products = NonNullList.create();

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
