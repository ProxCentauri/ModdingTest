package js25.advancedStuff.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import js25.advancedStuff.items.ItemDebugger;
import js25.advancedStuff.lib.Textures;
import js25.advancedStuff.util.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiDebugger extends GuiContainer {

    public enum ScreenStates {
        SMALL, LARGE, RESIZING
    }

    //finals
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation(Textures.LOCATION_NAME, "textures/gui/debugger_small.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation(Textures.LOCATION_NAME, "textures/gui/debugger_large.png");
    private static final int RESIZING_DURATION_MS = 750;
    //references
    private ContainerDebugger container;
    //persistent
    ItemDebugger.DebugModes curMode;
    //working
    private ForgeDirection resizingDirection;
    private long resizeStarted;
    private ScreenStates curScreenState;
    float modeScale;

    public GuiDebugger(InventoryPlayer inventoryPlayer, ItemStack itemStack) {
        super(new ContainerDebugger(inventoryPlayer, itemStack));

        container = (ContainerDebugger)inventorySlots;
        curMode = ItemDebugger.getSavedMode(itemStack);
        xSize = 256;
        ySize = getScreenStateForMode(curMode) == ScreenStates.LARGE ? 256 : 128;
        curScreenState = getScreenStateForMode(curMode);

        try {
            modeScale = Float.valueOf(StatCollector.translateToLocal("gui.debugger.mode.scale"));
        } catch(Exception e) {
            Log.addWarning("Couldn't load property 'gui.debugger.mode.scale' in %s, change it to a valid float value", Minecraft.getMinecraft().gameSettings.language + ".lang");
            modeScale = 1F;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        GL11.glColor4f(1, 1, 1, 1);

        if(curScreenState == ScreenStates.SMALL) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_1);
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        } else if(curScreenState == ScreenStates.LARGE) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_2);
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        } else if(curScreenState == ScreenStates.RESIZING) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_2);
            float factor = curScreenState == ScreenStates.RESIZING && resizeStarted != -1 ? (resizingDirection == ForgeDirection.UP ? 1 - ((float)(Minecraft.getSystemTime() - resizeStarted) / RESIZING_DURATION_MS) : (float)(Minecraft.getSystemTime() - resizeStarted) / RESIZING_DURATION_MS) : 1;
            ySize = 128 + (int)(128 * factor);
            initGui();
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize - 12);
            drawTexturedModalRect(guiLeft, guiTop + ySize - 12, 0, 256 - 12, xSize, 12);

            if(factor >= 1 || factor <= 0) {
                curScreenState = getScreenStateForMode(curMode);
                resizeStarted = -1;
                ySize = curScreenState == ScreenStates.SMALL ? 128 : 256;
                initGui();
            }
        }



    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawCenteredString(fontRendererObj, StatCollector.translateToLocal("item.debugger.name"), xSize - xSize / 2, 10, 0xDDDDDD);


        GL11.glScalef(modeScale, modeScale, modeScale);
        drawCenteredString(fontRendererObj, StatCollector.translateToLocal(curMode.getLocalizedName()), (int)(xSize / (2 * modeScale)), (int)(128 / (2 * modeScale)) - fontRendererObj.FONT_HEIGHT / 2, 0xDDDDDD);


    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        container.itemStack.stackTagCompound.setString("selectedMode", curMode.toString());
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);

        if(button != 2) return;

        for(ItemDebugger.DebugModes cur : ItemDebugger.DebugModes.values()) {
            if(curMode.ordinal() + 1 == cur.ordinal()) {
                changeMode(cur);
                return;
            }
        }
        changeMode(ItemDebugger.DebugModes.INFO);

    }

    private void changeMode(ItemDebugger.DebugModes newMode) {
        ItemDebugger.DebugModes oldMode = curMode;
        if(oldMode == newMode) return;
        curMode = newMode;
        if(getScreenStateForMode(newMode) != getScreenStateForMode(oldMode)) {
            if(curScreenState == ScreenStates.RESIZING) {
                resizeStarted = Minecraft.getSystemTime() - (RESIZING_DURATION_MS - (Minecraft.getSystemTime() - resizeStarted));
            } else {
                curScreenState = ScreenStates.RESIZING;
                resizeStarted = Minecraft.getSystemTime();
            }
            resizingDirection = getScreenStateForMode(newMode) == ScreenStates.SMALL ? ForgeDirection.UP : ForgeDirection.DOWN;
            initGui();
        }

    }

    private ScreenStates getScreenStateForMode(ItemDebugger.DebugModes mode) {
        if(mode == ItemDebugger.DebugModes.INFO) return ScreenStates.SMALL;
        if(mode == ItemDebugger.DebugModes.INJECT || mode == ItemDebugger.DebugModes.SET_BLOCK || mode == ItemDebugger.DebugModes.SET_META) return ScreenStates.LARGE;
        return null;
    }

}
