/*
 * SkinsRestorer
 * Copyright (C) 2024  SkinsRestorer Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.skinsrestorer.adapter;

import net.skinsrestorer.api.property.SkinProperty;
import net.skinsrestorer.api.property.SkinVariant;
import net.skinsrestorer.shared.storage.adapter.StorageAdapter;
import net.skinsrestorer.shared.storage.model.cache.MojangCacheData;
import net.skinsrestorer.shared.storage.model.player.PlayerData;
import net.skinsrestorer.shared.storage.model.skin.CustomSkinData;
import net.skinsrestorer.shared.storage.model.skin.PlayerSkinData;
import net.skinsrestorer.shared.storage.model.skin.URLSkinData;
import org.junit.Assert;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AdapterHelper {
    private static final String DEFAULT_NAME = "Pistonmaster";
    private static final UUID DEFAULT_UUID = UUID.nameUUIDFromBytes(DEFAULT_NAME.getBytes(StandardCharsets.UTF_8));

    public static void testAdapter(StorageAdapter adapter) {
        adapter.setCachedUUID("test", MojangCacheData.of(UUID.randomUUID(), -1));
        adapter.setPlayerData(UUID.randomUUID(), PlayerData.of(UUID.randomUUID(), null));
        adapter.setPlayerSkinData(DEFAULT_UUID, PlayerSkinData.of(DEFAULT_UUID, DEFAULT_NAME, SkinProperty.of("test", "test"), -1));
        adapter.setURLSkinData("test", URLSkinData.of("https://test.com", "test", SkinProperty.of("test", "test"), SkinVariant.CLASSIC));
        adapter.setCustomSkinData("test-skin", CustomSkinData.of("test-skin", SkinProperty.of("test", "test")));
        adapter.setCustomSkinData("test-skin2", CustomSkinData.of("test-skin2", SkinProperty.of("test", "test")));

        Assert.assertEquals(2, adapter.getCustomGUISkins(0).size());

        // Check if offset works as well, we actually have two skins in the storage for GUI
        Assert.assertEquals(1, adapter.getCustomGUISkins(1).size());
    }
}
