package tudbut.squarey.client;

import de.tudbut.type.Vector2d;
import tudbut.squarey.world.entity.EntityPlayer;

public class ClientPlayer extends EntityPlayer {
    public ClientPlayer(Vector2d pos) {
        super(pos, Client.getInstance().world);
    }
}
