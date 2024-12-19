package io.github.hanhuoer.maa.exception;

public class ControllerNotBoundException extends MaaFrameworkException {

    public ControllerNotBoundException() {
        super();
    }

    public ControllerNotBoundException(String message) {
        super(message);
    }

    public ControllerNotBoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerNotBoundException(Throwable cause) {
        super(cause);
    }
}
