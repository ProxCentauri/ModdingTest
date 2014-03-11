package js25.advancedStuff.util;

import js25.advancedStuff.lib.ModInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;

public class Log{

    public static Logger logger;

    public static void add(String format, Object... args) {
        logger.info("[" + ModInfo.NAME + "] " + String.format(format, args));
    }

    public static void addDebug(String format, Object... args) {
        add("[Debug] " + format, args);
    }

    public static void addWarning(String format, Object... args) {
        logger.warn("[" + ModInfo.NAME + "] " + String.format(format, args));
    }

    public static void chatMessage(EntityPlayer player, String format, Object... args) {
    	player.addChatComponentMessage(new ChatComponentText(String.format(format, args)));
    }

    public static void globalChatMessage(World world, String format, Object... args) {
        for(Object player : world.playerEntities) {
            chatMessage((EntityPlayer)player, format, args);
        }
    }

    public static String castToDigits(int digits, float num) {
        String s = "";
        for(int i = 0; i < digits; i++) s += "#";
        DecimalFormat df = new DecimalFormat("#." + s);
        return df.format(num);
    }
    
    
    
    
}
