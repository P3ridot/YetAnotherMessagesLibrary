package dev.peri.yetanothermessageslibrary.replace;

import dev.peri.yetanothermessageslibrary.adventure.AdventureHelper;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OkaeriPlaceholdersReplaceable implements Replaceable {

    private static final Pattern FIELD_PATTERN = Pattern.compile("\\{(?<content>[^}]+)\\}");

    private final Placeholders placeholders;
    private final Function<PlaceholderContext, PlaceholderContext> applyContexts;

    public OkaeriPlaceholdersReplaceable(@NotNull Placeholders placeholders, @NotNull Function<PlaceholderContext, PlaceholderContext> applyContexts) {
        this.placeholders = placeholders;
        this.applyContexts = applyContexts;
    }

    @Override
    public @NotNull String replace(@Nullable Locale locale, @NotNull String text) {
        return this.replacePlaceholders(locale, text);
    }

    @Override
    public @NotNull Component replace(@Nullable Locale locale, @NotNull Component text) {
        TextReplacementConfig replacement = TextReplacementConfig.builder()
                .match(FIELD_PATTERN)
                .replacement((result, input) -> AdventureHelper.legacyToComponent(this.replacePlaceholders(locale, "{" + result.group() + "}")))
                .build();
        return text.replaceText(replacement);
    }

    private String replacePlaceholders(@Nullable Locale locale, @NotNull String text) {
        CompiledMessage compiled = locale != null
                ? CompiledMessage.of(locale, text)
                : CompiledMessage.of(text);
        return this.applyContexts.apply(this.placeholders.contextOf(compiled)).apply();
    }

}
