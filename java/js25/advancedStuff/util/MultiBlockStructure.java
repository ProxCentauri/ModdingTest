package js25.advancedStuff.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class MultiBlockStructure {

    private Block mainBlock;
    int[] mainBlockPos;
    private Block[][] blocks;
    private int[][] meta;

    public MultiBlockStructure(Block mainBlock, Block[][] blocks) {
        this.mainBlock = mainBlock;
        this.blocks = blocks;
        for(int i = 0; i < this.blocks.length; i++) {
            if(this.blocks[i] == null) continue;
            for(int j = 0; j < this.blocks[i].length; j++)
                if(this.blocks[i][j] == this.mainBlock) mainBlockPos = new int[] {i, j};
        }
    }

    public boolean isValidStructure(World world, int x, int y, int z) {
        if(world.getBlock(x, y, z) != mainBlock) return false;

        if(checkValidationNorth(world, x, y, z)) return true;
        if(checkValidationEast(world, x, y, z)) return true;
        if(checkValidationSouth(world, x, y, z)) return true;
        if(checkValidationWest(world, x, y, z)) return true;
        return false;
    }

    private boolean checkValidationNorth(World world, int mainX, int mainY, int mainZ) {
        int startY = mainY - mainBlockPos[0];
        int startX = mainX - mainBlockPos[1] % 3;
        int startZ = mainZ - mainBlockPos[1] / 3;

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                for(int k = 0; k < 3; k++) {
                    if(blocks[i] == null) continue;
                    if(blocks[i][j + k * 3] == null) continue;
                    if(blocks[i][j + k * 3] == world.getBlock(startX + j, startY + i, startZ + k)) continue;
                    return false;
                }
        return true;
    }

    private boolean checkValidationEast(World world, int mainX, int mainY, int mainZ) {
        int startY = mainY - mainBlockPos[0];
        int startX = mainX + mainBlockPos[1] / 3;
        int startZ = mainZ - mainBlockPos[1] % 3;

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                for(int k = 0; k < 3; k++) {
                    if(blocks[i] == null) continue;
                    if(blocks[i][j * 3 + k] == null) continue;
                    if(blocks[i][j * 3 + k] == world.getBlock(startX - j, startY + i, startZ + k)) continue;
                    return false;
                }
        return true;
    }

    private boolean checkValidationSouth(World world, int mainX, int mainY, int mainZ) {
        int startY = mainY - mainBlockPos[0];
        int startX = mainX + mainBlockPos[1] % 3;
        int startZ = mainZ + mainBlockPos[1] / 3;

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                for(int k = 0; k < 3; k++) {
                    if(blocks[i] == null) continue;
                    if(blocks[i][j + k * 3] == null) continue;
                    if(blocks[i][j + k * 3] == world.getBlock(startX - j, startY + i, startZ - k)) continue;
                    return false;
                }
        return true;
    }

    private boolean checkValidationWest(World world, int mainX, int mainY, int mainZ) {
        int startY = mainY - mainBlockPos[0];
        int startX = mainX - mainBlockPos[1] / 3;
        int startZ = mainZ + mainBlockPos[1] % 3;

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                for(int k = 0; k < 3; k++) {
                    if(blocks[i] == null) continue;
                    if(blocks[i][j * 3 + k] == null) continue;
                    if(blocks[i][j * 3 + k] == world.getBlock(startX + j, startY + i, startZ - k)) continue;
                    return false;
                }
        return true;
    }

    public void setMeta(int[][] meta) { this.meta = meta; };



}
