package macrophage.dig.handler;

import com.google.common.io.Files;
import macrophage.dig.api.ResourceRegistry;
import macrophage.dig.api.resource.IResource;
import macrophage.dig.api.resource.Resource;
import macrophage.dig.helper.ResourceHelper;
import macrophage.dig.util.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ScriptHandler {
    public static File[] findScripts() throws IOException {
        File script_dir = Paths.get(ConfigHandler.Features.SCRIPTS_FOLDER).toFile();

        if (!script_dir.exists()) script_dir.mkdirs();

        FilenameFilter script_ext = (dir, name) -> name.endsWith(ModInfo.SCRIPT_EXT);

        return script_dir.listFiles(script_ext);
    }

    public static boolean parseScripts() throws IOException {
        File[] scripts = findScripts();

        if (!Objects.equals(scripts, null) && scripts.length > 0) {
            for (File script : scripts) {
                parseScript(script);
            }
        }

        return ResourceRegistry.hasResources();
    }

    public static void parseScript(File file) throws IOException {
        List<String> lines = Files.readLines(file, Charset.defaultCharset());

        if (lines.size() > 0) {
            for (String line : lines) processLine(line);
        }
    }

    private static void processLine(String line) {
        IResource res = ResourceHelper.deserialize(line);
        ResourceRegistry.register(res, null);
    }
}
