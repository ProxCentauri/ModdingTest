package js25.advancedStuff.blocks;

/**
 * Intended for tile entities which need to sync data between server and client
 */
interface IServerClientSync {

    /**
     * Should send all necessary NBT-saved data to the client
     * Called from the TE itself or an adjacent updater
     */
    abstract void syncClient();

    abstract boolean receiveClientEvent(int id, int value);

}
