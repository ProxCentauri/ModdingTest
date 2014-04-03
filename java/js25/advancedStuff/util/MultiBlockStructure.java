package js25.advancedStuff.util;

import js25.advancedStuff.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class MultiBlockStructure {

    private Block mainBlock;
    int[] mainBlockPos;
    private Block[][] blocks;
    private int[][] meta;

    public MultiBlockStructure(Block mainBlock, Block[][] blocks) {
        this.mainBlock = mainBlock;
        this.blocks = blocks;
        for(int i = 0; i < blocks.length; i++)
            for(int j = 0; j < blocks[i].length; j++)
                if(blocks[i][j] == mainBlock) mainBlockPos = new int[] {i, j};

    }

    //TODO remove
    public static void createDump() {
        new MultiBlockStructure(ModRegistry.Blocks.BUILDER.instance, new Block[][] {null, {null, null, null, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, null, ModRegistry.Blocks.BUILDER.instance, null}, null});
    }

    public boolean isValidStructure(World world, int x, int y, int z) {
        if(world.getBlock(x, y, z) != mainBlock) return false;
        if(checkValidationWest(world, x, y, z)) return true;
        return false;
    }

    private boolean checkValidationWest(World world, int mainX, int mainY, int mainZ) {
        int startY = mainY - mainBlockPos[0];
        int startX = mainX - mainBlockPos[1] % 3;
        int startZ = mainZ - mainBlockPos[1] / 3;

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                for(int k = 0; k < 3; k++) {
                    if(blocks[i][j + k * 3] == null) continue;
                    if(blocks[i][j + k * 3] == world.getBlock(startX + j, startY + i, startZ + k)) continue;
                    return false;
                }


        return false;
    }

    public void setMeta(int[][] meta) { this.meta = meta; };



}
