package tudbut.squarey.client;

import de.tudbut.tools.FileRW;
import de.tudbut.tools.Keyboard;
import de.tudbut.tools.Mouse;
import de.tudbut.type.Vector2d;
import de.tudbut.ui.windowgui.AdaptedGraphics;
import de.tudbut.ui.windowgui.RenderableWindow;
import tudbut.logger.Logger;
import tudbut.logger.LoggerSink;
import tudbut.net.ic.PBIC;
import tudbut.obj.Vector2i;
import tudbut.rendering.Maths2D;
import tudbut.rendering.Rectangle2D;
import tudbut.squarey.client.communication.ClientConnection;
import tudbut.squarey.client.rendering.Renderer;
import tudbut.squarey.item.Item;
import tudbut.squarey.item.ItemStack;
import tudbut.squarey.item.ItemTree;
import tudbut.squarey.item.ItemType;
import tudbut.squarey.world.World;
import tudbut.squarey.world.block.BlockType;
import tudbut.squarey.world.entity.Entity;
import tudbut.squarey.world.entity.EntityPlayer;
import tudbut.squarey.world.structure.StructureTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Client {
    public LoggerSink logger = new Logger("Squarey");
    private static Client instance; { instance = this; }
    public static Client getInstance() {
        return instance;
    }
    
    public RenderableWindow window;
    
    public World world;
    public Vector2i renderOffset;
    public Vector2i lastMousePos;
    public int lastMouseWheelPos = 0;
    
    public Entity viewEntity;
    public Vector2d lookingAt;
    public Vector2i lookingAtBlock;
    
    public Client(String server, int port) {
        if(server != null) {
            try {
                PBIC.Client client = new PBIC.Client(server, port);
                viewEntity = new ClientPlayer(new Vector2d(256 * 16,300));
                ((EntityPlayer) viewEntity).clientConnection = new ClientConnection(client.connection, (ClientPlayer) viewEntity);
                ((ClientPlayer) viewEntity).clientConnection.run();
                while (viewEntity.world == null);
                world = viewEntity.world;
                world.entities.add(viewEntity);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if (!new File("world").exists()) {
                world = new World();
                logger.info("Generating");
                world.generate((long) (Math.random() * Long.MAX_VALUE), logger);
                logger.info("Done");
            }
            else {
                try {
                    logger.info("Loading");
                    String s = new FileRW("world").getContent().join("\n");
                    world = World.deserialize(s, logger);
                    logger.info("Done");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    new FileRW("world").setContent(world.serialize());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }, "World save thread"));
            viewEntity = new ClientPlayer(new Vector2d(256 * 16,300));
        }
        
        renderOffset = new Vector2i(-(16 * 256 * 16), 9 * 50 + 200 * 16);
        
        window = new RenderableWindow(16 * 50, 9 * 50, "Squarey", 60, true);
        window.render(this::render);
        window.prepareRender();
        window.getWindow().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        new Thread(() -> {
            while (true) {
                long sa = new Date().getTime();
    
                for (int i = 0; i < world.entities.size(); i++) {
                    if(world.entities.get(i) == viewEntity)
                        world.entities.get(i).applyGravity();
                }
                
                if (viewEntity instanceof ClientPlayer) {
    
                    viewEntity.inventory.items[0][0] = new ItemStack(new Item(ItemType.STONE), 1);
                    viewEntity.inventory.items[1][0] = new ItemStack(new Item(ItemType.WALL), 1);
                    viewEntity.inventory.items[2][0] = new ItemStack(new Item(ItemType.DIRT), 1);
                    viewEntity.inventory.items[3][0] = new ItemStack(new Item(ItemType.GRASS), 1);
                    viewEntity.inventory.items[4][0] = new ItemStack(new Item(ItemType.LADDER), 1);
                    viewEntity.inventory.items[5][0] = new ItemStack(new Item(ItemType.PLANKS), 1);
                    viewEntity.inventory.items[6][0] = new ItemStack(new Item(ItemType.LOG), 1);
                    viewEntity.inventory.items[7][0] = new ItemStack(new Item(ItemType.LEAVES), 1);
                    viewEntity.inventory.items[0][1] = new ItemStack(new ItemTree(), 1);
    
                    int mouseWheelPos = Mouse.getMouseWheelPos();
                    int scroll = -(mouseWheelPos - lastMouseWheelPos);
                    viewEntity.inventory.roll(!Keyboard.isKeyDown(KeyEvent.VK_SHIFT) ? scroll : 0, Keyboard.isKeyDown(KeyEvent.VK_SHIFT) ? scroll : 0);
                    lastMouseWheelPos = mouseWheelPos;
                    viewEntity.noGravity = true;
    
                    if (Keyboard.isKeyDown(KeyEvent.VK_A)) {
                        if (world.getBlock(viewEntity.pos.getX(), viewEntity.pos.getY()).type.climbable)
                            viewEntity.motion.add(-0.02 * (viewEntity.noGravity ? 3 : 1), 0);
                        else
                            viewEntity.motion.add(-0.05 * (viewEntity.noGravity ? 3 : 1), 0);
                    }
                    if (Keyboard.isKeyDown(KeyEvent.VK_D)) {
                        if (world.getBlock(viewEntity.pos.getX(), viewEntity.pos.getY()).type.climbable)
                            viewEntity.motion.add(0.02 * (viewEntity.noGravity ? 3 : 1), 0);
                        else
                            viewEntity.motion.add(0.05 * (viewEntity.noGravity ? 3 : 1), 0);
                    }
                    if (Keyboard.isKeyDown(KeyEvent.VK_SPACE)) {
                        viewEntity.jump();
                    }
                    if (Keyboard.isKeyDown(KeyEvent.VK_W)) {
                        try {
                            if (world.getBlock(viewEntity.pos.getX(), viewEntity.pos.getY()).type.climbable || viewEntity.noGravity) {
                                viewEntity.motion.add(0, 0.05);
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException ignored) {
                        }
                    }
                    if (Keyboard.isKeyDown(KeyEvent.VK_S)) {
                        try {
                            if (world.getBlock(viewEntity.pos.getX(), viewEntity.pos.getY()).type.climbable || viewEntity.noGravity) {
                                viewEntity.motion.add(0, -0.05);
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException ignored) {
                        }
                    }
                    if (Keyboard.isKeyDown(KeyEvent.VK_G)) {
                        new StructureTree().generate(world.chunks, lookingAtBlock);
                    }
                    if (Mouse.isKeyDown(1)) {
                        try {
                            Vector2d vec = lookingAt.clone().add(viewEntity.pos.clone().add(0, 0.8).negate());
                            if (Math.sqrt(vec.getX() * vec.getX() + vec.getY() * vec.getY()) < 5) {
                                world.setBlock(lookingAtBlock.getX(), lookingAtBlock.getY(), BlockType.AIR);
                                if(((ClientPlayer) viewEntity).clientConnection != null)
                                    ((ClientPlayer) viewEntity).clientConnection.writeBlockUpdate(lookingAtBlock);
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException | PBIC.PBICException.PBICWriteException ignored) {
                        }
                    }
                    if (Mouse.isKeyDown(3)) {
                        try {
                            Vector2d vec = lookingAt.clone().add(viewEntity.pos.clone().add(0, 0.8).negate());
                            if (Math.sqrt(vec.getX() * vec.getX() + vec.getY() * vec.getY()) < 5) {
                                viewEntity.inventory.place(lookingAtBlock, world);
                                if(((ClientPlayer) viewEntity).clientConnection != null)
                                    ((ClientPlayer) viewEntity).clientConnection.writeBlockUpdate(lookingAtBlock);
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException | PBIC.PBICException.PBICWriteException ignored) {
                        }
                    }
                    
    
                    if (((EntityPlayer) viewEntity).clientConnection != null) {
                        try {
                            if(!viewEntity.motion.toString().equals(new Vector2d(0,0).toString()))
                                ((ClientPlayer) viewEntity).clientConnection.writeMotion();
                            viewEntity.sendPos++;
                            if(viewEntity.sendPos >= 50) {
                                ((ClientPlayer) viewEntity).clientConnection.writePosition();
                            }
                            viewEntity.tickMotion();
                        }
                        catch (PBIC.PBICException.PBICWriteException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        for (int i = 0; i < world.entities.size(); i++) {
                            world.entities.get(i).tickMotion();
                        }
                    
                }
    

    
                try {
                    Thread.sleep(1000 / 50 - (new Date().getTime() - sa));
                }
                catch (Exception ignore) {
                    logger.info("Tick took too long!");
                }
            }
        }, "Ticker").start();
        while (true) {
            long sa = new Date().getTime();
            Vector2i la = window.getMousePos().add(renderOffset.clone().negate());
            lookingAt = new Vector2d(la.getX() / 16d, -la.getY() / 16d).add(0, 1);
            lookingAtBlock = new Vector2i((int) lookingAt.getX(), (int) lookingAt.getY());
            lastMousePos = Mouse.getMousePos();
            window.doRender();
            window.swapBuffers();
            try {
                Thread.sleep(1000 / 60 - (new Date().getTime() - sa));
            }
            catch (Exception ignore) {
                //logger.info("Render took too long!");
            }
        }
        
    }
    
    public void render(AdaptedGraphics ag, Graphics g, BufferedImage img) {
        Vector2i size = window.getSizeOnScreen();
        ag.setColor(0xaaeeff);
        g.fillRect(0,0, size.getX(), size.getY());
        Vector2i a = renderOffset.clone().negate().multiply(1 / 16f, -1 / 16f);
        Vector2i b = a.clone().add(size.clone().multiply(1 / 16f, -1 / 16f));
        a.add(new Vector2i(0,2));
        b.add(new Vector2i(2,0));
        for (int x = Math.max(a.getX(), 0); x < Math.min(b.getX(), 512 * 16); x++) {
            for (int y = Math.max(b.getY(), 0); y < Math.min(a.getY(),  512); y++) {
                try {
                    Renderer.render(ag, world.getBlock(x, y), renderOffset);
                } catch (NullPointerException e) {
                    logger.info("Null at " + x + " " + y);
                }
            }
        }
        for (int i = 0; i < world.entities.size(); i++) {
            if(world.entities.get(i) == viewEntity) {
                Vector2d vec = viewEntity.pos.clone().multiply(-16d, 16d);
                renderOffset.set((int) vec.getX() + (window.getSizeOnScreen().getX() / 2), (int) vec.getY() + (window.getSizeOnScreen().getY() / 2));
            }
            Renderer.render(ag, world.entities.get(i), renderOffset);
        }
    
        Vector2d vec = lookingAt.clone().add(viewEntity.pos.clone().add(0, 0.8).negate());
        if(Math.sqrt(vec.getX()*vec.getX() + vec.getY()*vec.getY()) < 5) {
            ag.setColor(0x00ff00);
            g.drawRect(lookingAtBlock.getX() * 16 - 1 + renderOffset.getX(), -lookingAtBlock.getY() * 16 - 1 + renderOffset.getY(), 17, 17);
            boolean hitsEntity = false;
            for (int i = 0; i < world.entities.size(); i++) {
                Entity entity = world.entities.get(i);
                boolean[] rel = Maths2D.getRelation(entity.getHitbox(), new Rectangle2D(new Vector2d(lookingAt.getX(), lookingAt.getY()), new Vector2d(0, 0)));
                boolean hits = true;
                for (boolean value : rel) {
                    if (value) {
                        hits = false;
                        break;
                    }
                }
                if (hits)
                    hitsEntity = true;
            }
            if (hitsEntity) {
                ag.setColor(0xff0000);
                Vector2i pos = window.getMousePos();
                g.drawLine(pos.getX() - 8, pos.getY(), pos.getX() + 8, pos.getY());
                g.drawLine(pos.getX(), pos.getY() - 8, pos.getX(), pos.getY() + 8);
            }
        }
        
        viewEntity.inventory.render(ag, 10, 10);
    }
}
