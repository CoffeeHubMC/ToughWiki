package me.theseems.toughwiki;

// The constants are replaced before compilation
public class BuildConstants {

    public static final String VERSION = "${version}";
    public static final Boolean DEV_BUILD = Boolean.valueOf("${devBuild}");
}
