package tudbut.squarey.world.structure;

import tudbut.net.ic.PBIC;
import tudbut.obj.Vector2i;
import tudbut.squarey.client.Client;
import tudbut.squarey.world.Chunk;
import tudbut.squarey.world.block.BlockType;
import tudbut.squarey.world.entity.EntityPlayer;

public class Structure {
    
    private BlockType[][] blocks;
    private Vector2i relativeBase;
    
    protected Structure(String structureIn, Association... map) {
        int y = structureIn.split("\n").length;
        int x = (structureIn.length()) / y + 1;
        blocks = new BlockType[x][y];
        char[] structureAsArray = structureIn.toCharArray();
        int ix = 0;
        int iy = y - 1;
        for (int i = 0; i < structureAsArray.length; i++) {
            if(structureAsArray[i] == '\n') {
                ix = 0;
                iy--;
                continue;
            }
            boolean b = false;
            for (int j = 0; j < map.length; j++) {
                if(map[j].a == structureAsArray[i]) {
                    if(map[j].c)
                        relativeBase = new Vector2i(ix, iy);
                    try {
                        blocks[ix][iy] = map[j].b;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println(ix);
                        System.out.println(iy);
                        System.out.println(x);
                        System.out.println(y);
                    }
                    b = true;
                }
            }
            if(!b)
                blocks[ix][iy] = null;
            ix++;
        }
    }
    
    public BlockType[][] build() {
        return blocks.clone();
    }
    
    public void generate(Chunk[] world, Vector2i baseLoc, boolean isUsingTool) {
        baseLoc = baseLoc.clone().add(relativeBase.clone().negate());
        BlockType[][] types = build();
        for (int x = 0; x < types.length; x++) {
            for (int y = 0; y < types[x].length; y++) {
                if(types[x][y] != null) {
                    int lx = x + baseLoc.getX(), ly = y + baseLoc.getY();
                    try {
                        world[lx / 16].blocks[lx % 16][ly] = types[x][y].create(new Vector2i(lx, ly));
                        if(Client.getInstance() != null && Client.getInstance().viewEntity instanceof EntityPlayer && isUsingTool) {
                            ((EntityPlayer) Client.getInstance().viewEntity).clientConnection.writeBlockUpdate(new Vector2i(lx, ly));
                        }
                    } catch (ArrayIndexOutOfBoundsException | PBIC.PBICException.PBICWriteException ignored) {
                    
                    }
                }
            }
        }
    }
    
    protected static class Association {
        public final char a;
        public final BlockType b;
        public final boolean c;
    
        public Association(char a, BlockType b) {
            this.a = a;
            this.b = b;
            this.c = false;
        }
        public Association(char a, BlockType b, boolean c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
}
