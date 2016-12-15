package macrophage.dig.util;

public class ModInfo {
    public static final String MODID   = "dig";
    public static final String NAME    = "Dig";
    public static final String VERSION = "1.0.10";

    public class COMMANDS {
        public static final String ADD_RESOURCE   = "res_add";
        public static final String LIST_RESOURCES = "res_list";
        public static final String REM_RESOURCE   = "res_rem";
        public static final String RES_GET_NAME   = "res_name";
        public static final String EDIT_RESOURCE  = "res_edit";

        public class USAGE {
            public static final String ADD_RESOURCE   = "/res_add [(block_modid)block_registry/oredict_name:metadata=durability|(drop_modid)drop_registry/oredict_name:metadata=drop_chance]";
            public static final String REM_RESOURCE   = "/res_rem [all/block_registry/oredict_name]";
            public static final String LIST_RESOURCES = "/res_list [all/drops/blocks] [registry_name]";
            public static final String EDIT_RESOURCE  = "/res_edit [index] [(block_modid)block_registry/oredict_name:metadata=durability|(drop_modid)drop_registry/oredict_name:metadata=drop_chance]";
        }
    }
}
