package io.github.hanhuoer.maa.exception;

public class ResourceNotBoundException extends MaaFrameworkException {

    public ResourceNotBoundException() {
        super();
    }

    public ResourceNotBoundException(String message) {
        super(message);
    }

    public ResourceNotBoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotBoundException(Throwable cause) {
        super(cause);
    }
}
