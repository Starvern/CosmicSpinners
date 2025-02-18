package net.sierr.cosmicSpinners;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Spinner
{
    private final CosmicSpinners api;
    private final String name;
    private final String permission;
    private final Color color;
    private final double radius;
    private final double speed;
    private final double amount;
    private final float size;
    private final String material;
    private final String displayName;
    private final String description;

    public Spinner(
            CosmicSpinners api,
            String name,
            String permission,
            Color color,
            double radius,
            double speed,
            double amount,
            float size,
            String material,
            String displayName,
            String description
    ) {
        this.api = api;
        this.name = name;
        this.permission = permission;
        this.color = color;
        this.radius = radius;
        this.amount = amount / 10;
        this.speed = speed * this.amount;
        this.size = size;
        this.material = material;
        this.displayName = displayName;
        this.description = description;
    }

    public String getMaterial()
    {
        return this.material;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public String getName()
    {
        return this.name;
    }

    public String getPermission()
    {
        return this.permission;
    }

    public void spawn(Player player, Player target)
    {
        if (target.hasPermission("cosmicspinners.hide")) return;

        Particle.DustOptions dustOptions = new Particle.DustOptions(this.color, this.size);

        int count = 0;

        for (double t = 0; t <= 2 * Math.PI * radius; t += this.amount)
        {
            count ++;
            double x = this.radius * Math.cos(t) + player.getX();
            double z = this.radius * Math.sin(t) + player.getZ();

            Location calculatedLocation = new Location(player.getWorld(), x, player.getY(), z);
            Bukkit.getScheduler().runTaskLater(this.api.getPlugin(), () ->
            {
                target.spawnParticle(Particle.DUST, calculatedLocation, 1, 0, 0, 0, 1, dustOptions);
            }, (long) (count * this.speed));
        }
    }

    public void spawnSingle(Player player, Player target)
    {
        Particle.DustOptions dustOptions = new Particle.DustOptions(this.color, this.size);
        target.spawnParticle(Particle.DUST, player.getLocation(), 1, 0, 0, 0, 1, dustOptions);
    }
}
