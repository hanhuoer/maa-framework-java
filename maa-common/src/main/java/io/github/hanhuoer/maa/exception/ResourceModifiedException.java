package io.github.hanhuoer.maa.exception;

public class ResourceModifiedException extends MaaFrameworkException {

    public ResourceModifiedException() {
        super();
    }

    public ResourceModifiedException(String message) {
        super(message);
    }

    public ResourceModifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceModifiedException(Throwable cause) {
        super(cause);
    }
}
