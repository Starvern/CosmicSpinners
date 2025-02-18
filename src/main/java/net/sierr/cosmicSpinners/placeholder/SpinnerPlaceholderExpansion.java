package net.sierr.cosmicSpinners.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.sierr.cosmicSpinners.CosmicSpinners;
import net.sierr.cosmicSpinners.Spinner;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class SpinnerPlaceholderExpansion extends PlaceholderExpansion
{
    private final CosmicSpinners api;

    public SpinnerPlaceholderExpansion(CosmicSpinners api)
    {
        this.api = api;
    }

    @Override
    public @NotNull String getIdentifier()
    {
        return "cosmicspinners";
    }

    @Override
    public @NotNull String getAuthor()
    {
        return "Starvern";
    }

    @Override
    public @NotNull String getVersion()
    {
        return "0.1.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params)
    {
        if (!(offlinePlayer instanceof Player player))
            return null;

        @Nullable Spinner spinner = this.api.getSpinner(player);

        if (spinner == null)
            return "none";

        return switch (params.toLowerCase(Locale.ROOT))
        {
            case "spinner", "spinner_name" -> spinner.getName();
            case "spinner_display_name" -> spinner.getDisplayName();
            case "spinner_description" -> spinner.getDescription();
            case "spinner_permission" -> spinner.getPermission();
            case "spinner_material" -> spinner.getMaterial();
            default -> "none";
        };
    }
}
