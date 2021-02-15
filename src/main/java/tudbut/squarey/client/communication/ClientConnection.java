package tudbut.squarey.client.communication;

import de.tudbut.tools.Tools;
import de.tudbut.type.Vector2d;
import tudbut.net.ic.PBIC;
import tudbut.obj.Vector2i;
import tudbut.squarey.client.Client;
import tudbut.squarey.client.ClientPlayer;
import tudbut.squarey.world.World;
import tudbut.squarey.world.block.BlockType;

public class ClientConnection {
    
    public final PBIC.Connection connection;
    private final ClientPlayer player;
    
    public ClientConnection(PBIC.Connection connection, ClientPlayer player) {
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
                        player.entityID = Integer.parseInt(content);
                    }
                    if(id == 3) {
                        player.world = World.deserialize(content, Client.getInstance().logger);
                    }
                    if(id == 4) {
                        Vector2i pos = Vector2i.fromMap(Tools.stringToMap(content.split(" ")[0]));
                        player.world.setBlock(pos.getX(), pos.getY(), BlockType.values()[Integer.parseInt(content.split(" ")[1])]);
                    }
                }
            } catch (Throwable throwable) {
                System.exit(0);
            }
        }, "Client receive thread").start();
    }
    
    public void writeMotion() throws PBIC.PBICException.PBICWriteException {
        connection.writePacket(() -> "\u0000\u0000" + player.motion.toString());
    }
    
    public void writePosition() throws PBIC.PBICException.PBICWriteException {
        connection.writePacket(() -> "\u0000\u0001" + player.pos.toString());
    }
    
    public void writeBlockUpdate(Vector2i vec) throws PBIC.PBICException.PBICWriteException {
        connection.writePacket(() -> "\u0000\u0002" + vec.toString() + " " + player.world.getBlock(vec.getX(), vec.getY()).type.ordinal());
    }
}
