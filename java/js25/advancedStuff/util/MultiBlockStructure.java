package js25.advancedStuff.util;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class MultiBlockStructure {

    private Block mainBlock;
    int height, width, depth, mainLayerPos, mainBlockPos;
    private Block[][] blocks;
    private int[][] meta;

    /**
     * @param blocks The blocks required for a valid structure. Format: Block[Layer][Blocks]; <code>null</code> ignores placed blocks
     */
    public MultiBlockStructure(Block mainBlock, int height, int width, int depth, Block[][] blocks) {
        this.mainBlock = mainBlock;
        this.height = height;
        this.width = width;
        this.depth = depth;
        this.blocks = blocks;

        for(int i = 0; i < this.blocks.length; i++) {
            if(this.blocks[i] == null) continue;
            for(int j = 0; j < this.blocks[i].length; j++)
                if(this.blocks[i][j] == this.mainBlock) {
                    mainLayerPos = i;
                    mainBlockPos = j;
                }
        }

        int maxLayers = 0;
        int maxBlocks = 0;
        for(int i = 0; i < this.blocks.length; i++) {
            maxLayers = i + 1;
            maxBlocks = this.blocks[i] != null && this.blocks[i].length > maxBlocks ? this.blocks[i].length : maxBlocks;
        }
        if(maxLayers != height || maxBlocks != width * depth)
            throw new IllegalArgumentException(String.format("Incorrect boundings for MultiBlockStructure: [%d, %d, %d]", this.height, this.width, this.depth));

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
        int startY = mainY - mainLayerPos;
        int startX = mainX - mainBlockPos % width;
        int startZ = mainZ - mainBlockPos / depth;

        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                for(int k = 0; k < depth; k++) {
                    if(blocks[i] == null) continue;
                    if(blocks[i][j + k * width] == null) continue;
                    if(blocks[i][j + k * width] == world.getBlock(startX + j, startY + i, startZ + k)) continue;
                    return false;
                }
        return true;
    }

    private boolean checkValidationEast(World world, int mainX, int mainY, int mainZ) {
        int startY = mainY - mainLayerPos;
        int startX = mainX + mainBlockPos / width;
        int startZ = mainZ - mainBlockPos % depth;

        for(int i = 0; i < height; i++)
            for(int j = 0; j < depth; j++)
                for(int k = 0; k < width; k++) {
                    if(blocks[i] == null) continue;
                    if(blocks[i][j * width + k] == null) continue;
                    if(blocks[i][j * width + k] == world.getBlock(startX - j, startY + i, startZ + k)) continue;
                    return false;
                }
        return true;
    }

    private boolean checkValidationSouth(World world, int mainX, int mainY, int mainZ) {
        int startY = mainY - mainLayerPos;
        int startX = mainX + mainBlockPos % width;
        int startZ = mainZ + mainBlockPos / depth;

        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                for(int k = 0; k < depth; k++) {
                    if(blocks[i] == null) continue;
                    if(blocks[i][j + k * width] == null) continue;
                    if(blocks[i][j + k * width] == world.getBlock(startX - j, startY + i, startZ - k)) continue;
                    return false;
                }
        return true;
    }

    private boolean checkValidationWest(World world, int mainX, int mainY, int mainZ) {
        int startY = mainY - mainLayerPos;
        int startX = mainX - mainBlockPos / width;
        int startZ = mainZ + mainBlockPos % depth;

        for(int i = 0; i < height; i++)
            for(int j = 0; j < depth; j++)
                for(int k = 0; k < width; k++) {
                    if(blocks[i] == null) continue;
                    if(blocks[i][j * width + k] == null) continue;
                    if(blocks[i][j * width + k] == world.getBlock(startX + j, startY + i, startZ - k)) continue;
                    return false;
                }
        return true;
    }

    public void setMeta(int[][] meta) { this.meta = meta; };



}
