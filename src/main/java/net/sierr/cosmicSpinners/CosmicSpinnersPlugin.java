package net.sierr.cosmicSpinners;

import net.sierr.cosmicSpinners.command.SpinnerCommand;
import net.sierr.cosmicSpinners.task.SpinnerTask;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

public class CosmicSpinnersPlugin extends JavaPlugin
{
    private CosmicSpinners api;
    private BukkitTask timerTask;

    private FileConfiguration messageConfig;
    private FileConfiguration spinnerConfig;

    @Override
    public void onEnable()
    {
        this.initializeFiles();

        this.api = new CosmicSpinners(this);
        this.api.loadSpinners();

        this.timerTask = new SpinnerTask(this.api).runTaskTimerAsynchronously(this, 0, 10);

        new SpinnerCommand(this.api).register("spinner");
    }

    public void initializeFiles()
    {
        File messageFile = new File(this.getDataFolder(), "messages.yml");
        File spinnerFile = new File(this.getDataFolder(), "spinners.yml");

        if (!messageFile.exists() || !spinnerFile.exists())
        {
            this.saveResource(messageFile.getName(), false);
            this.saveResource(spinnerFile.getName(), false);
        }

        this.messageConfig = YamlConfiguration.loadConfiguration(messageFile);
        this.spinnerConfig = YamlConfiguration.loadConfiguration(spinnerFile);
    }

    public CosmicSpinners getApi()
    {
        return this.api;
    }

    @Override
    public void onDisable()
    {
        this.timerTask.cancel();
        this.api.onDisable();
        this.api = null;
    }

    public FileConfiguration getMessageConfig()
    {
        return this.messageConfig;
    }

    public FileConfiguration getSpinnerConfig()
    {
        return this.spinnerConfig;
    }
}
