package tudbut.squarey.world.entity;

import de.tudbut.type.Vector2d;
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
    
    public void jump() {
        if(jumpTimer == 0 && hitsGround()) {
            jumpTimer = 15;
            motion.add(0, 1);
        }
    }
    
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
    
    public Rectangle2D getHitbox() {
        return getHitbox(pos);
    }
    
    public static Rectangle2D getHitbox(Vector2d pos) {
        return new Rectangle2D(new Vector2d(pos.getX() - 0.25, pos.getY() + 1), new Vector2d(0.5,1));
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
                motion.set(motion.multiply(0.01));
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
    
    public void kill() {
        world.entities.remove(this);
    }
}
