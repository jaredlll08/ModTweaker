package modtweaker.mods.tconstruct.commands;

import com.blamejared.mtlib.commands.CommandLogger;

import java.util.Collection;

import static modtweaker.mods.tconstruct.TConstructHelper.modifiers;

public class ModifierLogger extends CommandLogger {

    @Override
    public Collection<? extends String> getList() {
        return modifiers.keySet();
    }

    @Override
    public String getName() {
        return "Tinker's Construct modifiers";
    }
}
