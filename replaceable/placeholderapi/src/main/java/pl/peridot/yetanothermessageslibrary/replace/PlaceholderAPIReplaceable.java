package pl.peridot.yetanothermessageslibrary.replace;

import java.lang.ref.WeakReference;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIReplaceable implements Replaceable {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("[%]([^%]+)[%]");

    private final WeakReference<Player> playerRef;

    public PlaceholderAPIReplaceable(@Nullable Player player) {
        this.playerRef = new WeakReference<>(player);
    }

    @Override
    public @NotNull String replace(@NotNull String text, boolean ignoreCase) {
        return this.replacePlaceholder(text);
    }

    @Override
    public @NotNull Component replace(@NotNull Component text) {
        TextReplacementConfig replacement = TextReplacementConfig.builder()
                .match(PLACEHOLDER_PATTERN)
                .replacement((result, input) -> Component.text(this.replacePlaceholder(result.group())))
                .build();
        return text.replaceText(replacement);
    }

    private String replacePlaceholder(String text) {
        Player player = this.playerRef.get();
        if (player == null) {
            return text;
        }
        return PlaceholderAPI.setPlaceholders(player, text);
    }

}