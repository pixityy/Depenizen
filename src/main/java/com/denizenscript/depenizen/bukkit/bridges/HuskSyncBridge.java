package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.denizencore.tags.TagManager;
import com.denizenscript.denizencore.tags.TagRunnable;
import com.denizenscript.denizencore.tags.ReplaceableTagEvent;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.DenizenCore;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.commands.husksync.HuskSyncCommand;
import com.denizenscript.depenizen.bukkit.events.husksync.PlayerDataSyncScriptEvent;
import com.denizenscript.depenizen.bukkit.events.husksync.PlayerDataPreSyncScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.husksync.HuskSyncPlayerProperties;
import net.william278.husksync.api.HuskSyncAPI;

public class HuskSyncBridge extends Bridge {

    // <--[language]
    // @name HuskSync Bridge
    // @group Depenizen Bridges
    // @plugin Depenizen, HuskSync
    // @description
    // HuskSync is a modern, cross-server player data synchronization system.
    // 
    // This bridge provides access to HuskSync's data synchronization features,
    // allowing scripts to interact with player data across multiple servers.
    // 
    // You can listen for data synchronization events and access player sync data
    // through properties and tags.
    //
    // -->

    public static HuskSyncAPI huskSyncAPI;

    @Override
    public void init() {
        try {
            huskSyncAPI = HuskSyncAPI.getInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize HuskSync API", e);
        }
        
        // Register properties for PlayerTag to access HuskSync data
        PropertyParser.registerProperty(HuskSyncPlayerProperties.class, PlayerTag.class);
        
        // Register script events for HuskSync data synchronization
        ScriptEvent.registerScriptEvent(PlayerDataSyncScriptEvent.class);
        ScriptEvent.registerScriptEvent(PlayerDataPreSyncScriptEvent.class);
        
        // Register HuskSync commands
        DenizenCore.commandRegistry.registerCommand(HuskSyncCommand.class);

        // Register HuskSync global tags
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                huskSyncTagEvent(event);
            }
        }, "husksync");
    }

    public void huskSyncTagEvent(ReplaceableTagEvent event) {
        Attribute attribute = event.getAttributes().fulfill(1);

        // <--[tag]
        // @attribute <husksync.is_enabled>
        // @returns ElementTag(Boolean)
        // @plugin Depenizen, HuskSync
        // @description
        // Returns whether HuskSync is enabled and properly initialized.
        // -->
        if (attribute.startsWith("is_enabled")) {
            event.setReplacedObject(new ElementTag(huskSyncAPI != null).getObjectAttribute(attribute.fulfill(1)));
        }

        // <--[tag]
        // @attribute <husksync.version>
        // @returns ElementTag
        // @plugin Depenizen, HuskSync
        // @description
        // Returns the version of HuskSync.
        // -->
        else if (attribute.startsWith("version")) {
            if (huskSyncAPI != null) {
                String version = plugin.getDescription().getVersion();
                event.setReplacedObject(new ElementTag(version).getObjectAttribute(attribute.fulfill(1)));
            }
        }
    }
}