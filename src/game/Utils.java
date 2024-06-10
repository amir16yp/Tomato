package game;

import game.entities.player.PlayerEntity;

public class Utils
{
    public static Runtime runtime = Runtime.getRuntime();
    private static final long KB = 1024;
    private static final long MB = KB * KB;
    private static final long GB = MB * KB;
    private static final long TB = GB * KB;

    public static String humanReadableByteCount(long bytes) {
        if (bytes < KB) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(KB));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %sB", bytes / Math.pow(KB, exp), pre);
    }

    public static class ConsoleColors {
        // Reset
        public static final String RESET = "\033[0m";  // Text Reset

        // Regular Colors
        public static final String BLACK = "\033[0;30m";   // BLACK
        public static final String RED = "\033[0;31m";     // RED
        public static final String GREEN = "\033[0;32m";   // GREEN
        public static final String YELLOW = "\033[0;33m";  // YELLOW
        public static final String BLUE = "\033[0;34m";    // BLUE
        public static final String PURPLE = "\033[0;35m";  // PURPLE
        public static final String CYAN = "\033[0;36m";    // CYAN
        public static final String WHITE = "\033[0;37m";   // WHITE

        // Bold Colors
        public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
        public static final String RED_BOLD = "\033[1;31m";    // RED
        public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
        public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
        public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
        public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
        public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
        public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

        // Underline Colors
        public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
        public static final String RED_UNDERLINED = "\033[4;31m";    // RED
        public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
        public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
        public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
        public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
        public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
        public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

        // Background Colors
        public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
        public static final String RED_BACKGROUND = "\033[41m";    // RED
        public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
        public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
        public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
        public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
        public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
        public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

        // High Intensity Colors
        public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
        public static final String RED_BRIGHT = "\033[0;91m";    // RED
        public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
        public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
        public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
        public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
        public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
        public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

        // Bold High Intensity Colors
        public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
        public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
        public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
        public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
        public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
        public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
        public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
        public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

        // High Intensity Background Colors
        public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
        public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
        public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
        public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
        public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
        public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
        public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
        public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE

        public static String ASCIIColorToHTML(String asciiString) {
            return asciiString
                    .replace(RESET, "</span>") // Reset
                    .replace(BLACK, "<span style=\"color:black;\">") // BLACK
                    .replace(RED, "<span style=\"color:red;\">") // RED
                    .replace(GREEN, "<span style=\"color:green;\">") // GREEN
                    .replace(YELLOW, "<span style=\"color:yellow;\">") // YELLOW
                    .replace(BLUE, "<span style=\"color:blue;\">") // BLUE
                    .replace(PURPLE, "<span style=\"color:purple;\">") // PURPLE
                    .replace(CYAN, "<span style=\"color:cyan;\">") // CYAN
                    .replace(WHITE, "<span style=\"color:white;\">") // WHITE
                    .replace(BLACK_BOLD, "<span style=\"color:black;font-weight:bold;\">") // BLACK_BOLD
                    .replace(RED_BOLD, "<span style=\"color:red;font-weight:bold;\">") // RED_BOLD
                    .replace(GREEN_BOLD, "<span style=\"color:green;font-weight:bold;\">") // GREEN_BOLD
                    .replace(YELLOW_BOLD, "<span style=\"color:yellow;font-weight:bold;\">") // YELLOW_BOLD
                    .replace(BLUE_BOLD, "<span style=\"color:blue;font-weight:bold;\">") // BLUE_BOLD
                    .replace(PURPLE_BOLD, "<span style=\"color:purple;font-weight:bold;\">") // PURPLE_BOLD
                    .replace(CYAN_BOLD, "<span style=\"color:cyan;font-weight:bold;\">") // CYAN_BOLD
                    .replace(WHITE_BOLD, "<span style=\"color:white;font-weight:bold;\">") // WHITE_BOLD
                    .replace(BLACK_UNDERLINED, "<span style=\"color:black;text-decoration:underline;\">") // BLACK_UNDERLINED
                    .replace(RED_UNDERLINED, "<span style=\"color:red;text-decoration:underline;\">") // RED_UNDERLINED
                    .replace(GREEN_UNDERLINED, "<span style=\"color:green;text-decoration:underline;\">") // GREEN_UNDERLINED
                    .replace(YELLOW_UNDERLINED, "<span style=\"color:yellow;text-decoration:underline;\">") // YELLOW_UNDERLINED
                    .replace(BLUE_UNDERLINED, "<span style=\"color:blue;text-decoration:underline;\">") // BLUE_UNDERLINED
                    .replace(PURPLE_UNDERLINED, "<span style=\"color:purple;text-decoration:underline;\">") // PURPLE_UNDERLINED
                    .replace(CYAN_UNDERLINED, "<span style=\"color:cyan;text-decoration:underline;\">") // CYAN_UNDERLINED
                    .replace(WHITE_UNDERLINED, "<span style=\"color:white;text-decoration:underline;\">") // WHITE_UNDERLINED
                    .replace(BLACK_BACKGROUND, "<span style=\"background-color:black;\">") // BLACK_BACKGROUND
                    .replace(RED_BACKGROUND, "<span style=\"background-color:red;\">") // RED_BACKGROUND
                    .replace(GREEN_BACKGROUND, "<span style=\"background-color:green;\">") // GREEN_BACKGROUND
                    .replace(YELLOW_BACKGROUND, "<span style=\"background-color:yellow;\">") // YELLOW_BACKGROUND
                    .replace(BLUE_BACKGROUND, "<span style=\"background-color:blue;\">") // BLUE_BACKGROUND
                    .replace(PURPLE_BACKGROUND, "<span style=\"background-color:purple;\">") // PURPLE_BACKGROUND
                    .replace(CYAN_BACKGROUND, "<span style=\"background-color:cyan;\">") // CYAN_BACKGROUND
                    .replace(WHITE_BACKGROUND, "<span style=\"background-color:white;\">") // WHITE_BACKGROUND
                    .replace(BLACK_BRIGHT, "<span style=\"color:black;\">") // BLACK_BRIGHT
                    .replace(RED_BRIGHT, "<span style=\"color:red;\">") // RED_BRIGHT
                    .replace(GREEN_BRIGHT, "<span style=\"color:green;\">") // GREEN_BRIGHT
                    .replace(YELLOW_BRIGHT, "<span style=\"color:yellow;\">") // YELLOW_BRIGHT
                    .replace(BLUE_BRIGHT, "<span style=\"color:blue;\">") // BLUE_BRIGHT
                    .replace(PURPLE_BRIGHT, "<span style=\"color:purple;\">") // PURPLE_BRIGHT
                    .replace(CYAN_BRIGHT, "<span style=\"color:cyan;\">") // CYAN_BRIGHT
                    .replace(WHITE_BRIGHT, "<span style=\"color:white;\">") // WHITE_BRIGHT
                    .replace(BLACK_BOLD_BRIGHT, "<span style=\"color:black;font-weight:bold;\">") // BLACK_BOLD_BRIGHT
                    .replace(RED_BOLD_BRIGHT, "<span style=\"color:red;font-weight:bold;\">") // RED_BOLD_BRIGHT
                    .replace(GREEN_BOLD_BRIGHT, "<span style=\"color:green;font-weight:bold;\">") // GREEN_BOLD_BRIGHT
                    .replace(YELLOW_BOLD_BRIGHT, "<span style=\"color:yellow;font-weight:bold;\">") // YELLOW_BOLD_BRIGHT
                    .replace(BLUE_BOLD_BRIGHT, "<span style=\"color:blue;font-weight:bold;\">") // BLUE_BOLD_BRIGHT
                    .replace(PURPLE_BOLD_BRIGHT, "<span style=\"color:purple;font-weight:bold;\">") // PURPLE_BOLD_BRIGHT
                    .replace(CYAN_BOLD_BRIGHT, "<span style=\"color:cyan;font-weight:bold;\">") // CYAN_BOLD_BRIGHT
                    .replace(WHITE_BOLD_BRIGHT, "<span style=\"color:white;font-weight:bold;\">") // WHITE_BOLD_BRIGHT
                    .replace(BLACK_BACKGROUND_BRIGHT, "<span style=\"background-color:black;\">") // BLACK_BACKGROUND_BRIGHT
                    .replace(RED_BACKGROUND_BRIGHT, "<span style=\"background-color:red;\">") // RED_BACKGROUND_BRIGHT
                    .replace(GREEN_BACKGROUND_BRIGHT, "<span style=\"background-color:green;\">") // GREEN_BACKGROUND_BRIGHT
                    .replace(YELLOW_BACKGROUND_BRIGHT, "<span style=\"background-color:yellow;\">") // YELLOW_BACKGROUND_BRIGHT
                    .replace(BLUE_BACKGROUND_BRIGHT, "<span style=\"background-color:blue;\">") // BLUE_BACKGROUND_BRIGHT
                    .replace(PURPLE_BACKGROUND_BRIGHT, "<span style=\"background-color:purple;\">") // PURPLE_BACKGROUND_BRIGHT
                    .replace(CYAN_BACKGROUND_BRIGHT, "<span style=\"background-color:cyan;\">") // CYAN_BACKGROUND_BRIGHT
                    .replace(WHITE_BACKGROUND_BRIGHT, "<span style=\"background-color:white;\">");
        }
    }
}
