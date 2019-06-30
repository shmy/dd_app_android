package tech.shmy.dd_app.entity;

import java.util.ArrayList;
import java.util.List;

public class MenuEntity {
    public int id;
    public String name;
    public List<MenuEntity> children;

    public MenuEntity(int id, String name, List<MenuEntity> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }

    public MenuEntity(int id, String name) {
        this.id = id;
        this.name = name;
        this.children = new ArrayList<>();
    }
}
