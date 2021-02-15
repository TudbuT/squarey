package tudbut.squarey.world;

import tudbut.logger.LoggerSink;
import tudbut.obj.Vector2i;
import tudbut.squarey.world.block.Block;
import tudbut.squarey.world.block.BlockType;
import tudbut.squarey.world.structure.Structure;
import tudbut.squarey.world.structure.StructureTree;
import tudbut.tools.NoiseGenerator;

import java.util.Random;

public class ChunkGenerator {
    
    public static Chunk[] createWorld(Random random, LoggerSink logger) {
        Chunk[] chunks = new Chunk[512];
    
        float[] heightMap = NoiseGenerator.generateRandom(1, 1, 512 * 16, 40, 20, random)[0][0];
        logger.debug("Height map done");
        float[] dirtHeightMap = NoiseGenerator.generateRandom(1, 1, 512 * 16, 8, 8, random)[0][0];
        logger.debug("Dirt map done");
        float[][] carveMap = NoiseGenerator.generateRandom(1, 512 * 16, 512, 20, 2, new Random())[0];
        logger.debug("Cave map done");
        
        for (int i = 0; i < chunks.length; i++) {
            chunks[i] = new Chunk();
        }
        logger.debug("Chunk initialisation done");
        for (int i = 0; i < heightMap.length; i++) {
            Chunk chunk = chunks[i / 16];
            int height = (int) heightMap[i] + 200;
            int r = (int) dirtHeightMap[i] + 7;
            for (int j = 0; j < 1; j++) {
                chunk.blocks[i % 16][j] = new Block(BlockType.END, new Vector2i(i, j));
            }
            for (int j = 1; j < height - r; j++) {
                if(random.nextInt(500 * j) == 0)
                    chunk.blocks[i % 16][j] = new Block(BlockType.WALL, new Vector2i(i, j));
                else
                    chunk.blocks[i % 16][j] = new Block(BlockType.STONE, new Vector2i(i, j));
            }
            for (int j = height - r; j < height - 1; j++) {
                chunk.blocks[i % 16][j] = new Block(BlockType.DIRT, new Vector2i(i, j));
            }
            for (int j = height - 1; j < height; j++) {
                chunk.blocks[i % 16][j] = new Block(BlockType.GRASS, new Vector2i(i, j));
            }
            for (int j = height; j < 512; j++) {
                chunk.blocks[i % 16][j] = new Block(BlockType.AIR, new Vector2i(i, j));
            }
        }
        logger.debug("Chunk carving done");
        for (int x = 0; x < carveMap.length; x++) {
            Chunk chunk = chunks[x / 16];
            for (int y = 1; y < carveMap[x].length; y++) {
                if(carveMap[x][y] >= 1.1) {
                    chunk.blocks[x % 16][y] = new Block(BlockType.AIR, new Vector2i(x, y));
                }
            }
        }
        logger.debug("Cave carving done");
        Structure tree = new StructureTree();
        int i = 0;
        for (int x = 0; x < 512 * 16; x++) {
            if(random.nextInt(20 - i) == 0) {
                for (int y = 511; y > 0; y--) {
                    if(chunks[x / 16].blocks[x % 16][y].type == BlockType.GRASS) {
                        tree.generate(chunks, new Vector2i(x, y), false);
                        break;
                    }
                }
                i = 0;
            }
            i++;
        }
        logger.debug("Population done");
        
        return chunks;
    }
}
