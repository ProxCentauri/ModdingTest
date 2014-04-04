package js25.advancedStuff.items;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import js25.advancedStuff.AdvancedStuff;
import js25.advancedStuff.blocks.IDebuggable;
import js25.advancedStuff.gui.GuiHandler;
import js25.advancedStuff.lib.Textures;
import js25.advancedStuff.util.Log;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.LinkedList;
import java.util.List;

public class ItemDebugger extends Item {

	public ItemDebugger() {
		super();
	}

    public enum DebugModes {
        INFO("info"),
        SET_BLOCK("block"),
        SET_META("meta"),
        INJECT("inject");

        public String unlocalizedName;

        private DebugModes(String unlocalized) {
            this.unlocalizedName = unlocalized;
        }

        public String getLocalizedName() {
            return StatCollector.translateToLocal("item.debugger.mode." + unlocalizedName);
        }
    }

    //icon stuff
	@SideOnly(Side.CLIENT)
	private IIcon icon;
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister reg) {
		icon = reg.registerIcon(Textures.ITEM_DEBUGGER);
	}	

    @SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int dmg) {
		return icon;
	}


    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        return super.getItemStackDisplayName(itemStack) + EnumChatFormatting.YELLOW + " [" + getSavedMode(itemStack).getLocalizedName() + "]" + EnumChatFormatting.RESET;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {

        if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.add(EnumChatFormatting.DARK_AQUA + "" + EnumChatFormatting.ITALIC + "Hold shift to show extra information");
            return;
        } else {
            list.add(EnumChatFormatting.DARK_AQUA + "" + EnumChatFormatting.ITALIC + "Release shift to hide extra information");
        }

        list.add("- Right click a block to get information");
        list.add("- Right click while sneaking to change modes");

    }


    @Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer p, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(world.isRemote || p.isSneaking()) return false;

        if(getSavedMode(stack) == DebugModes.INFO) {
            int meta = world.getBlockMetadata(x, y, z);

            Log.chatMessage(p, EnumChatFormatting.BLUE + "--- Debugger ---");
            Log.chatMessage(p, "Name: %s [%s]", world.getBlock(x, y, z).getLocalizedName(), Block.blockRegistry.getNameForObject(world.getBlock(x, y, z)));
            Log.chatMessage(p, "Metadata: %s", meta);
            Log.chatMessage(p, "Hardness: %s, Resistance: %s", world.getBlock(x, y, z).getBlockHardness(world, x, y, z), world.getBlock(x, y, z).getExplosionResistance(p, world, x, y, z, p.posX, p.posY, p.posZ));
            Log.chatMessage(p, world.getTileEntity(x, y, z) != null ? "This Block has a tile entity" : "This Block has no tile entity");

            if(world.getTileEntity(x, y, z) != null) {
                Log.chatMessage(p, EnumChatFormatting.GREEN + "- Tile-Data -");
                TileEntity tile = world.getTileEntity(x, y, z);

                if(tile instanceof IDebuggable && ((IDebuggable)tile).isValid(DebugModes.INFO)) {
                    List<String> info = new LinkedList<String>();
                    info = (LinkedList<String>)((IDebuggable)tile).performDebug(DebugModes.INFO, info);
                    if(info != null && info.size() > 0) {
                        for(String text : info)
                            Log.chatMessage(p, text);
                    } else {
                        Log.addWarning("%s should provide info for %s but returns an empty object", tile.getClass().getName(), "DebugModes.INFO");
                    }
                } else if(tile instanceof IInventory) {
                    IInventory inv = (IInventory)tile;
                    Log.chatMessage(p, "Name: %s", inv.getInventoryName());
                    int filledRelative = 0;
                    float filledAbsolute = 0;
                    for(int i = 0; i < inv.getSizeInventory(); i++) {
                        ItemStack curStack = inv.getStackInSlot(i);
                        if(curStack != null && curStack.stackSize > 0) {
                            ++filledRelative;
                            filledAbsolute += (float)curStack.stackSize / curStack.getMaxStackSize();
                        }
                    }
                    Log.chatMessage(p, "%d of %d slots have at least one item", filledRelative, inv.getSizeInventory());
                    Log.chatMessage(p, "%s%% filled up, %s%% of the slots have at least one item", Log.castToDigits(1, (filledAbsolute / inv.getSizeInventory()) * 100), Log.castToDigits(1, ((float)filledRelative / inv.getSizeInventory()) * 100));
                } else {
                    Log.chatMessage(p, EnumChatFormatting.RED + "[!] Unknown or unsupported tile entity: '" + tile.getClass().getSimpleName() + "'");
                    Log.addWarning("Unknown tile entity: " + tile.getClass().getName());
                }
            }
            return true;
        } else if(getSavedMode(stack) == DebugModes.INJECT) {
            TileEntity tile = world.getTileEntity(x, y, z);

            if(tile != null && tile instanceof IDebuggable && ((IDebuggable)tile).isValid(DebugModes.INJECT)) {
                Log.chatMessage(p, EnumChatFormatting.BLUE + "--- Debugger ---");

                List<String> info = new LinkedList<String>();
                info = (LinkedList<String>)((IDebuggable)tile).performDebug(DebugModes.INJECT, info);
                if(info != null && info.size() > 0)
                    for(String text : info)
                        Log.chatMessage(p, text);

                return true;
            }
        }
        return false;
	}

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
        if(p.isSneaking() && !world.isRemote) FMLNetworkHandler.openGui(p, AdvancedStuff.instance, GuiHandler.GUIs.DEBUGGER.id, world, 0, 0, 0);
        return stack;
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer p) {
        itemStack.stackTagCompound = new NBTTagCompound();
        itemStack.stackTagCompound.setString("selectedMode", DebugModes.INFO.toString());
    }

    public static DebugModes getSavedMode(ItemStack itemStack) {
        try {
            return DebugModes.valueOf(itemStack.stackTagCompound.getString("selectedMode"));
        } catch(Exception ex) {
            ex.printStackTrace();
            itemStack.getItem().onCreated(itemStack, null, null);
            return DebugModes.INFO;
        }
    }
}






