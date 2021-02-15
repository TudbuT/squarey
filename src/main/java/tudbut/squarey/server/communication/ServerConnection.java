package tudbut.squarey.server.communication;

import de.tudbut.tools.Tools;
import de.tudbut.type.Vector2d;
import tudbut.net.ic.PBIC;
import tudbut.obj.Vector2i;
import tudbut.squarey.client.ClientPlayer;
import tudbut.squarey.server.Server;
import tudbut.squarey.world.block.BlockType;
import tudbut.squarey.world.entity.Entity;
import tudbut.squarey.world.entity.EntityPlayer;

import java.nio.charset.StandardCharsets;

public class ServerConnection {
    
    public final PBIC.Connection connection;
    private final EntityPlayer player;
    
    public ServerConnection(PBIC.Connection connection, EntityPlayer player) {
        this.connection = connection;
        this.player = player;
    }
    
    public void run() {
        new Thread(() -> {
            try {
                while (true) {
                    PBIC.Packet packet = connection.readPacket();
                    String s = packet.getContent();
                    char[] idString = s.substring(0, 2).toCharArray();
                    int id = idString[1] + (idString[0] << 8);
                    String content = s.substring(2);
                    
                    if(id == 0) {
                        player.motion.set(Vector2d.fromMap(Tools.stringToMap(content)));
                    }
                    if(id == 1) {
                        player.pos.set(Vector2d.fromMap(Tools.stringToMap(content)));
                    }
                    if(id == 2) {
                        Vector2i pos = Vector2i.fromMap(Tools.stringToMap(content.split(" ")[0]));
                        player.world.setBlock(pos.getX(), pos.getY(), BlockType.values()[Integer.parseInt(content.split(" ")[1])]);
    
                        Entity[] entities = player.world.entities.toArray(new Entity[0]);
                        Entity entity;
                        for (int i = 0; i < entities.length; i++) {
                            entity = entities[i];
                            if(entity instanceof EntityPlayer)
                                ((EntityPlayer) entity).serverConnection.writeBlockUpdate(pos);
                        }
                    }
                }
            } catch (Throwable throwable) {
            }
        }, "Server receive thread").start();
    }
    
    public void writeMotion() throws PBIC.PBICException.PBICWriteException {
        connection.writePacket(() -> "\u0000\u0000" + player.motion.toString());
    }
    
    public void writePosition() throws PBIC.PBICException.PBICWriteException {
        connection.writePacket(() -> "\u0000\u0001" + player.pos.toString());
    }
    
    public void writeEntityID() throws PBIC.PBICException.PBICWriteException {
        connection.writePacket(() -> "\u0000\u0002" + player.entityID);
    }
    
    public void writeWorld() throws PBIC.PBICException.PBICWriteException {
        connection.writePacket(() -> "\u0000\u0003" + player.world.serialize());
    }
    
    public void writeBlockUpdate(Vector2i vec) throws PBIC.PBICException.PBICWriteException {
        connection.writePacket(() -> "\u0000\u0004" + vec.toString() + " " + player.world.getBlock(vec.getX(), vec.getY()).type.ordinal());
    }
    
}
