package com.blamejared;

import com.blamejared.api.annotations.*;
import minetweaker.MineTweakerAPI;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import static com.blamejared.reference.Reference.*;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public class ModTweaker {
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) throws IOException {
        File output = new File("output.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        
        e.getAsmData().getAll(Handler.class.getCanonicalName()).forEach(data -> {
            try {
                Class clazz = Class.forName(data.getClassName());
                Handler handl = (Handler) clazz.getAnnotation(Handler.class);
                if(Loader.isModLoaded(handl.value())) {
                    MineTweakerAPI.registerClass(clazz);
                    boolean document = false;
                    for(Method method : clazz.getDeclaredMethods()) {
                        if(method.isAnnotationPresent(Document.class)) {
                            document = true;
                        }
                    }
                    if(document) {
                        ZenClass zen = (ZenClass) clazz.getAnnotation(ZenClass.class);
                        writer.write("# " + handl.value() + "\n");
                        writer.write("## " + zen.value() + "\n\n");
                        
                        for(Method method : clazz.getDeclaredMethods()) {
                            if(method.isAnnotationPresent(Document.class)) {
                                StringBuilder builder = new StringBuilder();
                                
                                Document doc = method.getAnnotation(Document.class);
                                Map<String, Parameter> paramMap = new LinkedHashMap<>();
                                for(int i = 0; i < method.getParameters().length; i++) {
                                    paramMap.put(doc.value()[i], method.getParameters()[i]);
                                }
                                builder.append("//");
                                paramMap.forEach((key, val) -> {
                                    if(val.isAnnotationPresent(Optional.class)) {
                                        builder.append("<span style=\"color:red\">");
                                    }
                                    builder.append(val.getType().getSimpleName()).append(" ").append(key);
                                    if(val.isAnnotationPresent(Optional.class)) {
                                        builder.append("</span>");
                                    }
                                    builder.append(", ");
                                });
                                builder.reverse().delete(0, 2).reverse();
                                builder.append("\n```");
                                builder.append(zen.value()).append(".");
                                builder.append(method.getName()).append("(");
                                paramMap.forEach((key, val) -> {
                                    builder.append(val.getType().getSimpleName()).append(" ").append(key).append(", ");
                                });
                                builder.reverse().delete(0, 2).reverse();
                                builder.append(");");
                                builder.append("```\n");
                                writer.write(builder.toString());
                            }
                        }
                    }
                }
            } catch(ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
            }
        });
        writer.close();
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    
    }
}
