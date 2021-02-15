package tudbut.squarey.client.rendering;

import de.tudbut.type.Vector2d;
import de.tudbut.ui.windowgui.AdaptedGraphics;
import tudbut.obj.Vector2i;
import tudbut.squarey.world.block.Block;
import tudbut.squarey.world.entity.Entity;

public class Renderer {

    public static void render(AdaptedGraphics ag, Block block, Vector2i offset) {
        Vector2i pos = block.pos.clone().multiply(16).multiply(1, -1).add(offset);
        
        if(block.getTexture() != null)
            ag.drawImage(pos.getX(), pos.getY(), block.getTexture());
    }
    public static void render(AdaptedGraphics ag, Entity entity, Vector2i offset) {
        Vector2d pos = entity.getHitbox().getPos().multiply(16).multiply(1, -1).add(offset.getX(), offset.getY());
        
        if(entity.getTexture() != null)
            ag.drawImage((int) pos.getX(), (int) pos.getY(), entity.getTexture());
    }
}
