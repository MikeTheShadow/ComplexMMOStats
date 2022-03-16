package com.miketheshadow.complexmmostats.utils;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

public class LoaderTool {

    Plugin plugin;
    String packageName;
    Set<Class<?>> classes;
    boolean force = false;

    public LoaderTool(Plugin plugin, String packageName) {
        this.plugin = plugin;
        this.packageName = packageName;
        this.classes = collectAllClasses();
    }

    public LoaderTool(Plugin plugin, String packageName,boolean forceLoadAllClasses) {
        this.plugin = plugin;
        this.packageName = packageName;
        this.classes = collectAllClasses();
        this.force = forceLoadAllClasses;
    }

    public void defaultSetup() {
        try {
            registerListeners();
            registerCommands();
        } catch (Exception e) {
            plugin.getLogger().severe("Unable to register events with message: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void registerListeners() throws Exception {
        Set<Class<?>> clazzes = getListeners();
        PluginManager manager = Bukkit.getServer().getPluginManager();
        for(Class<?> clazz : clazzes) {
            manager.registerEvents((Listener) clazz.getDeclaredConstructor().newInstance(),plugin);
        }
    }

    public void registerCommands() throws Exception {

        Set<Class<?>> annotated =getClassesAnnotatedWith(CommandLoader.class);

        for(Class<?> clazz : annotated) {
            CommandLoader commandAnnotation = clazz.getAnnotation(CommandLoader.class);
            if(commandAnnotation == null) continue;
            String commandName = commandAnnotation.command();
            PluginCommand command = Bukkit.getServer().getPluginCommand(commandName);
            if(command == null) {
                throw new NotImplementedException("Missing plugin.yml registration for command: " + commandName);
            }
            command.setExecutor((CommandExecutor) clazz.getDeclaredConstructor().newInstance());
        }
    }

    public <A extends Annotation> Set<Class<?>> getClassesAnnotatedWith(Class<A> annotation) {
        if(!annotation.isAnnotation()) {
            throw new IllegalStateException("Class " + annotation.getName() + " is not an annotation!");
        }
        return classes.stream().filter(clazz -> clazz.getAnnotation(annotation) != null).collect(Collectors.toSet());
    }

    public Set<Class<?>> getListeners() {

        return classes.stream().filter(Listener.class::isAssignableFrom).collect(Collectors.toSet());
    }


    public Set<Class<?>> getClasses() {
        return classes;
    }

    private Set<Class<?>> collectAllClasses() {
        String searchName = packageName.replaceAll("[.]", "/");
        ClassLoader classLoader = plugin.getClass().getClassLoader();
        Set<Class<?>> classes = new HashSet<>();
        try {
            File currentFile = new File(LoaderTool.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            JarInputStream jarInputStream = new JarInputStream(new FileInputStream(currentFile));
            while (true) {
                ZipEntry entry = jarInputStream.getNextEntry();
                if (entry == null) break;
                String name = entry.getName();

                if(!force) {
                    String compare;
                    if(name.length() >= searchName.length()) {
                        compare = name.substring(0,searchName.length() - 1);
                    } else {
                        compare = name;
                    }
                    if(!searchName.contains(compare)) break;
                }

                if (name.contains(searchName) && name.endsWith(".class")) {
                    // Remove the .class wipe it's rear and replace the slashes with .s
                    classes.add(
                            classLoader.loadClass(name.replace(".class", "")
                                    .replaceAll("/", ".")));
                }
            }
            return classes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface CommandLoader {

        String command();

    }
}

