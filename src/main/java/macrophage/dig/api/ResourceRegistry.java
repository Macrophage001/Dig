package macrophage.dig.api;

import macrophage.dig.api.resource.IResource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ResourceRegistry {
    private static List<IResource> resources = new ArrayList<IResource>();

    public static void register(IResource resource, @Nullable Integer index) {
        if (resource != null) {
            if (index == null) {
                resources.add(resource);
            } else {
                resources.add(index, resource);
            }
        }
    }

    public static List<IResource> getResources() {
        return resources;
    }

    public static void remove(Integer index) {
        resources.remove(index.intValue());
    }

    public static void removeAll() {
        resources.clear();
    }

    public static boolean hasResources() {
        return resources.size() > 0;
    }
}
