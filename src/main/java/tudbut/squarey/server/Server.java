package tudbut.squarey.server;

import de.tudbut.tools.FileRW;
import de.tudbut.type.Vector2d;
import tudbut.logger.Logger;
import tudbut.logger.LoggerSink;
import tudbut.net.ic.PBIC;
import tudbut.squarey.server.communication.ServerConnection;
import tudbut.squarey.world.World;
import tudbut.squarey.world.entity.EntityPlayer;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Server {
    private static Server instance; { instance = this; }
    public static Server getInstance() {
        return instance;
    }
    
    public World world;
    public LoggerSink logger = new Logger("Squarey-Server");
    
    public static void main(String[] args) {
        new Server(23432);
    }
    
    public Server(int port) {
    
    
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
            catch (IOException e) {
                e.printStackTrace();
            }
        }, "World save thread"));
    
        try {
            PBIC.Server server = new PBIC.Server(port);
            server.onJoin.add(() -> {
                PBIC.Connection connection = server.lastConnection;
                EntityPlayer player = new EntityPlayer(new Vector2d(256 * 16,300), world);
                player.serverConnection = new ServerConnection(connection, player);
                try {
                    logger.info("Player logged in with entity ID " + player.entityID);
                    player.serverConnection.writeWorld();
                    logger.info("World sent");
                    player.serverConnection.writePosition();
                }
                catch (PBIC.PBICException.PBICWriteException e) {
                    e.printStackTrace();
                }
                player.serverConnection.run();
            });
            server.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    
        while (true) {
            long sa = new Date().getTime();
        
            for (int i = 0; i < world.entities.size(); i++) {
                if(!(world.entities.get(i) instanceof EntityPlayer))
                    world.entities.get(i).applyGravity();
            }
        
            for (int i = 0; i < world.entities.size(); i++) {
                world.entities.get(i).tickMotion();
            }
        
            try {
                Thread.sleep(1000 / 50 - (new Date().getTime() - sa));
            }
            catch (Exception ignore) {
                logger.info("Tick took too long!");
            }
        }
    }
}
