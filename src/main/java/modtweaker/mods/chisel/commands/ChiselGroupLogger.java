package modtweaker.mods.chisel.commands;

import com.blamejared.mtlib.commands.CommandLogger;
import team.chisel.api.carving.CarvingUtils;

import java.util.Collection;


public class ChiselGroupLogger extends CommandLogger {

    @Override
    public Collection<? extends String> getList() {
        return CarvingUtils.getChiselRegistry().getSortedGroupNames();
    }

    @Override
    public String getName() {
        return "Chisel Groups";
    }
}
