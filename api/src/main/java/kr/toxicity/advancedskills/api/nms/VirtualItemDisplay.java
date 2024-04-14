package kr.toxicity.advancedskills.api.nms;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents item display.
 */
public interface VirtualItemDisplay extends VirtualDisplay {
    /**
     * Sets the item of this display.
     * @param itemStack target item
     */
    void item(@NotNull ItemStack itemStack);
}
