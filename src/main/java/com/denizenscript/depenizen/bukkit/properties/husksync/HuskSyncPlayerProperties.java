package com.denizenscript.depenizen.bukkit.properties.husksync;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.TimeTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bridges.HuskSyncBridge;
import net.william278.husksync.api.HuskSyncAPI;
import net.william278.husksync.user.OnlineUser;
import net.william278.husksync.user.User;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class HuskSyncPlayerProperties implements Property {

    @Override
    public String getPropertyString() {
        return null;
    }

    @Override
    public String getPropertyId() {
        return "HuskSyncPlayer";
    }

    @Override
    public void adjust(Mechanism mechanism) {
        // None for now
    }

    public static boolean describes(ObjectTag object) {
        return object instanceof PlayerTag;
    }

    public static HuskSyncPlayerProperties getFrom(ObjectTag object) {
        if (!describes(object)) {
            return null;
        }
        else {
            return new HuskSyncPlayerProperties((PlayerTag) object);
        }
    }

    public static final String[] handledTags = new String[] {
            "husksync_is_syncing", "husksync_user_exists"
    };

    public static final String[] handledMechs = new String[] {
    }; // None for now

    public HuskSyncPlayerProperties(PlayerTag player) {
        this.player = player;
    }

    PlayerTag player;

    @Override
    public ObjectTag getObjectAttribute(Attribute attribute) {
        if (attribute == null) {
            return null;
        }

        if (HuskSyncBridge.huskSyncAPI == null) {
            Debug.echoError("HuskSync API not available");
            return null;
        }

        // <--[tag]
        // @attribute <PlayerTag.husksync_is_syncing>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, HuskSync
        // @description
        // Returns whether the player is currently syncing data.
        // -->
        if (attribute.startsWith("husksync_is_syncing")) {
            try {
                Optional<OnlineUser> onlineUser = HuskSyncBridge.huskSyncAPI.getUser(player.getPlayerEntity());
                if (onlineUser.isPresent()) {
                    return new ElementTag(HuskSyncBridge.huskSyncAPI.isLocked(onlineUser.get()))
                            .getObjectAttribute(attribute.fulfill(1));
                }
                return new ElementTag(false).getObjectAttribute(attribute.fulfill(1));
            } catch (Exception e) {
                Debug.echoError("Error checking HuskSync sync status: " + e.getMessage());
                return new ElementTag(false).getObjectAttribute(attribute.fulfill(1));
            }
        }

        // <--[tag]
        // @attribute <PlayerTag.husksync_user_exists>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, HuskSync
        // @description
        // Returns whether the player has a HuskSync user profile.
        // -->
        if (attribute.startsWith("husksync_user_exists")) {
            UUID uuid = player.getUUID();
            try {
                CompletableFuture<Optional<User>> userFuture = HuskSyncBridge.huskSyncAPI.getUser(uuid);
                Optional<User> user = userFuture.get(5, TimeUnit.SECONDS); // Add timeout
                return new ElementTag(user.isPresent()).getObjectAttribute(attribute.fulfill(1));
            } catch (Exception e) {
                Debug.echoError("Error checking HuskSync user existence: " + e.getMessage());
                return new ElementTag(false).getObjectAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}