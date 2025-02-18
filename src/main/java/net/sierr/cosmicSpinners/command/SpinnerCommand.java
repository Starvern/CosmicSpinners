package net.sierr.cosmicSpinners.command;

import net.sierr.cosmicSpinners.CosmicSpinners;
import net.sierr.cosmicSpinners.MessageKey;
import net.sierr.cosmicSpinners.Spinner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpinnerCommand implements CommandExecutor
{
    private final CosmicSpinners api;

    public SpinnerCommand(CosmicSpinners api)
    {
        this.api = api;
    }

    public void register(String commandName)
    {
        PluginCommand command = this.api.getPlugin().getCommand(commandName);
        if (command == null)
        {
            this.api.getLogger().severe("Invalid plugin.yml. Please re-install the plugin.");
            return;
        }
        command.setExecutor(this);
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String @NotNull [] args
    ) {

        if (!commandSender.hasPermission("cosmicspinners.command"))
        {
            MessageKey.INSUFFICIENT_PERMISSION.sendComponent(this.api, commandSender);
            return false;
        }

        if (args.length < 1)
        {
            MessageKey.SPECIFY_SPINNER.sendComponent(this.api, commandSender);
            return false;
        }

        if (args[0].equalsIgnoreCase("reload"))
        {
            if (!commandSender.hasPermission("cosmicspinners.command.reload"))
            {
                MessageKey.INSUFFICIENT_PERMISSION.sendComponent(this.api, commandSender);
                return false;
            }

            MessageKey.PLUGIN_RELOADED.sendComponent(this.api, commandSender);
            this.api.getPlugin().initializeFiles();
            this.api.loadSpinners();
            return true;
        }

        if (!(commandSender instanceof Player player))
        {
            MessageKey.NOT_PLAYER.sendComponent(this.api, commandSender);
            return false;
        }

        if (args[0].equalsIgnoreCase("deselect"))
        {
            player.getPersistentDataContainer().remove(this.api.getSelectedSpinnerKey());
            MessageKey.SPINNER_DESELECTED.sendComponent(this.api, player);
            return true;
        }

        @Nullable Spinner spinner = this.api.getSpinner(args[0]);

        if (spinner == null)
        {
            MessageKey.UNKNOWN_SPINNER.sendComponent(this.api, commandSender);
            return false;
        }

        if (!player.hasPermission(spinner.getPermission()))
        {
            MessageKey.INSUFFICIENT_PERMISSION.sendComponent(this.api, player);
            return false;
        }

        this.api.setSpinner(player, spinner);
        MessageKey.SPINNER_SELECTED.sendComponent(this.api, player);

        return true;
    }
}
