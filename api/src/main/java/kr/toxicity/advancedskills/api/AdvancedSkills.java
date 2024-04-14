package kr.toxicity.advancedskills.api;

import kr.toxicity.advancedskills.api.nms.NMS;
import kr.toxicity.advancedskills.api.plugin.ReloadResult;
import kr.toxicity.advancedskills.api.scheduler.WrappedScheduler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.serialize.minecraft.fs.FileTreeWriter;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Plugin's main class.
 */
public abstract class AdvancedSkills extends JavaPlugin {

    private static AdvancedSkills instance;

    // Do not call this.
    @Override
    public void onLoad() {
        if (instance != null) throw new RuntimeException();
        instance = this;
    }

    /**
     * Gets an instance of AdvancesSkills
     * @return plugin instance
     */
    public static @NotNull AdvancedSkills inst() {
        return Objects.requireNonNull(instance);
    }

    /**
     * Reload this plugin.
     * @return reload result
     */
    public abstract @NotNull ReloadResult reload();

    /**
     * Returns this plugin is currently on reload or not.
     * @return currently on reload or not
     */
    public abstract boolean onReload();

    /**
     * Gets a volatile code handler.
     * @return nms code
     */
    public abstract @NotNull NMS nms();

    /**
     * Gets a wrapped scheduler between Paper and Folia.
     * @return wrapped scheduler
     */
    public abstract @NotNull WrappedScheduler scheduler();

    /**
     * Gets a wrapped audiences between Spigot and Paper.
     * @return bukkit audiences
     */
    public abstract @NotNull BukkitAudiences audiences();

    /**
     * Gets a supplier of file writer.
     * @return file writer supplier
     */
    public abstract @NotNull Supplier<FileTreeWriter> writerSupplier();

    /**
     * Sets a supplier of file writer.
     * @param writerSupplier file writer supplier
     */
    public abstract void writerSupplier(@NotNull Supplier<FileTreeWriter> writerSupplier);

    /**
     * Gets a namespace. default is 'advancedskills'
     * @return namespace
     */
    public abstract @NotNull String namespace();

    /**
     * Sets a namespace.
     * @param namespace namespace
     */
    public abstract void namespace(@NotNull String namespace);
}
