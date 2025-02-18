package net.sierr.cosmicSpinners.task;

import net.sierr.cosmicSpinners.CosmicSpinners;
import net.sierr.cosmicSpinners.Spinner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpinnerTask extends BukkitRunnable
{
    private final CosmicSpinners api;
    private final Map<UUID, Location> movementMap;

    public SpinnerTask(CosmicSpinners api)
    {
        this.api = api;
        this.movementMap = new HashMap<>();
    }

    @Override
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (!player.getPersistentDataContainer().has(getSelectedSpinnerKey(), PersistentDataType.STRING))
                continue;

            String name = player.getPersistentDataContainer().get(
                    this.api.getSelectedSpinnerKey(),
                    PersistentDataType.STRING
            );

            @Nullable Spinner spinner = this.api.getSpinner(name);

            if (spinner == null)
                return;

            UUID uuid = player.getUniqueId();
            Location location = player.getLocation();

            if (!movementMap.containsKey(uuid))
            {
                movementMap.put(uuid, location);
            }

            for (Player target : Bukkit.getOnlinePlayers())
            {
                if (movementMap.get(uuid).distance(location) == 0.0)
                {
                    spinner.spawn(player, target);
                    continue;
                }
                spinner.spawnSingle(player, target);
            }
            movementMap.put(uuid, location);
        }
    }

    private NamespacedKey getSelectedSpinnerKey()
    {
        return this.api.getSelectedSpinnerKey();
    }
}

