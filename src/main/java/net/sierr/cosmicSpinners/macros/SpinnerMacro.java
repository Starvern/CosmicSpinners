package net.sierr.cosmicSpinners.macros;

import net.sierr.cosmicSpinners.CosmicSpinners;
import net.sierr.cosmicSpinners.Spinner;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.properties.impl.StringProperty;

import java.util.List;
import java.util.Optional;

public class SpinnerMacro extends Macro
{
    private final CosmicSpinners api;

    public SpinnerMacro(CosmicSpinners api)
    {
        super(api.getUuiApi(), api.getPlugin(), "spinners");
        this.api = api;
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (!(action.getHolder() instanceof GuiPage page))
            return;

        if (action.getArguments().size() < 2)
            return;

        String itemId = action.getArguments().get(0);
        String replacementId = action.getArguments().get(1);

        Optional<GuiItem> replacement = page.getItem(replacementId);

        if (replacement.isEmpty())
        {
            event.getGui().getLogger().warning("Invalid replacement item ID passed: " + replacementId);
            return;
        }

        List<Spinner> spinners = this.api.getRegisteredSpinners().stream().toList();
        event.getGui().ensureSize(event.getPage(), itemId, spinners.size());
        List<SlottedGuiItem> items = event.getGui().getAllSlottedItems(itemId);

        for (int i = 0; i < items.size(); i++)
        {
            SlottedGuiItem item = items.get(i);
            if (spinners.size() <= i)
            {
                replacement.get().slot(item.getSlot());
                continue;
            }

            Spinner spinner = spinners.get(i);

            item.getProperties().setProperty(new StringProperty("spinner", spinner.getName()), true);
            item.getProperties().setProperty(new StringProperty("spinner_name", spinner.getDisplayName()), true);
            item.getProperties().setProperty(new StringProperty("spinner_permission", spinner.getPermission()), true);
            item.getProperties().setProperty(new StringProperty("spinner_material", spinner.getMaterial()), true);
            item.getProperties().setProperty(new StringProperty("spinner_description", spinner.getDescription()), true);
        }
    }
}
