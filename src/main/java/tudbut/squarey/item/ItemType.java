package tudbut.squarey.item;

import tudbut.rendering.Maths2D;
import tudbut.squarey.world.block.BlockType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public enum ItemType {
    AIR(null, Item.class, null),
    GRASS("grass", Item.class, BlockType.GRASS),
    DIRT("dirt", Item.class, BlockType.DIRT),
    STONE("stone", Item.class, BlockType.STONE),
    LADDER("ladder", Item.class, BlockType.LADDER),
    WALL("wall", Item.class, BlockType.WALL),
    PLANKS("planks", Item.class, BlockType.PLANKS),
    LOG("log", Item.class, BlockType.LOG),
    LEAVES("leaves", Item.class, BlockType.LEAVES),
    TREE("tree", ItemTree.class, null),
    
    
    ;
    
    
    private Image texture;
    public final Class<? extends Item> type;
    public final BlockType blockType;
    
    public Item create() {
        try {
            return type.getDeclaredConstructor(ItemType.class).newInstance(this);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }
    
    public Image getTexture() {
        return texture;
    }
    
    ItemType(String texture, Class<? extends Item> type, BlockType blockType) {
        this.blockType = blockType;
        try {
            if(texture != null)
                this.texture = Maths2D.distortImage(ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("items/" + texture + ".png"))), 32, 32, 1);
        }
        catch (Exception ignore) {
        }
        this.type = type;
    }
}
