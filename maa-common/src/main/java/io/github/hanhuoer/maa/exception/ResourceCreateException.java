package io.github.hanhuoer.maa.exception;

public class ResourceCreateException extends MaaFrameworkException {

    public ResourceCreateException() {
        super();
    }

    public ResourceCreateException(String message) {
        super(message);
    }

    public ResourceCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceCreateException(Throwable cause) {
        super(cause);
    }
}
