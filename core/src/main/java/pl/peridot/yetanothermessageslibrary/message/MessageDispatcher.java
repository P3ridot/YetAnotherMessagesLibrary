package pl.peridot.yetanothermessageslibrary.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.peridot.yetanothermessageslibrary.adventure.AudienceSupplier;
import pl.peridot.yetanothermessageslibrary.locale.LocaleProvider;
import pl.peridot.yetanothermessageslibrary.replace.Replaceable;
import pl.peridot.yetanothermessageslibrary.replace.replacement.Replacement;

public class MessageDispatcher<R> {

    private final AudienceSupplier<R> audienceSupplier;
    private final LocaleProvider localeProvider;

    private final Sendable sendable;

    private final List<Replaceable> replaceables = new ArrayList<>();
    private final List<Function<R, Replaceable>> replaceablesSuppliers = new ArrayList<>();

    public MessageDispatcher(@NotNull AudienceSupplier<R> audienceSupplier, LocaleProvider localeProvider, @Nullable Sendable sendable) {
        this.audienceSupplier = audienceSupplier;
        this.localeProvider = localeProvider;
        this.sendable = sendable;
    }

    public MessageDispatcher<R> with(@NotNull Replaceable replaceable) {
        this.replaceables.add(replaceable);
        return this;
    }

    public MessageDispatcher<R> with(@NotNull Replaceable... replaceables) {
        Collections.addAll(this.replaceables, replaceables);
        return this;
    }

    public MessageDispatcher<R> with(@NotNull Function<@NotNull R, Replaceable> replaceableSupplier) {
        this.replaceablesSuppliers.add(replaceableSupplier);
        return this;
    }

    public MessageDispatcher<R> with(@NotNull Function<@NotNull R, Replaceable>... replaceableSuppliers) {
        Collections.addAll(this.replaceablesSuppliers, replaceableSuppliers);
        return this;
    }

    public MessageDispatcher<R> with(@NotNull String from, @NotNull Object to) {
        return this.with(Replacement.of(from, to));
    }

    public MessageDispatcher<R> with(@NotNull String from, @NotNull Supplier<@NotNull Object> to) {
        return this.with(Replacement.of(from, to));
    }

    public void sendTo(@Nullable R receiver) {
        if (receiver == null) {
            return;
        }

        if (this.sendable == null) {
            return;
        }

        Locale locale = this.localeProvider.getLocale(receiver);
        Audience audience = this.audienceSupplier.getAudience(receiver);
        boolean console = this.audienceSupplier.isConsole(receiver);

        List<Replaceable> replacables = new ArrayList<>(this.replaceables);
        this.replaceablesSuppliers.stream().map(supplier -> supplier.apply(receiver)).forEach(replacables::add);

        this.sendable.send(locale, audience, console, replacables.toArray(new Replaceable[0]));
    }


}