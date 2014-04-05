package js25.advancedStuff.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import js25.advancedStuff.lib.Textures;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBuilder extends BlockContainer {

    public BlockBuilder(Material material) {
        super(material);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int a) {
        return new TileEntityBuilder();
    }

    @SideOnly(Side.CLIENT)
    private IIcon textureBlank;
    @SideOnly(Side.CLIENT)
    private IIcon textureTop;
    @SideOnly(Side.CLIENT)
    private IIcon textureActive;

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        textureBlank = reg.registerIcon(Textures.BLOCK_BUILDER_BLANK);
        textureTop = reg.registerIcon(Textures.BLOCK_BUILDER_TOP);
        textureActive = reg.registerIcon(Textures.BLOCK_BUILDER_ACTIVE);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return ForgeDirection.getOrientation(side) == ForgeDirection.UP ? textureTop : textureBlank;
    }

    @Override
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
        if(!((TileEntityBuilder)access.getTileEntity(x, y, z)).isValidStructure)
            return ForgeDirection.getOrientation(side) == ForgeDirection.UP ? textureTop : null;
        return ForgeDirection.getOrientation(side) == ForgeDirection.UP ? textureTop : textureBlank;
    }

}












