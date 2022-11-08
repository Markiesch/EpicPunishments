package com.markiesch.modules.template;

public class TemplateModel {
    public final int id;
    public String name;
    public String reason;
    public String type;
    public Long duration;

    public TemplateModel(int id, String name, String reason, String type, Long duration) {
        this.id = id;
        this.name = name;
        this.reason = reason;
        this.type = type;
        this.duration = duration;
    }
}
