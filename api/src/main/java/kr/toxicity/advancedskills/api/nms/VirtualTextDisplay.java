package kr.toxicity.advancedskills.api.nms;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface VirtualTextDisplay extends VirtualDisplay {
    void text(@NotNull Component component);
    void opacity(byte opacity);
}
