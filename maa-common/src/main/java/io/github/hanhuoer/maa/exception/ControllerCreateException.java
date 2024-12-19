package io.github.hanhuoer.maa.exception;

public class ControllerCreateException extends MaaFrameworkException {

    public ControllerCreateException() {
        super();
    }

    public ControllerCreateException(String message) {
        super(message);
    }

    public ControllerCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerCreateException(Throwable cause) {
        super(cause);
    }
}
