package game;

import game.ui.Menu;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ModLoader {

    public static List<Mod> mods = new ArrayList<>();
    private static final String MODS_FOLDER = "mods";
    private static final Logger logger = new Logger(ModLoader.class.getName());

    public static void loadMods(Game game) {
        File modsDir = new File(MODS_FOLDER);
        if (!modsDir.exists() || !modsDir.isDirectory()) {
            logger.Log("Mods directory not found.");
            return;
        }

        File[] jarFiles = modsDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().endsWith(".jar");
            }
        });

        if (jarFiles == null || jarFiles.length == 0) {
            logger.Log("No mod JAR files found.");
            return;
        }


        for (File jarFile : jarFiles) {
            try {
                URL[] urls = {jarFile.toURI().toURL()};
                URLClassLoader classLoader = new URLClassLoader(urls, ModLoader.class.getClassLoader());

                // Load the JAR file
                JarFile jar = new JarFile(jarFile);
                Enumeration<? extends ZipEntry> entries = jar.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {
                        String className = entry.getName().replace("/", ".").replace(".class", "");
                        Class<?> cls = classLoader.loadClass(className);

                        // Check if the class implements the Mod interface and is not abstract or an interface
                        if (Mod.class.isAssignableFrom(cls) && !cls.isInterface() && !java.lang.reflect.Modifier.isAbstract(cls.getModifiers())) {
                            Mod modInstance = (Mod) cls.getDeclaredConstructor().newInstance();
                            mods.add(modInstance);
                            logger.Log("Loaded mod: " + className);
                        }
                    }
                }

                jar.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Initialize all loaded mods with the game instance
        for (Mod mod : mods) {
            mod.init();
            for (Menu menu : mod.getMenus())
            {
                Game.screen.addMouseListener(menu);
                Game.screen.addMouseMotionListener(menu);
            }
        }
    }
}
