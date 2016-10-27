package modtweaker.mods.chisel;

import com.google.common.collect.Lists;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

public class ChiselHelper {

    public static ICarvingGroup getGroup(String name) {
        return CarvingUtils.getChiselRegistry().getGroup(name);
    }

    public static ICarvingGroup getGroup(IItemStack stack) {
        return CarvingUtils.getChiselRegistry().getGroup(toStack(stack));
    }

    public static ICarvingVariation getVariation(IItemStack stack) {
        ICarvingGroup g = getGroup(stack);
        if (g != null) {
            for (ICarvingVariation v : g.getVariations()) {
                if (v.getStack().isItemEqual(toStack(stack))) {
                    return v;
                }
            }
        }
        return null;
    }

    public static ICarvingVariation makeVariation(IItemStack stack) {
        return new CarvingVariation(Block.getBlockFromItem(toStack(stack).getItem()));
    }

    public static ICarvingGroup makeGroup(String name) {
        return new CarvingGroup(name);
    }

    public static boolean groupContainsVariation(ICarvingGroup group, ICarvingVariation variation) {
        for (ICarvingVariation otherVariation : group.getVariations()) {
            if (otherVariation.getStack().isItemEqual(variation.getStack())) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static class CarvingVariation implements ICarvingVariation {
        Block block;

        public CarvingVariation(Block block) {
            this.block = block;
        }

        @Override
        public Block getBlock() {
            return block;
        }

        @Override
        public IBlockState getBlockState() {
            return block.getDefaultState();
        }

        @Nonnull
        @Override
        public ItemStack getStack() {
            return new ItemStack(block);
        }

        @Override
        public int getOrder() {
            return 99;
        }
    }

    static class CarvingGroup implements ICarvingGroup {
        private String name;
        private String sound;
        private String oreName;

        private List<ICarvingVariation> variations = Lists.newArrayList();

        public CarvingGroup(String name) {
            this.name = name;
        }

        public List<ICarvingVariation> getVariations() {
            return Lists.newArrayList(variations);
        }

        @Override
        public void addVariation(ICarvingVariation variation) {
            variations.add(variation);
            Collections.sort(variations, new Comparator<ICarvingVariation>() {

                @Override
                public int compare(ICarvingVariation o1, ICarvingVariation o2) {
                    return CarvingUtils.compare(o1, o2);
                }
            });
        }

        @Override
        public boolean removeVariation(ICarvingVariation variation) {
            ICarvingVariation toRemove = null;
            for (ICarvingVariation v : variations) {
                if (v.getStack().isItemEqual(variation.getStack())) {
                    toRemove = v;
                }
            }
            return toRemove == null ? false : variations.remove(toRemove);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getSound() {
            return sound;
        }

        @Override
        public void setSound(String sound) {
            this.sound = sound;
        }

        @Override
        public String getOreName() {
            return oreName;
        }

        @Override
        public void setOreName(String oreName) {
            this.oreName = oreName;
        }
    }
}
