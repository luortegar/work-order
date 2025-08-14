package cl.creando.skappserver.common;

public class CommonFunctions {
    public static String getPattern(String searchTerm) {
        return "%" + searchTerm + "%";
    }

    public static boolean compareStrings(String string1, String string2) {
        if (string1 == null && string2 == null) {
            return true;
        }
        if (string1 == null || string2 == null) {
            return false;
        }
        return string1.equals(string2);
    }
}
