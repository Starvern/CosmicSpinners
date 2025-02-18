package net.sierr.cosmicSpinners;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.HumanEntity;

import java.util.Locale;

public enum MessageKey
{
    NOT_PLAYER,
    SPINNER_SELECTED,
    SPINNER_DESELECTED,
    INSUFFICIENT_PERMISSION,
    PLUGIN_RELOADED,
    SPECIFY_SPINNER,
    UNKNOWN_SPINNER;

    @Override
    public String toString()
    {
        return super.toString().toLowerCase(Locale.ROOT);
    }

    public Component asComponent(CosmicSpinners api)
    {
        String message = api.getPlugin().getMessageConfig().getString(this.toString());
        if (message == null)
        {
            api.getLogger().warning("There was an error parsing messages. Please reload the config.");
            return Component.text().build();
        }
        return MiniMessage.miniMessage().deserialize(message);
    }

    public void sendComponent(CosmicSpinners api, Audience audience)
    {
        audience.sendMessage(this.asComponent(api));
    }
}
