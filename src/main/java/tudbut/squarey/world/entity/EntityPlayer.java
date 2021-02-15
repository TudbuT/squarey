package tudbut.squarey.world.entity;

import de.tudbut.type.Vector2d;
import tudbut.squarey.client.communication.ClientConnection;
import tudbut.squarey.item.Inventory;
import tudbut.squarey.server.communication.ServerConnection;
import tudbut.squarey.world.World;

public class EntityPlayer extends Entity {
    
    public ClientConnection clientConnection;
    public ServerConnection serverConnection;
    
    public EntityPlayer(Vector2d pos, World world) {
        super(EntityType.PLAYER, pos, world);
        inventory = new Inventory(9, 3);
    }
}
