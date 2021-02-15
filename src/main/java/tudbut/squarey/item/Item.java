package tudbut.squarey.item;

import tudbut.obj.Vector2i;
import tudbut.squarey.world.World;
import tudbut.squarey.world.block.Block;
import tudbut.squarey.world.block.BlockType;

public class Item {
    
    public final ItemType type;
    
    public Item(ItemType type) {
        this.type = type;
    }
    
    public boolean place(Vector2i pos, World world) {
        if(world.getBlock(pos.getX(), pos.getY()).type == BlockType.AIR) {
            world.setBlock(pos.getX(), pos.getY(), type.blockType);
            return true;
        }
        return false;
    }
}
