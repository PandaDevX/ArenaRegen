package me.realized.de.arenaregen;

import lombok.Getter;
import me.realized.de.arenaregen.command.ArenaregenCommand;
import me.realized.de.arenaregen.config.Config;
import me.realized.de.arenaregen.config.Lang;
import me.realized.de.arenaregen.nms.LatestHandler;
import me.realized.de.arenaregen.nms.MoreLatestHandler;
import me.realized.de.arenaregen.nms.NMS;
import me.realized.de.arenaregen.selection.SelectionManager;
import me.realized.de.arenaregen.zone.ResetZoneManager;
import me.realized.duels.api.extension.DuelsExtension;

public class ArenaRegen extends DuelsExtension {

    @Getter
    private Config configuration;
    @Getter
    private Lang lang;
    @Getter
    private NMS handler;
    @Getter
    private SelectionManager selectionManager;
    @Getter
    private ResetZoneManager zoneManager;

    @Override
    public void onEnable() {
        this.configuration = new Config(this);
        this.lang = new Lang(this);

        final String packageName = api.getServer().getClass().getPackage().getName();
        final String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        this.handler = new MoreLatestHandler();

        this.selectionManager = new SelectionManager(this, api);
        this.zoneManager = new ResetZoneManager(this, api);
        api.registerSubCommand("duels", new ArenaregenCommand(this, api));
    }

    @Override
    public void onDisable() {
        zoneManager.save();
    }

    @Override
    public String getRequiredVersion() {
        return "3.4.1";
    }

    public void info(final String s) {
        api.info("[" + getName() + " Extension] " + s);
    }

    public void error(final String s) {
        api.error("[" + getName() + " Extension] " + s);
    }

    public void error(final String s, final Throwable thrown) {
        api.error("[" + getName() + " Extension] " + s, thrown);
    }
}