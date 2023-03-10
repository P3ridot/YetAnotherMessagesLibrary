package dev.peri.yetanothermessageslibrary.replace.replacement;

import dev.peri.yetanothermessageslibrary.adventure.AdventureHelper;
import dev.peri.yetanothermessageslibrary.replace.Replaceable;
import dev.peri.yetanothermessageslibrary.replace.StringReplacer;
import java.util.Locale;
import java.util.function.Supplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Replacement implements Replaceable {

    private final String from;

    protected Replacement(@NotNull String from) {
        this.from = from;
    }

    public @NotNull String getFrom() {
        return this.from;
    }

    public abstract @NotNull String getTo();

    @Contract(pure = true)
    public @NotNull String replace(@Nullable Locale locale, @NotNull String text) {
        return text.replace(this.getFrom(), this.getTo());
    }

    @Contract(pure = true)
    @Override
    public @NotNull Component replace(@Nullable Locale locale, @NotNull Component text) {
        return text.replaceText(TextReplacementConfig.builder()
                .matchLiteral(this.getFrom())
                .replacement(AdventureHelper.legacyToComponent(this.getTo()))
                .build());
    }

    public static @NotNull SimpleReplacement of(@NotNull String from, @Nullable Object to) {
        return new SimpleReplacement(from, to);
    }

    public static @NotNull SimpleReplacement of(@NotNull String from, @Nullable String to, @NotNull Replacement... replacements) {
        return of(from, StringReplacer.replace(to, replacements));
    }

    public static @NotNull SupplierReplacement of(@NotNull String from, @NotNull Supplier<@Nullable Object> to) {
        return new SupplierReplacement(from, to);
    }

    public static @NotNull SupplierReplacement of(@NotNull String from, @NotNull Supplier<@Nullable Object> to, @NotNull Replacement... replacements) {
        return new SupplierReplacement(from, to, replacements);
    }

}
