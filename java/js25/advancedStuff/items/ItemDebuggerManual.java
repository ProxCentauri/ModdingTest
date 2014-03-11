package js25.advancedStuff.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import js25.advancedStuff.blocks.IDebuggable;
import js25.advancedStuff.lib.Textures;
import js25.advancedStuff.util.Log;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.LinkedList;
import java.util.List;

public class ItemDebuggerManual extends Item {

	public ItemDebuggerManual() {
		super();
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

    //visual / type stuff
    private static enum types {
        BLOCK_INFO(0, 15),
        INC_ID(16, 79),
        DEC_ID(80, 143),
        INC_META(144, 159),
        SET_META(160, 175);

        private int min_value;
        private int max_value;

        private types(int min, int max) {
            min_value = min;
            max_value = max;
        }

        private static types getTypeForDamage(int dmg) {
            if(dmg >= BLOCK_INFO.min_value && dmg <= BLOCK_INFO.max_value) return BLOCK_INFO;
            if(dmg >= INC_ID.min_value && dmg <= INC_ID.max_value) return INC_ID;
            if(dmg >= DEC_ID.min_value && dmg <= DEC_ID.max_value) return DEC_ID;
            if(dmg >= INC_META.min_value && dmg <= INC_META.max_value) return INC_META;
            if(dmg >= SET_META.min_value && dmg <= SET_META.max_value) return SET_META;
            return null;
        }

        private static int getTypeModifier(int dmg) {
            return (dmg / getTypeForDamage(dmg).min_value - 1) * getTypeForDamage(dmg).min_value + dmg % getTypeForDamage(dmg).min_value;
        }

        private static types getNextType(types type) {
            return getTypeForDamage(type.max_value + 1);
        }

        private static types getPrevType(types type) {
            return getTypeForDamage(type.min_value - 1);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {

        String s = "";
        try {
            switch(types.getTypeForDamage(stack.getItemDamage())) {
                case BLOCK_INFO: s = "Block Info"; break;
                case INC_ID: s = "Increase Block ID"; break;
                case DEC_ID: s = "Decrease Block ID"; break;
                case INC_META: s = "Increase Block Meta"; break;
                case SET_META: s = "Set Block Meta"; break;
                default: s = ""; break;
            }
        } catch(NullPointerException ex) {
            s = "ERROR";
        } finally {
            return super.getItemStackDisplayName(stack) + EnumChatFormatting.YELLOW + " [" + s + "]" + EnumChatFormatting.RESET;
        }
    }

    @SideOnly(Side.CLIENT)
    long nextKeySwitch = 0;

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean debug) {

        if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            list.add(EnumChatFormatting.DARK_AQUA + "" + EnumChatFormatting.ITALIC + "Hold shift to show extra information");
            return;
        } else {
            list.add(EnumChatFormatting.DARK_AQUA + "" + EnumChatFormatting.ITALIC + "Release shift to hide extra information");
        }


        int dmg = stack.getItemDamage();
        types curType = types.getTypeForDamage(dmg);

        if(curType == null) {
            list.add(EnumChatFormatting.DARK_RED + "Invalid item damge: " + dmg + EnumChatFormatting.RESET);
            return;
        }
        if(curType == types.BLOCK_INFO) list.add("Shows debug information about blocks and tile entities");
        if(curType == types.INC_ID) list.add(String.format("Increases the block id by %d", (types.getTypeModifier(dmg)) + 1));
        if(curType == types.DEC_ID) list.add(String.format("Decreases the block id by %d", (types.getTypeModifier(dmg)) + 1));
        if(curType == types.INC_META) list.add(String.format("Increases the block meta by %d", (types.getTypeModifier(dmg)) + 1));
        if(curType == types.SET_META) list.add(String.format("Sets the block meta to %d", types.getTypeModifier(dmg)));

        if(curType != types.BLOCK_INFO) list.add(EnumChatFormatting.GOLD + "Use Arrow keys (up and down) to switch values");
        list.add(EnumChatFormatting.GOLD + "Use Arrow keys (left and right) to switch modes");

        if(Minecraft.getSystemTime() < nextKeySwitch) return;

        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && types.getNextType(curType) != null) {
            stack.setItemDamage(types.getNextType(curType).min_value);
            nextKeySwitch = Minecraft.getSystemTime() + 400;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) && types.getPrevType(curType) != null) {
            stack.setItemDamage(types.getPrevType(curType).min_value);
            nextKeySwitch = Minecraft.getSystemTime() + 400;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_UP) && types.getTypeForDamage(dmg + 1) == curType) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                stack.setItemDamage(types.getTypeForDamage(dmg + 8) == curType ? dmg + 8 : curType.max_value);
            } else {
                stack.setItemDamage(dmg + 1);
            }
            nextKeySwitch = Minecraft.getSystemTime() + 200;
        } else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && types.getTypeForDamage(dmg - 1) == curType) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                stack.setItemDamage(types.getTypeForDamage(dmg - 8) == curType ? dmg - 8 : curType.min_value);
            } else {
                stack.setItemDamage(dmg - 1);
            }
            nextKeySwitch = Minecraft.getSystemTime() + 200;
        }


    }


    @Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer p, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if(world.isRemote) return false;

        types curType = types.getTypeForDamage(stack.getItemDamage());
        int meta = world.getBlockMetadata(x, y, z);

        if(curType == types.BLOCK_INFO) {
            Log.chatMessage(p, EnumChatFormatting.BLUE + "--- Debugger ---");
            Log.chatMessage(p, "Name: %s [%s]", world.getBlock(x, y, z).getLocalizedName(), Block.blockRegistry.getNameForObject(world.getBlock(x, y, z)));
            Log.chatMessage(p, "Metadata: %s", meta);
            Log.chatMessage(p, "Hardness: %s, Resistance: %s", world.getBlock(x, y, z).getBlockHardness(world, x, y, z), world.getBlock(x, y, z).getExplosionResistance(p, world, x, y, z, p.posX, p.posY, p.posZ));
            Log.chatMessage(p, world.getTileEntity(x, y, z) != null ? "This Block has a tile entity" : "This Block has no tile entity");

            if(world.getTileEntity(x, y, z) != null) {
                Log.chatMessage(p, EnumChatFormatting.GREEN + "- Tile-Data -");
                TileEntity tile = world.getTileEntity(x, y, z);

                if(tile instanceof IDebuggable) {
                    List<String> info = new LinkedList<String>();
                    ((IDebuggable)tile).getDebugInfo(info);
                    if(info.size() > 0) {
                        for(String text : info)
                            Log.chatMessage(p, text);
                    } else {
                        Log.addWarning("%s is an instance of %s but doesn't provide any debug info", tile.getClass().getName(), IDebuggable.class.getSimpleName());
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
        } else if(curType == types.INC_ID) {

        } else if(curType == types.DEC_ID) {

        }

        return true;
	}

}






