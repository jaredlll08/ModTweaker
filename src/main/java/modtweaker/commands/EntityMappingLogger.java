package modtweaker.commands;

import com.blamejared.mtlib.commands.CommandLogger;
import net.minecraft.entity.EntityList;

import java.util.Collection;

public class EntityMappingLogger extends CommandLogger {

    @Override
    public Collection<? extends String> getList() {
        return EntityList.NAME_TO_CLASS.keySet();
    }

    @Override
    public String getName() {
        return "Mob Keys";
    }
}
