package tudbut.squarey.world.block;

import tudbut.obj.Vector2i;
import tudbut.parsing.TCN;

import java.awt.*;

public class Block {
    
    public final BlockType type;
    public final Vector2i pos;
    
    public Block(BlockType type, Vector2i pos) {
        this.type = type;
        this.pos = pos;
    }
    
    public Image getTexture() {
        return type.getTexture();
    }
    
    public TCN getData() {
        return null;
    }
    
    public void loadData(TCN tcn) {
    
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Block) {
            return type == ((Block) o).type;
        }
        return false;
    }
}
