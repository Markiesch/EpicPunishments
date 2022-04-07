package com.markiesch.utils.configs;

import com.markiesch.EpicPunishments;

public class LangConfig extends Config {
    public LangConfig(EpicPunishments instance) {
        super(instance);
    }

    protected String getResource() { return "lang.yml"; }
}
