package tech.shmy.dd_app.entity;

import java.util.Date;
import java.util.List;

public class VideoEntity {
    public long id;
    public String pic;
    public String name;
    public String actor;
    public String area;
    public String des;
    public String director;
    public String lang;
    public String year;
    public List<LabelEntity> lable;
    public List<ResourceEntity> resources;
    public long pid;
    public long views;
    public Date created_at;
    public Date updated_at;
    public Date deleted_at;

}
