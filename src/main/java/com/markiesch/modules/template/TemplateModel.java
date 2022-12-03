package com.markiesch.modules.template;

import com.markiesch.modules.infraction.InfractionType;

public class TemplateModel {
    public final int id;
    public String name;
    public String reason;
    public InfractionType type;
    public Long duration;

    public TemplateModel(int id, String name, String reason, InfractionType type, Long duration) {
        this.id = id;
        this.name = name;
        this.reason = reason;
        this.type = type;
        this.duration = duration;
    }
}
