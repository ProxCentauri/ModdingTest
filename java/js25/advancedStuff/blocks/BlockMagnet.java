package js25.advancedStuff.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import js25.advancedStuff.lib.Textures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockMagnet extends BlockContainer {

    public BlockMagnet(Material material) {
        super(material);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityMagnet();
    }

    @SideOnly(Side.CLIENT)
    private IIcon textureBlank;
    @SideOnly(Side.CLIENT)
    private IIcon textureSide;
    @SideOnly(Side.CLIENT)
    private IIcon textureTopInactive;
    @SideOnly(Side.CLIENT)
    private IIcon textureTopActive;

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister reg) {
        textureBlank = reg.registerIcon(Textures.BLOCK_MAGNET_BLANK);
        textureSide = reg.registerIcon(Textures.BLOCK_MAGNET_SIDE);
        textureTopInactive = reg.registerIcon(Textures.BLOCK_MAGNET_TOP_INACTIVE);
        textureTopActive = reg.registerIcon(Textures.BLOCK_MAGNET_TOP_ACTIVE);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if(ForgeDirection.getOrientation(side) == ForgeDirection.UP) return isActive(meta) ? textureTopActive : textureTopInactive;
        return ForgeDirection.getOrientation(side) == ForgeDirection.DOWN ? textureBlank : textureSide;
    }

    public static boolean isPowered(int meta) {
        return meta % 2 == 1;
    }

    public static boolean isActive(int meta) {
        return meta / 2 == 1;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighborBlock) {
        int meta = world.getBlockMetadata(x, y, z);
        boolean worldPowered = world.isBlockIndirectlyGettingPowered(x, y, z);
        if(!isPowered(meta) && worldPowered) {
            world.setBlockMetadataWithNotify(x, y, z, ++meta, 2);
        } else if(isPowered(meta) && !worldPowered) {
            world.setBlockMetadataWithNotify(x, y, z, --meta, 2);
        }
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        return world.isBlockIndirectlyGettingPowered(x, y, z) ? ++meta : meta;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(world.isRemote || !player.isSneaking() || ForgeDirection.getOrientation(side) != ForgeDirection.UP) return false;
        int meta = world.getBlockMetadata(x, y, z);
        if(!isActive(meta)) {
            world.setBlockMetadataWithNotify(x, y, z, meta + 2, 2);
        } else if(isActive(meta)) {
            world.setBlockMetadataWithNotify(x, y, z, meta - 2, 2);
        }
        return true;
    }
}







