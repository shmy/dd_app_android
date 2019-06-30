package tech.shmy.dd_app.defs;

public class AfterResponse<T> {
    public T data;
    public Error error;

    public AfterResponse(T data) {
        this.data = data;
    }
    public AfterResponse(Error error) {
        this.error = error;
    }
}
