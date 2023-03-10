package dev.peri.yetanothermessageslibrary.util;

import java.lang.reflect.Method;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitLocaleHelper {

    private static Method GET_LOCALE_SPIGOT;

    static {
        try {
            GET_LOCALE_SPIGOT = Player.Spigot.class.getMethod("getLocale");
        } catch (NoSuchMethodException e) {
            GET_LOCALE_SPIGOT = null;
        }
    }

    public @Nullable static String getLocaleString(@NotNull Player player) {
        if (GET_LOCALE_SPIGOT != null) {
            try {
                return (String) GET_LOCALE_SPIGOT.invoke(player.spigot());
            }
            catch (Exception ex) {
                return null;
            }
        }

        return player.getLocale();
    }

}