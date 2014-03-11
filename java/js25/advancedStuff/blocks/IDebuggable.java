package js25.advancedStuff.blocks;

import js25.advancedStuff.items.ItemDebugger;

public interface IDebuggable {

    public abstract Object performDebug(ItemDebugger.DebugModes debugMode, Object in);

    public abstract boolean isValid(ItemDebugger.DebugModes debugMode);

}
