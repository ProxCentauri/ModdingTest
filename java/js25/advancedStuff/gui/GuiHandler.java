package js25.advancedStuff.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import js25.advancedStuff.AdvancedStuff;
import js25.advancedStuff.util.Log;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

public class GuiHandler implements IGuiHandler {

    public GuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(AdvancedStuff.instance, this);
    }

    private enum Types {
        BOUND, PORTABLE;
    }

    public enum GUIs {
        DEBUGGER(0, Types.PORTABLE, GuiDebugger.class, ContainerDebugger.class);

        public int id;
        private Types type;
        private Class<? extends GuiContainer> guiClass;
        private Class<? extends Container> containerClass;

        private GUIs(int id, Types type, Class<? extends GuiContainer> guiClass, Class<? extends Container> containerClass) {
            this.id = id;
            this.type = type;
            this.guiClass = guiClass;
            this.containerClass = containerClass;
        }

        public static GUIs getGUIsById(int id) {
            for(GUIs cur : GUIs.values())
                if(id == cur.id) return cur;
            return null;
        }
    }

    private Object getServerGuiElement(GUIs gui, EntityPlayer p, World world, int x, int y, int z) {
        try {
            if(gui.type == Types.BOUND) return (Container)gui.containerClass.getConstructor(InventoryPlayer.class, TileEntity.class).newInstance(p.inventory, world.getTileEntity(x, y, z));
            if(gui.type == Types.PORTABLE) return (Container)gui.containerClass.getConstructor(InventoryPlayer.class, ItemStack.class).newInstance(p.inventory, p.inventory.getCurrentItem());
        } catch(InvocationTargetException ex) {
            Log.addWarning("Exception caused by the experimental reflection management within %s", this.getClass().getName());
            ex.getCause().printStackTrace();
            return null;
        } catch (Exception ex) {
            Log.addWarning("Unexpected exception caused by the experimental reflection management within %s", this.getClass().getName());
            ex.printStackTrace();
        }
        return null;
    }

    private Object getClientGuiElement(GUIs gui, EntityPlayer p, World world, int x, int y, int z) {
        try {
            if(gui.type == Types.BOUND) return (GuiContainer)gui.guiClass.getConstructor(InventoryPlayer.class, TileEntity.class).newInstance(p.inventory, world.getTileEntity(x, y, z));
            if(gui.type == Types.PORTABLE) return (GuiContainer)gui.guiClass.getConstructor(InventoryPlayer.class, ItemStack.class).newInstance(p.inventory, p.inventory.getCurrentItem());
        } catch(InvocationTargetException ex) {
            Log.addWarning("Exception caused by the experimental reflection management within %s", this.getClass().getName());
            ex.getCause().printStackTrace();
            return null;
        } catch (Exception ex) {
            Log.addWarning("Unexpected exception caused by the experimental reflection management within %s", this.getClass().getName());
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer p, World world, int x, int y, int z) {
        return getServerGuiElement(GUIs.getGUIsById(id), p, world, x, y, z);
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer p, World world, int x, int y, int z) {
        return getClientGuiElement(GUIs.getGUIsById(id), p, world, x, y, z);
    }
}
