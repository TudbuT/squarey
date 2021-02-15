package tudbut.squarey.world;

import de.tudbut.tools.Tools;
import tudbut.logger.LoggerSink;
import tudbut.obj.Vector2i;
import tudbut.squarey.world.block.Block;
import tudbut.squarey.world.block.BlockType;
import tudbut.squarey.world.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class World {

    public Chunk[] chunks = new Chunk[512];
    public Chunk[] originalChunks = new Chunk[512];
    public final ArrayList<Entity> entities = new ArrayList<>();
    private long seed = 0;
    
    public void generate(long seed, LoggerSink logger) {
        this.seed = seed;
        originalChunks = ChunkGenerator.createWorld(new Random(seed), logger);
        for (int i = 0; i < chunks.length; i++) {
            chunks[i] = new Chunk();
        }
        for (int x = 0; x < 512 * 16; x++) {
            System.arraycopy(originalChunks[x / 16].blocks[x % 16], 0, chunks[x / 16].blocks[x % 16], 0, 512);
        }
    }
    
    public Block getBlock(int x, int y) {
        return chunks[x / 16].blocks[x % 16][y];
    }
    
    public Block getBlock(double x, double y) {
        return getBlock((int) (x - 0.1), (int) (y + 1.5));
    }
    
    public void setBlock(int x, int y, Block block) {
        chunks[x / 16].blocks[x % 16][y] = block;
    }
    
    public void setBlock(int x, int y, BlockType block) {
        chunks[x / 16].blocks[x % 16][y] = block.create(new Vector2i(x, y));
    }
    
    public String serialize() {
        Map<String, String> map = new HashMap<>();
        map.put("seed", seed + "");
        for (int x = 0; x < 512 * 16; x++) {
            for (int y = 0; y < 512; y++) {
                if(!chunks[x / 16].blocks[x % 16][y].equals(originalChunks[x / 16].blocks[x % 16][y])) {
                    map.put(x + " " + y, chunks[x / 16].blocks[x % 16][y].type.ordinal() + "");
                }
            }
        }
        return Tools.mapToString(map);
    }
    
    public static World deserialize(String s, LoggerSink logger) {
        Map<String, String> map = Tools.stringToMap(s);
        World world = new World();
        world.generate(Long.parseLong(map.get("seed")), logger);
        for (int x = 0; x < 512 * 16; x++) {
            for (int y = 0; y < 512; y++) {
                if(map.containsKey(x + " " + y)) {
                    world.chunks[x / 16].blocks[x % 16][y] = BlockType.values()[Integer.parseInt(map.get(x + " " + y))].create(new Vector2i(x, y));
                }
            }
        }
        return world;
    }
    
}
