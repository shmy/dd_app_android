package tech.shmy.dd_app.entity;

import java.util.List;

public class LinkEntityWithSource {
    public List<LinkEntity> links;
    public String source;

    public LinkEntityWithSource(List<LinkEntity> links, String source) {
        this.links = links;
        this.source = source;
    }
}
