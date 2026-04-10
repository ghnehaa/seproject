package utils;

public class Display {

    public static void printBanner(String title) {
        int width = 56;
        String line = "=".repeat(width);
        System.out.println("\n" + line);
        int pad = (width - title.length()) / 2;
        System.out.println(" ".repeat(Math.max(0, pad)) + title);
        System.out.println(line);
    }

    public static void printSectionHeader(String title) {
        System.out.println("\n── " + title + " " + "─".repeat(Math.max(0, 40 - title.length())));
    }

    public static void printSuccess(String msg) {
        System.out.println("  [✓] " + msg);
    }

    public static void printError(String msg) {
        System.out.println("  [✗] " + msg);
    }

    public static void printInfo(String msg) {
        System.out.println("  [i] " + msg);
    }

    public static void printDivider() {
        System.out.println("  " + "-".repeat(50));
    }
}
