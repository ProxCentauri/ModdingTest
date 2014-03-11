package js25.advancedStuff.gui;

import js25.advancedStuff.items.ItemDebugger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerDebugger extends Container {

    public ItemDebugger item;
    public ItemStack itemStack;

    public ContainerDebugger(InventoryPlayer inventoryPlayer, ItemStack itemStack) {
        this.itemStack = itemStack;
        this.item = (ItemDebugger)itemStack.getItem();

    }

    @Override
    public boolean canInteractWith(EntityPlayer p) {
        return true;
    }
}
