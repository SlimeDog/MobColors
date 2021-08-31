package dev.ratas.mobcolors;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.ChunkPopulateEvent;

import dev.ratas.mobcolors.coloring.MobColorer;
import dev.ratas.mobcolors.coloring.settings.ColorDecision;
import dev.ratas.mobcolors.coloring.settings.ColorReason;
import dev.ratas.mobcolors.config.Settings;
import dev.ratas.mobcolors.config.mob.MobTypes;
import dev.ratas.mobcolors.config.world.WorldSettings;
import dev.ratas.mobcolors.region.RegionOptions;

public class SpawnListener implements Listener {
    private final Settings settings;

    public SpawnListener(Settings settings) {
        this.settings = settings;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent event) {
        handleEntity(event.getEntity(), null, event.getSpawnReason());
    }

    public boolean handleEntity(LivingEntity ent, RegionOptions options, SpawnReason spawnReason) {
        Class<?> clazz = MobTypes.getInterestingClass(ent);
        if (clazz == null) {
            return false;
        }
        return handleEntity(ent, options, spawnReason, clazz);
    }

    private <T> boolean handleEntity(LivingEntity ent, RegionOptions options, SpawnReason spawnReason,
            Class<T> entityClass) {
        WorldSettings ws = settings.getWorldManager().getWorldSettings(ent.getWorld());
        if (ws == null) {
            return false;
        }
        @SuppressWarnings("unchecked")
        MobColorer<T, ?> colorer = (MobColorer<T, ?>) ws.getColorer(ent.getType());
        if (colorer == null) {
            return false;
        }
        @SuppressWarnings("unchecked")
        T t = (T) ent;
        ColorReason reason = ColorReason.fromSpawnReason(spawnReason);
        ColorDecision decision = colorer.shouldColor(t, options, reason);
        if (decision == ColorDecision.IGNORE) {
            return false;
        }
        return colorer.color(t, decision == ColorDecision.RESCHEDULE);
    }

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent event) {
        for (Entity ent : event.getChunk().getEntities()) {
            if (!(ent instanceof LivingEntity)) {
                continue;
            }
            handleEntity((LivingEntity) ent, null, SpawnReason.DEFAULT);
        }
    }

}