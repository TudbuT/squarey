package tudbut.squarey.world.entity;

import de.tudbut.type.Vector2d;
import tudbut.obj.Vector2i;
import tudbut.rendering.Maths2D;
import tudbut.rendering.Rectangle2D;
import tudbut.squarey.item.Inventory;
import tudbut.squarey.world.World;
import tudbut.squarey.world.block.BlockType;

import java.awt.*;

public class Entity {
    
    public final EntityType type;
    public Vector2d pos;
    public Vector2d motion = new Vector2d(0, 0);
    public int sendPos = 0;
    public volatile World world;
    public int jumpTimer = 0;
    public Inventory inventory = new Inventory(9, 1);
    private static int nextEntityID = 0;
    public int entityID = nextEntityID++;
    public boolean noGravity = false;
    
    public Entity(EntityType type, Vector2d pos, World world) {
        this.type = type;
        this.pos = pos.clone();
        if(world != null)
            (this.world = world).entities.add(this);
    }
    
    public Image getTexture() {
        return type.getTexture();
    }
    
    public void applyGravity() {
        try {
            if(!hitsGround() && !noGravity) {
                if(!world.getBlock(pos.getX(), pos.getY()).type.climbable)
                    motion.add(0, -0.25);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            motion.add(0, -1);
        }
    }
    
    public boolean hitsGround() {
        return hitsGround(pos, world);
    }
    
    public boolean hitsBlock() {
        return hitsBlock(pos, world);
    }
    
    /*public boolean hitsGround() {
        return cYn(pos, world);
    }
    
     */
    
    
    
    public static boolean hitsGround(Vector2d pos, World world) {
        if(world.getBlock((int) pos.getX(), (int) ((pos.getY()) + 1)).type.solid) {
            boolean[] rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((int) pos.getX(), (int) (pos.getY() - 0.5)), new Vector2d(1, 1)));
            boolean b = false;
            for (boolean value : rel) {
                if (value) {
                    b = true;
                    break;
                }
            }
            return b;
        }
        if(world.getBlock((int) (pos.getX() + 0.23), (int) ((pos.getY()) + 1)).type.solid) {
            boolean[] rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((int) pos.getX(), (int) (pos.getY() - 0.5)), new Vector2d(1, 1)));
            boolean b = false;
            for (boolean value : rel) {
                if (value) {
                    b = true;
                    break;
                }
            }
            return b;
        }
        if(world.getBlock((int) (pos.getX() - 0.25), (int) ((pos.getY()) + 1)).type.solid) {
            boolean[] rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((int) pos.getX(), (int) (pos.getY() - 0.5)), new Vector2d(1, 1)));
            boolean b = false;
            for (boolean value : rel) {
                if (value) {
                    b = true;
                    break;
                }
            }
            return b;
        }
        return false;
    }
    
    public static boolean hitsBlock(Vector2d pos, World world) {
        if(world.getBlock((int) pos.getX(), (int) ((pos.getY()) + 1)).type.solid) {
            boolean[] rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((int) pos.getX(), (int) (pos.getY() - 0.5)), new Vector2d(1, 1)));
            boolean b = false;
            for (boolean value : rel) {
                if (value) {
                    b = true;
                    break;
                }
            }
            return b;
        }
        if(world.getBlock((int) pos.getX(), (int) ((pos.getY()) + 3)).type.solid) {
            boolean[] rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((int) pos.getX(), (int) (pos.getY() + 2.5)), new Vector2d(1, 1)));
            boolean b = false;
            for (boolean value : rel) {
                if (value) {
                    b = true;
                    break;
                }
            }
            return b;
        }
        if(world.getBlock((int) (pos.getX() + 0.23), (int) ((pos.getY()) + 1)).type.solid) {
            boolean[] rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((int) pos.getX(), (int) (pos.getY() - 0.5)), new Vector2d(1, 1)));
            boolean b = false;
            for (boolean value : rel) {
                if (value) {
                    b = true;
                    break;
                }
            }
            return b;
        }
        if(world.getBlock((int) (pos.getX() - 0.25), (int) ((pos.getY()) + 1)).type.solid) {
            boolean[] rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((int) pos.getX(), (int) (pos.getY() - 0.5)), new Vector2d(1, 1)));
            boolean b = false;
            for (boolean value : rel) {
                if (value) {
                    b = true;
                    break;
                }
            }
            return b;
        }
        return false;
    }
    
    public static boolean g(Vector2d pos, World world) {
        boolean[] rel;
        boolean b;
        b = false;
        if(world.getBlock((int) (pos.getX()), (int) ((pos.getY()) - 0.5)).type != BlockType.AIR) {
            rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((int) pos.getX(), (int) (pos.getY() - 0.5)), new Vector2d(1, 1)));
            for (boolean value : rel) {
                if (value) {
                    b = true;
                    break;
                }
            }
        }
        if(world.getBlock((int) (pos.getX()), (int) ((pos.getY()) + 1.5)).type != BlockType.AIR) {
            rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((int) pos.getX(), (int) (pos.getY() + 1.5)), new Vector2d(1, 1)));
            for (boolean value : rel) {
                if (value) {
                    b = true;
                    break;
                }
            }
        }
        if(world.getBlock((int) (pos.getX()) + 1, (int) ((pos.getY()) + 0.5)).type != BlockType.AIR) {
            rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((int) pos.getX() + 1, (int) (pos.getY() + 0.5)), new Vector2d(1, 1)));
            for (boolean value : rel) {
                if (value) {
                    b = true;
                    break;
                }
            }
        }
        if(world.getBlock((int) (pos.getX()) - 1, (int) ((pos.getY()) + 0.5)).type != BlockType.AIR) {
            rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((int) pos.getX() - 1, (int) (pos.getY() + 0.5)), new Vector2d(1, 1)));
            for (boolean value : rel) {
                if (value) {
                    b = true;
                    break;
                }
            }
        }
        return b;
    }
    
    public void jump() {
        if(jumpTimer == 0 && hitsGround()) {
            jumpTimer = 15;
            motion.add(0, 1);
        }
    }
    
    public static boolean collides(Vector2d pos, Rectangle2D hitBox) {
        return Maths2D.collides(getHitbox(pos), hitBox);
    }
    
    public static boolean collides(Vector2d pos, Vector2i block) {
        return Maths2D.collides(getHitbox(pos), new Rectangle2D(new Vector2d(block.getX(), block.getY()), new Vector2d(1,1)));
    }
    
    public static boolean cXp(Vector2d pos, World world) {
        boolean b = false;
        Vector2i blockPos = new Vector2i((int) pos.getX() + 1, (int) (pos.getY() - 1));
        for (int y = 0; y < 3; y++) {
            if(world.getBlock(blockPos.getX(), blockPos.getY()).type.solid && collides(pos, blockPos)) {
                b = true;
                break;
            }
            blockPos.add(new Vector2i(0, 1));
        }
        return b;
    }
    
    public static boolean cXn(Vector2d pos, World world) {
        boolean b = false;
        Vector2i blockPos = new Vector2i((int) pos.getX() - 1, (int) (pos.getY() - 1));
        for (int y = 0; y < 3; y++) {
            if(world.getBlock(blockPos.getX(), blockPos.getY()).type.solid && collides(pos, blockPos)) {
                b = true;
                break;
            }
            blockPos.add(new Vector2i(0, 1));
        }
        return b;
    }
    
    public static boolean cYp(Vector2d pos, World world) {
        boolean b = false;
        Vector2i blockPos = new Vector2i((int) pos.getX() - 1, (int) (pos.getY() + 1));
        for (int y = 0; y < 3; y++) {
            if(world.getBlock(blockPos.getX(), blockPos.getY()).type.solid && collides(pos, blockPos)) {
                b = true;
                break;
            }
            blockPos.add(new Vector2i(1, 0));
        }
        return b;
    }
    
    public static boolean cYn(Vector2d pos, World world) {
        boolean b = false;
        Vector2i blockPos = new Vector2i((int) pos.getX() - 1, (int) (pos.getY()));
        for (int x = 0; x < 3; x++) {
            if(world.getBlock(blockPos.getX() + x, blockPos.getY()).type.solid && collides(pos, blockPos)) {
                b = true;
                break;
            }
        }
        return b;
    }
    
    public Rectangle2D getHitbox() {
        return getHitbox(pos);
    }
    
    public static Rectangle2D getHitbox(Vector2d pos) {
        return new Rectangle2D(new Vector2d(pos.getX() - 0.25, pos.getY() + 1), new Vector2d(0.5,1));
    }
    
    public Vector2i getRealPos() {
        double x;
        double y;
        
        if(pos.getX() < 0)
            x = (Math.ceil(pos.getX()) - 0.5);
        else
            x = (Math.floor(pos.getX()) + 0.5);
        
        if(pos.getY() < 0)
            y = (Math.ceil(pos.getY()) - 0.5);
        else
            y = (Math.floor(pos.getY()) + 0.5);
        
        return new Vector2i((int) Math.floor(x), (int) Math.floor(y));
    }
    
    public void tickMotion() {
        jumpTimer--;
        if(jumpTimer < 0)
            jumpTimer = 0;
        try {
            Vector2d pos = this.pos.clone();
            pos.add(motion);
            if(!hitsBlock(pos.clone().add(0, 0.01), world)) {
                this.pos.set(pos);
                motion.multiply(0.75);
                if(motion.getX() <= 0.01 && motion.getX() >= -0.01) {
                    motion.setX(0);
                }
                if(motion.getY() <= 0.01 && motion.getY() >= -0.01) {
                    motion.setY(0);
                }
            }
            else {
                pos = this.pos.clone();
                motion.multiply(0.01);
                while (!hitsBlock(pos.clone().add(0, 0), world))
                    pos.add(motion);
                this.pos.set(pos);
                motion.multiply(0);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            this.pos.add(motion);
            motion.multiply(0.25);
            if(motion.getX() < 0.01) {
                motion.setX(0);
            }
            if(motion.getY() < 0.01) {
                motion.setY(0);
            }
        }
    }
    /*
    public static boolean hitsGround(Vector2d pos, World world) {
        boolean[] rel;
        boolean b;
        boolean[] var4;
        int var5;
        int var6;
        boolean value;
        if (world.getBlock((int)pos.getX(), (int)(pos.getY() + 1.0D)).type.solid) {
            rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((double)((int)pos.getX()), (double)((int)(pos.getY() - 0.5D))), new Vector2d(1.0D, 1.0D)));
            b = false;
            var4 = rel;
            var5 = rel.length;
            
            for(var6 = 0; var6 < var5; ++var6) {
                value = var4[var6];
                if (value) {
                    b = true;
                    break;
                }
            }
            
            return b;
        } else if (world.getBlock((int)(pos.getX() + 0.23D), (int)(pos.getY() + 1.0D)).type.solid) {
            rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((double)((int)pos.getX()), (double)((int)(pos.getY() - 0.5D))), new Vector2d(1.0D, 1.0D)));
            b = false;
            var4 = rel;
            var5 = rel.length;
            
            for(var6 = 0; var6 < var5; ++var6) {
                value = var4[var6];
                if (value) {
                    b = true;
                    break;
                }
            }
            
            return b;
        } else if (!world.getBlock((int)(pos.getX() - 0.25D), (int)(pos.getY() + 1.0D)).type.solid) {
            return false;
        } else {
            rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((double)((int)pos.getX()), (double)((int)(pos.getY() - 0.5D))), new Vector2d(1.0D, 1.0D)));
            b = false;
            var4 = rel;
            var5 = rel.length;
            
            for(var6 = 0; var6 < var5; ++var6) {
                value = var4[var6];
                if (value) {
                    b = true;
                    break;
                }
            }
            
            return b;
        }
    }
    
    public static boolean hitsBlock(Vector2d pos, World world) {
        boolean[] rel;
        boolean b;
        boolean[] var4;
        int var5;
        int var6;
        boolean value;
        if (world.getBlock((int)pos.getX(), (int)(pos.getY() + 1.0D)).type.solid) {
            rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((double)((int)pos.getX()), (double)((int)(pos.getY() - 0.5D))), new Vector2d(1.0D, 1.0D)));
            b = false;
            var4 = rel;
            var5 = rel.length;
            
            for(var6 = 0; var6 < var5; ++var6) {
                value = var4[var6];
                if (value) {
                    b = true;
                    break;
                }
            }
            
            return b;
        } else if (world.getBlock((int)pos.getX(), (int)(pos.getY() + 3.0D)).type.solid) {
            rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((double)((int)pos.getX()), (double)((int)(pos.getY() + 2.5D))), new Vector2d(1.0D, 1.0D)));
            b = false;
            var4 = rel;
            var5 = rel.length;
            
            for(var6 = 0; var6 < var5; ++var6) {
                value = var4[var6];
                if (value) {
                    b = true;
                    break;
                }
            }
            
            return b;
        } else if (world.getBlock((int)(pos.getX() + 0.23D), (int)(pos.getY() + 1.0D)).type.solid) {
            rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((double)((int)pos.getX()), (double)((int)(pos.getY() - 0.5D))), new Vector2d(1.0D, 1.0D)));
            b = false;
            var4 = rel;
            var5 = rel.length;
            
            for(var6 = 0; var6 < var5; ++var6) {
                value = var4[var6];
                if (value) {
                    b = true;
                    break;
                }
            }
            
            return b;
        } else if (!world.getBlock((int)(pos.getX() - 0.25D), (int)(pos.getY() + 1.0D)).type.solid) {
            return false;
        } else {
            rel = Maths2D.getRelation(getHitbox(pos), new Rectangle2D(new Vector2d((double)((int)pos.getX()), (double)((int)(pos.getY() - 0.5D))), new Vector2d(1.0D, 1.0D)));
            b = false;
            var4 = rel;
            var5 = rel.length;
            
            for(var6 = 0; var6 < var5; ++var6) {
                value = var4[var6];
                if (value) {
                    b = true;
                    break;
                }
            }
            
            return b;
        }
    }*/
    
    public void kill() {
        world.entities.remove(this);
    }
}
