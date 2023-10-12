package net.skinsrestorer.bukkit.hooks;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.skinsrestorer.api.PropertyUtils;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.property.SkinIdentifier;
import net.skinsrestorer.api.property.SkinProperty;
import net.skinsrestorer.api.storage.PlayerStorage;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
public class SRPlaceholderAPIExpansion extends PlaceholderExpansion {
    private final SkinsRestorer api;
    private final PluginDescriptionFile description;
    private static final String STEVE_URL = "https://textures.minecraft.net/texture/6d3b06c38504ffc0229b9492147c69fcf59fd2ed7885f78502152f77b4d50de1";
    private static final String ALEX_URL = "https://textures.minecraft.net/texture/fb9ab3483f8106ecc9e76bd47c71312b0f16a58784d606864f3b3e9cb1fd7b6c";

    @Override
    public @NotNull String getIdentifier() {
        return "skinsrestorer";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", description.getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return description.getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(@NotNull OfflinePlayer offlinePlayer, @NotNull String params) {
        params = params.toLowerCase(Locale.ROOT);
        PlayerStorage storage = api.getPlayerStorage();

        if (params.startsWith("skin_name")) {
            Optional<SkinIdentifier> skin = storage.getSkinIdOfPlayer(offlinePlayer.getUniqueId());

            if (skin.isPresent()) {
                return skin.get().getIdentifier();
            }

            if (params.startsWith("skin_name_")) {
                String subString = params.substring("skin_name_".length());

                if (subString.equalsIgnoreCase("or_player_name")) {
                    return offlinePlayer.getName();
                }
            }

            return "Error";
        }

        if (params.toLowerCase().startsWith("texture_url")) {
            try {
                Optional<SkinProperty> skin = storage.getSkinForPlayer(offlinePlayer.getUniqueId(), offlinePlayer.getName());

                if (skin.isPresent()) {
                    return extractUrl(skin.get());
                }

                if (params.startsWith("texture_url_")) {
                    String subString = params.substring("texture_url_".length());

                    if (subString.equalsIgnoreCase("or_empty")) {
                        return "";
                    } else if (subString.equalsIgnoreCase("or_steve")) {
                        return STEVE_URL;
                    } else if (subString.equalsIgnoreCase("or_alex")) {
                        return ALEX_URL;
                    }
                }
            } catch (DataRequestException e) {
                e.printStackTrace();
            }

            return "Error";
        }

        if (params.toLowerCase().startsWith("texture_id")) {
            try {
                Optional<SkinProperty> skin = storage.getSkinForPlayer(offlinePlayer.getUniqueId(), offlinePlayer.getName());

                if (skin.isPresent()) {
                    return extractUrlStripped(skin.get());
                }

                if (params.startsWith("texture_id_")) {
                    String subString = params.substring("texture_id_".length());

                    if (subString.equalsIgnoreCase("or_empty")) {
                        return "";
                    } else if (subString.equalsIgnoreCase("or_steve")) {
                        return STEVE_URL;
                    } else if (subString.equalsIgnoreCase("or_alex")) {
                        return ALEX_URL;
                    }
                }
            } catch (DataRequestException e) {
                e.printStackTrace();
            }

            return "Error";
        }

        return null;
    }

    private String extractUrl(SkinProperty property) {
        return PropertyUtils.getSkinTextureUrl(property);
    }

    private String extractUrlStripped(SkinProperty property) {
        return PropertyUtils.getSkinTextureUrlStripped(property);
    }
}
