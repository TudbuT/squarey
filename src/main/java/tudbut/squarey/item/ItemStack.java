package tudbut.squarey.item;

import de.tudbut.ui.windowgui.AdaptedGraphics;
import tudbut.obj.Vector2i;
import tudbut.squarey.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ItemStack {
    private Item item;
    public int count = 1;
    
    public ItemStack(Item item) {
        this.item = item;
    }
    public ItemStack(Item item, int count) {
        this.item = item;
        this.count = count;
    }
    
    public BufferedImage getTexture() {
        BufferedImage tex = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics g = tex.createGraphics();
        AdaptedGraphics ag = new AdaptedGraphics(g);
        ag.drawImage(0, 0, getItem().type.getTexture());
        if(count > 1) {
            ag.setColor(0xffffff);
            g.drawString("" + count, 2, 2 + 11);
            g.dispose();
        }
        return tex;
    }
    
    public void place(Vector2i pos, World world) {
        if(item.place(pos, world))
            count--;
        if(count == 0)
            item = new Item(ItemType.AIR);
    }
    
    public Item getItem() {
        return item;
    }
}
