package com.markiesch.modules.template;

import com.markiesch.modules.infraction.InfractionType;

public class TemplateModel {
    private final int id;
    private String name;
    private String reason;
    private InfractionType type;
    private Long duration;

    public TemplateModel(int id, String name, String reason, InfractionType type, Long duration) {
        this.id = id;
        this.name = name;
        this.reason = reason;
        this.type = type;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InfractionType getType() {
        return type;
    }

    public void setType(InfractionType type) {
        this.type = type;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
