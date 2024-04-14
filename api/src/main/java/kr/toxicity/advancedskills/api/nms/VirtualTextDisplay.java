package kr.toxicity.advancedskills.api.nms;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Represents text display.
 */
public interface VirtualTextDisplay extends VirtualDisplay {
    /**
     * Sets the text of this display.
     * @param component text
     */
    void text(@NotNull Component component);

    /**
     * Sets the opacity of text.
     * @param opacity text opacity
     */
    void opacity(byte opacity);
}
