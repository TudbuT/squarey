package tudbut.squarey.world.structure;

import tudbut.squarey.world.block.BlockType;

public class StructureTree extends Structure {
    
    public StructureTree() {
        super(
                "" +
                " ##### \n" +
                "#######\n" +
                "###|###\n" +
                "###|###\n" +
                " ##|## \n" +
                "   |   \n" +
                "   |   \n" +
                "   —   ",
                
                new Association('|', BlockType.LOG),
                new Association('#', BlockType.LEAVES),
                new Association('-', BlockType.GRASS),
                new Association('—', BlockType.GRASS, true)
        );
    }
}
