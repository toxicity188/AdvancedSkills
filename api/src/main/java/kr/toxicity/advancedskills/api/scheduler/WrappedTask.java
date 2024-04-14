package kr.toxicity.advancedskills.api.scheduler;

/**
 * Wrapped task between Paper and Folia.
 */
public interface WrappedTask {
    /**
     * Returns whether this task is cancelled by some reason.
     * @return whether this task is cancelled.
     */
    boolean isCancelled();

    /**
     * Cancels this task.
     */
    void cancel();
}
