package js25.advancedStuff.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import js25.advancedStuff.lib.Textures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCamouflage extends BlockContainer {

    public BlockCamouflage(Material material) {
        super(material);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int a) {
        return new TileEntityCamouflage();
    }

    @SideOnly(Side.CLIENT)
    private IIcon connectedIron;

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        connectedIron = reg.registerIcon(Textures.BLOCK_CAMOUFLAGE_CONNECTED_IRON);
    }

    @Override
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
        TileEntityCamouflage tile = (TileEntityCamouflage)access.getTileEntity(x, y, z);
        if(tile.cover instanceof BlockCamouflage || tile.cover == null) {
            return null;
        }

        if(tile.owner != null && tile.owner.isValidStructure)
            return connectedIron;

        return tile.cover.getIcon(side, access.getBlockMetadata(x, y, z));
    }

    @Override
    public Item getItem(World world, int x, int y, int z) {
        if(((TileEntityCamouflage)world.getTileEntity(x, y, z)).cover == null) return Item.getItemFromBlock(this);
        return Item.getItemFromBlock(((TileEntityCamouflage)world.getTileEntity(x, y, z)).cover);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return null;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        if(world.getTileEntity(x, y, z) == null) return 1F;
        if(((TileEntityCamouflage)world.getTileEntity(x, y, z)).cover instanceof BlockCamouflage || ((TileEntityCamouflage)world.getTileEntity(x, y, z)).cover == null) return 1F;
        return ((TileEntityCamouflage)world.getTileEntity(x, y, z)).cover.getBlockHardness(world, x, y, z);
    }

    @Override
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
        if(world.getTileEntity(x, y, z) == null) return 1F;
        if(((TileEntityCamouflage)world.getTileEntity(x, y, z)).cover instanceof BlockCamouflage || ((TileEntityCamouflage)world.getTileEntity(x, y, z)).cover == null) return 1F;
        return ((TileEntityCamouflage)world.getTileEntity(x, y, z)).cover.getExplosionResistance(entity, world, x, y, z, explosionX, explosionY, explosionZ);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block b, int meta) {
        if(((TileEntityCamouflage)world.getTileEntity(x, y, z)).cover != null && !(((TileEntityCamouflage)world.getTileEntity(x, y, z)).cover instanceof BlockCamouflage))
            ((TileEntityCamouflage)world.getTileEntity(x, y, z)).cover.dropBlockAsItem(world, x, y, z, meta, 0);
        super.breakBlock(world, x, y, z, b, meta);
    }

    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        Block block = ((TileEntityCamouflage)worldObj.getTileEntity(target.blockX, target.blockY, target.blockZ)).cover;
        if (block.getMaterial() != Material.air) {
            Random rand = new Random();
            float f = 0.1F;
            double d0 = (double)target.blockX + rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinX();
            double d1 = (double)target.blockY + rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinY();
            double d2 = (double)target.blockZ + rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinZ();

            switch(target.sideHit) {
                case 0: d1 = (double)target.blockY + block.getBlockBoundsMinY() - (double)f; break;
                case 1: d1 = (double)target.blockY + block.getBlockBoundsMaxY() + (double)f; break;
                case 2: d2 = (double)target.blockZ + block.getBlockBoundsMinZ() - (double)f; break;
                case 3: d2 = (double)target.blockZ + block.getBlockBoundsMaxZ() + (double)f; break;
                case 4: d0 = (double)target.blockX + block.getBlockBoundsMinX() - (double)f; break;
                case 5: d0 = (double)target.blockX + block.getBlockBoundsMaxX() + (double)f; break;
            }
            effectRenderer.addEffect((new EntityDiggingFX(worldObj, d0, d1, d2, 0.0D, 0.0D, 0.0D, block, worldObj.getBlockMetadata(target.blockX, target.blockY, target.blockZ))).applyColourMultiplier(target.blockX, target.blockY, target.blockZ).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        byte b0 = 4;
        for (int i1 = 0; i1 < b0; ++i1)
            for (int j1 = 0; j1 < b0; ++j1)
                for (int k1 = 0; k1 < b0; ++k1) {
                    double d0 = (double)x + ((double)i1 + 0.5D) / (double)b0;
                    double d1 = (double)y + ((double)j1 + 0.5D) / (double)b0;
                    double d2 = (double)z + ((double)k1 + 0.5D) / (double)b0;
                    effectRenderer.addEffect((new EntityDiggingFX(world, d0, d1, d2, d0 - (double)x - 0.5D, d1 - (double)y - 0.5D, d2 - (double)z - 0.5D, ((TileEntityCamouflage)world.getTileEntity(x, y, z)).cover, meta)).applyColourMultiplier(x, y, z));
                }
        return true;
    }



}





