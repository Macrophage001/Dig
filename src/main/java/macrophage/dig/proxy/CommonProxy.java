package macrophage.dig.proxy;

import macrophage.dig.handler.ScriptHandler;

import java.io.IOException;

public class CommonProxy {
    public void postInit() {
        try {
            ScriptHandler.parseScripts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
