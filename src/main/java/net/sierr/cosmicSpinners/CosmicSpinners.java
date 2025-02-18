package net.sierr.cosmicSpinners;

import net.sierr.cosmicSpinners.macros.SpinnerMacro;
import net.sierr.cosmicSpinners.placeholder.SpinnerPlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class CosmicSpinners
{
    private final CosmicSpinnersPlugin plugin;
    private final Set<Spinner> registeredSpinners;
    private final NamespacedKey selectedSpinnerKey;
    private final Logger logger;
    private UUI uuiApi;
    private SpinnerMacro spinnerMacro;

    public CosmicSpinners(CosmicSpinnersPlugin plugin)
    {
        this.plugin = plugin;
        this.registeredSpinners = new HashSet<>();
        this.selectedSpinnerKey = new NamespacedKey(this.plugin, "selected_spinner");
        this.logger = Logger.getLogger("CosmicSpinnersAPI");

        RegisteredServiceProvider<UUI> provider = Bukkit.getServicesManager().getRegistration(UUI.class);

        if (provider != null)
        {
            this.uuiApi = provider.getProvider();
            this.spinnerMacro = new SpinnerMacro(this);
            this.spinnerMacro.register();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
        {
            new SpinnerPlaceholderExpansion(this).register();
        }
    }

    public void onDisable()
    {
        if (this.uuiApi != null)
            this.uuiApi.getMacroManager().removeMacro(this.spinnerMacro);
    }

    /**
     * @return The API's logger.
     * @since 0.1.0
     */
    public Logger getLogger()
    {
        return this.logger;
    }

    /**
     * @return The instance of UUI, or null if the plugin is not enabled.
     * @since 0.1.0
     */
    public @Nullable UUI getUuiApi()
    {
        return this.uuiApi;
    }

    /**
     * Loads all spinners from spinners.yml.
     * @since 0.1.0
     */
    public void loadSpinners()
    {
        this.registeredSpinners.clear();

        for (String key : this.plugin.getSpinnerConfig().getKeys(false))
        {
            ConfigurationSection section = this.plugin.getSpinnerConfig().getConfigurationSection(key);
            if (section == null) continue;

            Color color = Color.fromARGB(
                    section.getInt("color.alpha"),
                    section.getInt("color.red"),
                    section.getInt("color.green"),
                    section.getInt("color.blue")
            );

            Spinner spinner = new Spinner(
                    this,
                    key,
                    section.getString("permission"),
                    color,
                    section.getDouble("radius"),
                    section.getDouble("speed"),
                    section.getDouble("amount"),
                    (float) section.getDouble("size"),
                    section.getString("material"),
                    section.getString("display_name"),
                    section.getString("description")
            );

            this.registeredSpinners.add(spinner);
        }
    }

    /**
     * @return The NameSpacedKey for holding selected spinner within Player.
     * @since 0.1.0
     */
    public NamespacedKey getSelectedSpinnerKey()
    {
        return this.selectedSpinnerKey;
    }

    /**
     * @return The instance of plugin.
     * @since 0.1.0
     */
    public CosmicSpinnersPlugin getPlugin()
    {
        return this.plugin;
    }

    /**
     * @param player The Player whose spinner to get.
     * @return The Player's equipped spinner, or null.
     * @since 0.1.0
     */
    public @Nullable Spinner getSpinner(Player player)
    {
        if (!player.getPersistentDataContainer().has(this.getSelectedSpinnerKey(), PersistentDataType.STRING))
            return null;

        String name = player.getPersistentDataContainer().get(
                this.getSelectedSpinnerKey(),
                PersistentDataType.STRING
        );

        return this.getSpinner(name);
    }

    /**
     * @param player The Player to set spinner for.
     * @param spinner The spinner to set.
     * @since 0.1.0
     */
    public void setSpinner(Player player, @Nullable Spinner spinner)
    {
        if (spinner == null || !player.hasPermission(spinner.getPermission())) return;

        player.getPersistentDataContainer().set(
                this.getSelectedSpinnerKey(),
                PersistentDataType.STRING,
                spinner.getName()
        );
    }

    /**
     * @return All registered spinners.
     * @since 0.1.0
     */
    public Set<Spinner> getRegisteredSpinners()
    {
        return this.registeredSpinners;
    }

    /**
     * @param name The name of the spinner to get.
     * @return The spinner with the given name, or null.
     * @since 0.1.0
     */
    public @Nullable Spinner getSpinner(String name)
    {
        for (Spinner spinner : this.registeredSpinners)
        {
            if (spinner.getName().equalsIgnoreCase(name))
                return spinner;
        }
        return null;
    }
}
