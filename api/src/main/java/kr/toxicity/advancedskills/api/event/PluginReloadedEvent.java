package kr.toxicity.advancedskills.api.event;

import kr.toxicity.advancedskills.api.plugin.ReloadResult;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event be called when plugin is reloaded.
 */
public class PluginReloadedEvent extends Event implements AdvancedSkillsEvent {
    private final ReloadResult result;
    public PluginReloadedEvent(@NotNull ReloadResult result) {
        this.result = result;
    }

    /**
     * Gets the result of reload.
     * @return reload result
     */
    public @NotNull ReloadResult result() {
        return result;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
