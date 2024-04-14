package kr.toxicity.advancedskills.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event be called when plugin is enabled.
 */
public class PluginLoadEvent extends Event implements AdvancedSkillsEvent {
    public PluginLoadEvent() {
        super(true);
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
