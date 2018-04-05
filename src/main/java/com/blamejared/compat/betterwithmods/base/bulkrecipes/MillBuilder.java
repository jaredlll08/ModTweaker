package com.blamejared.compat.betterwithmods.base.bulkrecipes;

import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.function.Supplier;

public class MillBuilder extends BulkRecipeBuilder<MillRecipe> {

    private String sound;

    public MillBuilder(Supplier<CraftingManagerBulk<MillRecipe>> registry, String name) {
        super(registry, name);
    }

    @ZenMethod
    @Override
    public void build() {
        addRecipe(new MillRecipe(inputs, outputs).setSound(sound).setPriority(priority));
    }

    @ZenMethod
    public MillBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    @ZenMethod
    public MillBuilder setGrindType(String sound) {
        this.sound = sound;
        return this;
    }

    @ZenMethod
    public MillBuilder buildRecipe(IIngredient[] inputs, IItemStack[] outputs) {
        _buildRecipe(inputs,outputs);
        return this;
    }


}
