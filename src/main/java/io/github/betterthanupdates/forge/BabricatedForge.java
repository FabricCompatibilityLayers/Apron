package io.github.betterthanupdates.forge;

import forge.MinecraftForge;
import modloader.ModLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BabricatedForge {
    // FIXME: Downloads should not be used! At the very least, they should not be hardcoded!
    protected static final String MODLOADER_URL = "https://download770.mediafire.com/0th8lmgk23ug/vb5bq3aoitdkgx8/ModLoader+B1.7.3.zip";
    protected static final String MODLOADERMP_CLIENT_URL = "https://download1336.mediafire.com/bubjlfr7xv3g/j60zj1ljdtv03g7/ModLoaderMp+1.7.3+Unofficial+v2.zip";
    protected static final String FORGE_CLIENT_URL = "https://netcologne.dl.sourceforge.net/project/buildcraft/Forge%201.0.7%2020110907/minecraftforge-client-1.0.7-20110907.zip";
    protected static final String MODLOADERMP_SERVER_URL = "https://download1586.mediafire.com/i1okrhztqcqg/ivb74fttuebrj7k/ModLoaderMp+1.7.3+Unofficial+Server+v2.zip";
    protected static final String FORGE_SERVER_URL = "https://master.dl.sourceforge.net/project/buildcraft/Forge%201.0.7%2020110907/minecraftforge-server-1.0.7-20110907.zip?viasf=1";

    // Logging
    public static final Logger LOGGER = LogManager.getLogger(BabricatedForge.class);
    public static final java.util.logging.Logger MOD_LOADER_LOGGER = ModLoader.getLogger();
    public static final Logger FORGE_LOGGER = MinecraftForge.LOGGER;
}
