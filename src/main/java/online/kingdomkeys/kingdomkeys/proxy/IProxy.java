package online.kingdomkeys.kingdomkeys.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Proxy interface for both client and server
 */
public interface IProxy {
    //Setup event for the proxy
    void setup(final FMLCommonSetupEvent event);

}
