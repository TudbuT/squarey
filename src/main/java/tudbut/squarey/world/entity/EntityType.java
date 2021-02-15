package tudbut.squarey.world.entity;

import tudbut.rendering.Maths2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;

public enum EntityType {
    PLAYER("player"),
    
    ;
    
    
    private Image texture;
    
    public Image getTexture() {
        return texture;
    }
    
    EntityType(String texture) {
        try {
            this.texture = Maths2D.distortImage(ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("entities/" + texture + ".png"))), 8, 16, 1);
        }
        catch (Exception ignore) {
        }
    }
}
