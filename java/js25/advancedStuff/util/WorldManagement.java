package js25.advancedStuff.util;

/**
 * @deprecated
 * Use {@link net.minecraftforge.common.util.ForgeDirection ForgeDirection} instead
 */
@Deprecated
public class WorldManagement {

    //texture side ids of blocks
    public static final int SIDE_TEXTURE_BOTTOM = 0;
    public static final int SIDE_TEXTURE_TOP = 1;
    public static final int SIDE_TEXTURE_NORTH = 2;
    public static final int SIDE_TEXTURE_SOUTH = 3;
    public static final int SIDE_TEXTURE_WEST = 4;
    public static final int SIDE_TEXTURE_EAST = 5;

    //icon side ids of blockIcons
    public static final int SIDE_ICON_FRONT = SIDE_TEXTURE_WEST;
    public static final int SIDE_ICON_RIGHT = SIDE_TEXTURE_SOUTH;
    public static final int SIDE_ICON_TOP = SIDE_TEXTURE_TOP;

    //ids of directions int the world
    public static final int DIRECTION_NORTH = 0;
    public static final int DIRECTION_EAST = 1;
    public static final int DIRECTION_SOUTH = 2;
    public static final int DIRECTION_WEST = 3;

    public static int getDirection(float rotation) {
		if(rotation > 315 || rotation <= 45) return 0;
		if(rotation > 45 && rotation <= 135) return 1;
		if(rotation > 155 && rotation <= 225) return 2;
		if(rotation > 225 && rotation <= 315) return 3;
		return -1;
	}
	
	public static int getTextureSide(int direction) {
		return direction == DIRECTION_NORTH ? SIDE_TEXTURE_NORTH : direction == DIRECTION_EAST ? SIDE_TEXTURE_EAST : direction == DIRECTION_SOUTH ? SIDE_TEXTURE_SOUTH : direction == DIRECTION_WEST ? SIDE_TEXTURE_WEST : -1;
	}
	

    
    
    
	
}
