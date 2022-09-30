package me.theseems.toughwiki.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class TextUtils {
    private static final MiniMessage miniMessage = MiniMessage.builder().strict(false).tags(StandardTags.defaults()).build();

    public static Component parse(String string) {
        return GsonComponentSerializer.gson()
                .deserialize(GsonComponentSerializer.gson().serialize(parseText(string)));
    }

    @NotNull
    private static Component parseText(String text) {
        if (text.contains("&") || text.contains("§")) {
            text = replaceLegacy(text);
        }

        Component component = miniMessage.deserialize(text);
        if (component.decorations().get(TextDecoration.ITALIC) == TextDecoration.State.NOT_SET) {
            component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        }

        return component.colorIfAbsent(NamedTextColor.WHITE);
    }

    @NotNull
    private static String replaceLegacy(@NotNull String text) {
        text = text.replace('\u00A7', '&');
        return text.replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<gray>")
                .replace("&8", "<dark_gray>")
                .replace("&9", "<blue>")
                .replaceAll("&[Aa]", "<green>")
                .replaceAll("&[Bb]", "<aqua>")
                .replaceAll("&[Cc]", "<red>")
                .replaceAll("&[Dd]", "<light_purple>")
                .replaceAll("&[Ee]", "<yellow>")
                .replaceAll("&[Ff]", "<white>")
                .replaceAll("&[Kk]", "<obf>")
                .replaceAll("&[Ll]", "<b>")
                .replaceAll("&[Mm]", "<st>")
                .replaceAll("&[Nn]", "<u>")
                .replaceAll("&[Oo]", "<i>");
    }
}
