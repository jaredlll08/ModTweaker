package modtweaker.mods.chisel.commands;

import com.blamejared.mtlib.commands.CommandLogger;
import net.minecraft.item.Item;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ChiselVariationLogger extends CommandLogger {

    @Override
    public Collection<? extends String> getList() {
        List<String> var = new LinkedList<>();
        for(String s : CarvingUtils.getChiselRegistry().getSortedGroupNames()) {
            for(ICarvingVariation variation : CarvingUtils.getChiselRegistry().getGroup(s).getVariations()) {
                String stringedVariation = "<" + Item.REGISTRY.getNameForObject(Item.getItemFromBlock(variation.getBlock())) + ":" + variation.getStack().getItemDamage() + ">";
                stringedVariation += " " + s;
                System.out.println("Chisel Variation " + stringedVariation);
                var.add(stringedVariation);
            }
        }
        return var;
    }

    @Override
    public String getName() {
        return "Chisel Variations";
    }
}
