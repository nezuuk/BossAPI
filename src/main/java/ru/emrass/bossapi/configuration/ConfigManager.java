package ru.emrass.bossapi.configuration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.emrass.bossapi.BossApiCore;


public class ConfigManager {

    private final BossApiCore bossApiCore = BossApiCore.getInstance();

    private File localizationConfigFile;


    private FileConfiguration localizationConfig;

    public void init(File dataFolder) {
        this.localizationConfigFile = new File(dataFolder + File.separator + "localization.yml");
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            if (!localizationConfigFile.exists()) {
                localizationConfigFile.createNewFile();
                copyDefaults(new File("localization.yml"), localizationConfigFile);
            }

            load();
            save();

            bossApiCore.getLogger().info("&aConfigs loaded!");
        } catch (Exception e) {
            e.printStackTrace();
            bossApiCore.getLogger().info("&cFailed to create configuration!");
        }
    }

    public void load() {
        localizationConfig = YamlConfiguration.loadConfiguration(localizationConfigFile);
    }

    public void save() {
        try {
            localizationConfig.save(localizationConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
            bossApiCore.getLogger().info("&cFailed to save configuration!");
        }
    }

    @SneakyThrows
    public void reloadConfig(File file, FileConfiguration config) {
        config.load(file);
    }
    public FileConfiguration getLocalizationConfig() {
        return localizationConfig;
    }


    public void copyDefaults(File filePath, File out) throws Exception {
        Files.copy(bossApiCore.getResource(filePath.getPath()), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

}
