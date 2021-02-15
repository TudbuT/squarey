package tudbut.squarey.world.block;

import tudbut.obj.Vector2i;
import tudbut.rendering.Maths2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;

public enum BlockType {
    AIR(null, false, false, false, Block.class),
    END("end", false, true, false, Block.class),
    GRASS("grass", false, true, false, Block.class),
    DIRT("dirt", false, true, false, Block.class),
    STONE("stone", false, true, false, Block.class),
    LADDER("ladder", false, false, true, Block.class),
    WALL("wall", false, false, false, Block.class),
    PLANKS("planks", false, true, false, Block.class),
    CHEST("chest", true, true, false, Block.class),
    LOG("log", false, false, false, Block.class),
    LEAVES("leaves", false, false, false, Block.class),
    
    ;
    
    
    private Image texture;
    public final boolean hasData;
    public final boolean solid;
    public final boolean climbable;
    private Class<? extends Block> type;
    
    public Block create(Vector2i pos) {
        try {
            return type.getDeclaredConstructor(BlockType.class, Vector2i.class).newInstance(this, pos);
        }
        catch (Exception e) {
            throw new RuntimeException("Impossible event", e);
        }
    }
    
    public Image getTexture() {
        return texture;
    }
    public boolean hasData() {
        return hasData;
    }
    public Class<? extends Block> getBlockType() {
        return type;
    }
    
    BlockType(String texture, boolean hasData, boolean solid, boolean climbable, Class<? extends Block> type) {
        this.solid = solid;
        this.climbable = climbable;
        try {
            if(texture != null)
                this.texture = Maths2D.distortImage(ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("blocks/" + texture + ".png"))), 16, 16, 1);
        }
        catch (Exception ignore) {
        }
        this.hasData = hasData;
        this.type = type;
    }
}
