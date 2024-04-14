package kr.toxicity.advancedskills.api.nms;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface VirtualItemDisplay extends VirtualDisplay {
    void item(@NotNull ItemStack itemStack);
}
