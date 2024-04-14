package kr.toxicity.advancedskills.api.nms;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface VirtualDisplay {
    @NotNull
    UUID uuid();
    @NotNull
    Location location();
    @NotNull
    World world();
    boolean isRemoved();

    void teleport(@NotNull Location location);
    void scale(@NotNull Vector vector);
    void brightness(@NotNull Display.Brightness brightness);
    void billboard(@NotNull Display.Billboard billboard);
    void remove();
}
