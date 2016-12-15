package macrophage.dig.helper;

public class StringHelper {
    public static String[] split(String toSplit, char delim) {
        int pos = 0;

        CharSequence cs = String.valueOf(delim);

        if (toSplit.contains(cs)) {
            for (int i = 0; i < toSplit.length(); i++) {
                if (toSplit.charAt(i) == delim) {
                    pos = i;
                }
            }
        } else {
            return new String[] {
                    toSplit,
                    "-1"
            };
        }

        return new String[]{
                toSplit.substring(0, pos),
                toSplit.substring(pos+1)
        };
    }
}
