package tudbut.squarey.world;

import tudbut.squarey.world.block.Block;
import tudbut.squarey.world.entity.Entity;

public class Chunk {
    
    public final Block[][] blocks = new Block[16][512];
    public final Entity[] entities = new Entity[256];
}
