package kr.toxicity.advancedskills.api.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Wrapped scheduler between Paper and Folia.
 */
public interface WrappedScheduler {

    /**
     * Teleports entity to specific location.
     * @param entity target entity
     * @param location target location
     */
    void teleport(@NotNull Entity entity, @NotNull Location location);

    /**
     * Runs global region task.
     * @param runnable task
     * @return scheduled task
     */
    @NotNull WrappedTask task(@NotNull Runnable runnable);

    /**
     * Runs local region task.
     * @param runnable task
     * @return scheduled task
     */
    @NotNull WrappedTask task(@NotNull Location location, @NotNull Runnable runnable);

    /**
     * Runs global region task.
     * @param delay task delay
     * @param runnable task
     * @return scheduled task
     */
    @NotNull WrappedTask taskLater(long delay, @NotNull Runnable runnable);

    /**
     * Runs local region task.
     * @param delay task delay
     * @param runnable task
     * @return scheduled task
     */
    @NotNull WrappedTask taskLater(long delay, @NotNull Location location, @NotNull Runnable runnable);

    /**
     * Runs global region task.
     * @param delay task delay
     * @param period task period
     * @param runnable task
     * @return scheduled task
     */
    @NotNull WrappedTask taskTimer(long delay, long period, @NotNull Runnable runnable);

    /**
     * Runs local region task.
     * @param delay task delay
     * @param period task period
     * @param runnable task
     * @return scheduled task
     */
    @NotNull WrappedTask taskTimer(long delay, long period, @NotNull Location location, @NotNull Runnable runnable);

    /**
     * Runs async task.
     * @param runnable task
     * @return scheduled task
     */
    @NotNull WrappedTask asyncTask(@NotNull Runnable runnable);

    /**
     * Runs async task.
     * @param delay task delay
     * @param runnable task
     * @return scheduled task
     */
    @NotNull WrappedTask asyncTaskLater(long delay, @NotNull Runnable runnable);

    /**
     * Runs async task.
     * @param delay task delay
     * @param period task period
     * @param runnable task
     * @return scheduled task
     */
    @NotNull WrappedTask asyncTaskTimer(long delay, long period, @NotNull Runnable runnable);
}
