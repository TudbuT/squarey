package tudbut.squarey.item;

import de.tudbut.ui.windowgui.AdaptedGraphics;
import tudbut.obj.Vector2i;
import tudbut.squarey.world.World;
import tudbut.tools.Tools2;

import java.awt.*;

public class Inventory {
    
    public final ItemStack[][] items;
    public final Vector2i selectedItem = new Vector2i(0,0);
    
    public Inventory(int sizeX, int sizeY) {
        items = new ItemStack[sizeX][sizeY];
        for (int x = 0; x < items.length; x++) {
            for (int y = 0; y < items[x].length; y++) {
                items[x][y] = new ItemStack(new Item(ItemType.AIR), 0);
            }
        }
    }
    
    public void render(AdaptedGraphics graphics, int xo, int yo) {
        xo += 3;
        yo += 3;
        for (int x = 0; x < items.length; x++) {
            for (int y = 0; y < items[x].length; y++) {
                if(selectedItem.getX() == x && selectedItem.getY() == y)
                    graphics.setColor(0x80000000);
                else
                    graphics.setColor(0x20000000);
                graphics.g().fillRect(xo + (x * (32 + 10)) - 3, yo + (y * (32 + 10)) - 3, 32 + 6, 32 + 6);
                graphics.drawImage(xo + (x * (32 + 10)), yo + (y * (32 + 10)), items[x][y].getTexture());
            }
        }
    }
    
    public void roll(int x, int y) {
        selectedItem.set((int) Tools2.roll(x + selectedItem.getX(), 0, items.length), (int) Tools2.roll(y + selectedItem.getY(), 0, items[0].length));
    }
    
    public void place(Vector2i pos, World world) {
        items[selectedItem.getX()][selectedItem.getY()].place(pos, world);
    }
}
