package tech.shmy.dd_app.entity;

public class ErrorResponseEntity {
    public int code;
    public String info;
    public String path;

    public ErrorResponseEntity(int code, String info, String path) {
        this.code = code;
        this.info = info;
        this.path = path;
    }
}
