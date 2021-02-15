package tudbut.squarey.item;

import tudbut.obj.Vector2i;
import tudbut.squarey.world.World;
import tudbut.squarey.world.block.BlockType;
import tudbut.squarey.world.structure.StructureTree;

public class ItemTree extends Item {
    public ItemTree() {
        super(ItemType.TREE);
    }
    public ItemTree(ItemType type) {
        super(ItemType.TREE);
    }
    
    private static final StructureTree tree = new StructureTree();
    
    @Override
    public boolean place(Vector2i pos, World world) {
        if(world.getBlock(pos.getX(), pos.getY() + 1).type == BlockType.AIR && world.getBlock(pos.getX(), pos.getY()).type == BlockType.GRASS) {
            tree.generate(world.chunks, pos, true);
            return true;
        }
        return false;
    }
}
