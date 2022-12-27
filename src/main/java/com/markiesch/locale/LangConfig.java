package com.markiesch.locale;

import com.markiesch.EpicPunishments;
import com.markiesch.utils.Config;

public class LangConfig extends Config {
    public LangConfig(EpicPunishments plugin) {
        super(plugin);
    }

    @Override
    protected String getResource() {
        return "locale/en_US.yml";
    }
}
