package dev.ratas.mobcolors.region.version.mock;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.BlockChangeDelegate;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameRule;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Raid;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.StructureType;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.StructureSearchResult;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class MockWorld implements World {
    private final UUID id;
    private final String name;

    public MockWorld(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Biome getBiome(Location location) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setBiome(Location location, Biome biome) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setBiome(int x, int y, int z, Biome biome) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public BlockState getBlockState(Location location) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public BlockData getBlockData(Location location) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public BlockData getBlockData(int x, int y, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Material getType(Location location) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Material getType(int x, int y, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setBlockData(Location location, BlockData blockData) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setBlockData(int x, int y, int z, BlockData blockData) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setType(Location location, Material material) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setType(int x, int y, int z, Material material) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType type) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType type, Consumer<BlockState> stateConsumer) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Entity spawnEntity(Location location, EntityType type) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Entity spawnEntity(Location loc, EntityType type, boolean randomizeData) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function)
            throws IllegalArgumentException {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, boolean randomizeData, Consumer<T> function)
            throws IllegalArgumentException {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUID() {
        return id;
    }

    @Override
    public Environment getEnvironment() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public long getSeed() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getMinHeight() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getMaxHeight() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        throw new IllegalStateException("Not implemented yet");
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
    public Block getBlockAt(int x, int y, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Block getBlockAt(Location location) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getHighestBlockYAt(int x, int z, HeightMap heightMap) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getHighestBlockYAt(Location location, HeightMap heightMap) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Block getHighestBlockAt(int x, int z, HeightMap heightMap) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Block getHighestBlockAt(Location location, HeightMap heightMap) {
        throw new IllegalStateException("Not implemented yet");
    }

    private MockChunk chunk;

    public void setChunk(MockChunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return chunk;
    }

    @Override
    public Chunk getChunkAt(Location location) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Chunk getChunkAt(Block block) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Chunk[] getLoadedChunks() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void loadChunk(Chunk chunk) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return true;
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return true;
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void loadChunk(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean regenerateChunk(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean refreshChunk(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isChunkForceLoaded(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Collection<Chunk> getForceLoadedChunks() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean addPluginChunkTicket(int x, int z, Plugin plugin) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean removePluginChunkTicket(int x, int z, Plugin plugin) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void removePluginChunkTickets(Plugin plugin) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Collection<Plugin> getPluginChunkTickets(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Item dropItem(Location location, ItemStack item) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Item dropItem(Location location, ItemStack item, Consumer<Item> function) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Item dropItemNaturally(Location location, ItemStack item) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Item dropItemNaturally(Location location, ItemStack item, Consumer<Item> function) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Arrow spawnArrow(Location location, Vector direction, float speed, float spread) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T extends AbstractArrow> T spawnArrow(Location location, Vector direction, float speed, float spread,
            Class<T> clazz) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean generateTree(Location location, TreeType type) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public LightningStrike strikeLightning(Location loc) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public LightningStrike strikeLightningEffect(Location loc) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public List<Entity> getEntities() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public List<Player> getPlayers() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z,
            Predicate<Entity> filter) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox, Predicate<Entity> filter) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance,
            Predicate<Entity> filter) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize,
            Predicate<Entity> filter) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance,
            FluidCollisionMode fluidCollisionMode) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance,
            FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance,
            FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize,
            Predicate<Entity> filter) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Location getSpawnLocation() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z, float angle) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public long getTime() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setTime(long time) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public long getFullTime() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setFullTime(long time) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public long getGameTime() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean hasStorm() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setStorm(boolean hasStorm) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getWeatherDuration() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setWeatherDuration(int duration) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isThundering() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setThundering(boolean thundering) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getThunderDuration() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setThunderDuration(int duration) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isClearWeather() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setClearWeatherDuration(int duration) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getClearWeatherDuration() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks,
            Entity source) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean createExplosion(Location loc, float power) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks, Entity source) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean getPVP() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setPVP(boolean pvp) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public ChunkGenerator getGenerator() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void save() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public List<BlockPopulator> getPopulators() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    // @SuppressWarnings("deprecation")
    public FallingBlock spawnFallingBlock(Location location, MaterialData data) throws IllegalArgumentException {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, BlockData data) throws IllegalArgumentException {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public FallingBlock spawnFallingBlock(Location location, Material material, byte data)
            throws IllegalArgumentException {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void playEffect(Location location, Effect effect, int data) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void playEffect(Location location, Effect effect, int data, int radius) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T data) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T data, int radius) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean getAllowAnimals() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean getAllowMonsters() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Biome getBiome(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setBiome(int x, int z, Biome bio) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public double getTemperature(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public double getTemperature(int x, int y, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public double getHumidity(int x, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public double getHumidity(int x, int y, int z) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getLogicalHeight() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isNatural() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isBedWorks() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean hasSkyLight() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean hasCeiling() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isPiglinSafe() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isRespawnAnchorWorks() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean hasRaids() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isUltraWarm() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getSeaLevel() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isAutoSave() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setAutoSave(boolean value) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Difficulty getDifficulty() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public File getWorldFolder() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public WorldType getWorldType() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean canGenerateStructures() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isHardcore() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setHardcore(boolean hardcore) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public long getTicksPerMonsterSpawns() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public long getTicksPerWaterSpawns() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setTicksPerWaterSpawns(int ticksPerWaterSpawns) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public long getTicksPerWaterAmbientSpawns() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setTicksPerWaterAmbientSpawns(int ticksPerAmbientSpawns) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public long getTicksPerAmbientSpawns() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getMonsterSpawnLimit() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setMonsterSpawnLimit(int limit) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getAnimalSpawnLimit() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setAnimalSpawnLimit(int limit) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setWaterAnimalSpawnLimit(int limit) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getWaterAmbientSpawnLimit() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setWaterAmbientSpawnLimit(int limit) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getAmbientSpawnLimit() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setAmbientSpawnLimit(int limit) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void playSound(Location location, String sound, float volume, float pitch) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public String[] getGameRules() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public String getGameRuleValue(String rule) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean setGameRuleValue(String rule, String value) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean isGameRule(String rule) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> T getGameRuleValue(GameRule<T> rule) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> T getGameRuleDefault(GameRule<T> rule) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> boolean setGameRule(GameRule<T> rule, T newValue) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public WorldBorder getWorldBorder() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
            double offsetZ) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
            double offsetZ, T data) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ, T data) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
            double offsetZ, double extra) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ, double extra) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
            double offsetZ, double extra, T data) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ, double extra, T data) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY,
            double offsetZ, double extra, T data, boolean force) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX,
            double offsetY, double offsetZ, double extra, T data, boolean force) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Location locateNearestStructure(Location origin, StructureType structureType, int radius,
            boolean findUnexplored) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getViewDistance() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Spigot spigot() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public Raid locateNearestRaid(Location location, int radius) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public List<Raid> getRaids() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public DragonBattle getEnderDragonBattle() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public long getTicksPerWaterUndergroundCreatureSpawns() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getWaterUndergroundCreatureSpawnLimit() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setTicksPerWaterUndergroundCreatureSpawns(int arg0) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void setWaterUndergroundCreatureSpawnLimit(int arg0) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public boolean generateTree(Location arg0, Random arg1, TreeType arg2, Predicate<BlockState> arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NamespacedKey getKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSimulationDistance() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSpawnLimit(SpawnCategory arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getTicksPerSpawns(SpawnCategory arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void playSound(Entity arg0, Sound arg1, float arg2, float arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playSound(Entity arg0, Sound arg1, SoundCategory arg2, float arg3, float arg4) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSpawnLimit(SpawnCategory arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTicksPerSpawns(SpawnCategory arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public StructureSearchResult locateNearestStructure(Location origin,
            org.bukkit.generator.structure.StructureType structureType, int radius, boolean findUnexplored) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StructureSearchResult locateNearestStructure(Location arg0, Structure arg1, int arg2, boolean arg3) {
        // TODO Auto-generated method stub
        return null;
    }

}
