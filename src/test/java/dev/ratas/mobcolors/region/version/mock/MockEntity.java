package dev.ratas.mobcolors.region.version.mock;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class MockEntity implements Entity {
    private final UUID id;
    private final String name;
    private final Location loc;
    private final int entityId;
    private final EntityType type;

    public MockEntity(UUID id, String name, Location loc, int entityId, EntityType type) {
        this.id = id;
        this.name = name;
        this.loc = loc.clone();
        this.entityId = entityId;
        this.type = type;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void sendMessage(String message) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void sendMessage(String... messages) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void sendMessage(UUID sender, String message) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void sendMessage(UUID sender, String... messages) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isPermissionSet(String name) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean hasPermission(String name) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean hasPermission(Permission perm) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void recalculatePermissions() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean value) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public String getCustomName() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setCustomName(String name) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Location getLocation() {
        return this.loc.clone();
    }

    @Override
    public Location getLocation(Location loc) {
        loc.setX(this.loc.getX());
        loc.setY(this.loc.getY());
        loc.setZ(this.loc.getZ());
        loc.setPitch(this.loc.getPitch());
        loc.setYaw(this.loc.getYaw());
        return loc;
    }

    @Override
    public void setVelocity(Vector velocity) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Vector getVelocity() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public double getHeight() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public double getWidth() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public BoundingBox getBoundingBox() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isOnGround() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isInWater() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public World getWorld() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean teleport(Location location) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean teleport(Location location, TeleportCause cause) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean teleport(Entity destination) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean teleport(Entity destination, TeleportCause cause) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public int getFireTicks() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getMaxFireTicks() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setFireTicks(int ticks) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setVisualFire(boolean fire) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isVisualFire() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getFreezeTicks() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getMaxFreezeTicks() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setFreezeTicks(int ticks) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isFrozen() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void remove() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Server getServer() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void setPersistent(boolean persistent) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Entity getPassenger() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean setPassenger(Entity passenger) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public List<Entity> getPassengers() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean addPassenger(Entity passenger) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isEmpty() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean eject() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public float getFallDistance() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setFallDistance(float distance) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public UUID getUniqueId() {
        return id;
    }

    @Override
    public int getTicksLived() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setTicksLived(int value) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void playEffect(EntityEffect type) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public EntityType getType() {
        return type;
    }

    @Override
    public boolean isInsideVehicle() {
        return false;
    }

    @Override
    public boolean leaveVehicle() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Entity getVehicle() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public void setGlowing(boolean flag) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isGlowing() {
        return false;
    }

    @Override
    public void setInvulnerable(boolean flag) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }

    @Override
    public boolean isSilent() {
        return false;
    }

    @Override
    public void setSilent(boolean flag) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean hasGravity() {
        return true;
    }

    @Override
    public void setGravity(boolean gravity) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getPortalCooldown() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Set<String> getScoreboardTags() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public BlockFace getFacing() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Pose getPose() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Spigot spigot() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public SpawnCategory getSpawnCategory() {
        // TODO Auto-generated method stub
        return null;
    }

}
