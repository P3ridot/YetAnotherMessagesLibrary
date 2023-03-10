package dev.peri.yetanothermessageslibrary.adventure;

import dev.peri.yetanothermessageslibrary.replace.StringReplacer;
import dev.peri.yetanothermessageslibrary.replace.replacement.Replacement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class AdventureHelper {

    private static final Map<String, String> LEGACY_TO_MINI_MESSAGE = new HashMap<String, String>() {{
        this.put("&0", "<black>");
        this.put("&1", "<dark_blue>");
        this.put("&2", "<dark_green>");
        this.put("&3", "<dark_aqua>");
        this.put("&4", "<dark_red>");
        this.put("&5", "<dark_purple>");
        this.put("&6", "<gold>");
        this.put("&7", "<gray>");
        this.put("&8", "<dark_gray>");
        this.put("&9", "<blue>");
        this.put("&a", "<green>");
        this.put("&b", "<aqua>");
        this.put("&c", "<red>");
        this.put("&d", "<light_purple>");
        this.put("&e", "<yellow>");
        this.put("&f", "<white>");
        this.put("&k", "<obf>");
        this.put("&l", "<b>");
        this.put("&m", "<st>");
        this.put("&n", "<underlined>");
        this.put("&o", "<i>");
        this.put("&r", "<r>");
    }};
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("&(#[a-fA-F0-9]{6})");

    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

    /**
     * Replace legacy color codes in text with mini message codes.
     *
     * @param legacyText legacy text
     * @return mini message text
     */
    public static @NotNull String legacyToMiniMessage(@NotNull String legacyText) {
        Matcher hexMatcher = HEX_COLOR_PATTERN.matcher(legacyText);
        while (hexMatcher.find()) {
            String hex = hexMatcher.group(1);
            String adventureColor = "<color:" + hex + ">";
            legacyText = StringReplacer.replace(legacyText, Replacement.of(hexMatcher.group(), adventureColor));
        }

        List<? extends Replacement> replacementList = LEGACY_TO_MINI_MESSAGE.entrySet()
                .stream()
                .map(entry -> Replacement.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        legacyText = StringReplacer.replace(legacyText, replacementList);

        return legacyText;
    }

    public static @NotNull Component legacyToComponent(@NotNull String legacyText) {
        return LEGACY_SERIALIZER.deserialize(legacyText); //TODO: Find better way without using legacy serializer
    }

}
