package kr.toxicity.advancedskills.api.plugin;

import org.jetbrains.annotations.NotNull;

/**
 * Reload result data.
 * @param state a state of reload
 * @param time reload time
 */
public record ReloadResult(@NotNull ReloadState state, long time) {
}
