package pl.peridot.yetanothermessageslibrary;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.peridot.yetanothermessageslibrary.locale.LocaleProvider;
import pl.peridot.yetanothermessageslibrary.replace.Replaceable;
import pl.peridot.yetanothermessageslibrary.replace.StringReplacer;

public interface MessageService<C extends MessageRepository> {

    default @Nullable <T> T supplyValue(@Nullable Object entity, @NotNull Function<@NotNull C, @Nullable T> valueSupplier) {
        Locale locale = this.getLocale(entity);
        return valueSupplier.apply(this.getMessageRepository(locale));
    }

    default @Nullable <T> T supplyValue(@NotNull Function<@NotNull C, @Nullable T> valueSupplier) {
        return this.supplyValue(null, valueSupplier);
    }

    @Contract(pure = true)
    default @Nullable String supplyString(@Nullable Object object, @NotNull Function<@NotNull C, @Nullable String> stringSupplier, @NotNull Replaceable... replacements) {
        String string = this.supplyValue(object, stringSupplier);
        if (string == null) {
            return null;
        }
        Locale locale = this.getLocale(object);
        return StringReplacer.replace(locale, string, replacements);
    }

    default @Nullable List<String> supplyStringList(@Nullable Object object, @NotNull Function<@NotNull C, @Nullable List<String>> stringSupplier, @NotNull Replaceable... replacements) {
        List<String> stringList = this.supplyValue(object, stringSupplier);
        if (stringList == null || stringList.isEmpty()) {
            return stringList;
        }
        Locale locale = this.getLocale(object);
        return StringReplacer.replace(locale, stringList, replacements);
    }

    @NotNull Locale getDefaultLocale();

    @NotNull LocaleProvider getLocaleProvider();

    default @NotNull Locale getLocale(@Nullable Object entity) {
        if (entity == null) {
            return this.getDefaultLocale();
        }

        Locale locale = this.getLocaleProvider().getLocale(entity);
        if (locale == null) {
            return this.getDefaultLocale();
        }
        return locale;
    }

    @NotNull C getMessageRepository(@NotNull Locale locale);

}
