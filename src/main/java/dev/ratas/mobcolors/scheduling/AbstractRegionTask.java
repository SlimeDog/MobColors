package dev.ratas.mobcolors.scheduling;

import org.bukkit.World;

public abstract class AbstractRegionTask extends AbstractMultiChunkTask {

    public AbstractRegionTask(World world, int regionX, int regionZ, boolean ignoreUngenerated) {
        super(world, regionX << 5, regionZ << 5, (regionX << 5) + 31, (regionZ << 5) + 31, ignoreUngenerated);
    }

}
