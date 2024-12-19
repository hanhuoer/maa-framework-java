package io.github.hanhuoer.maa.exception;

public class ControllerModifiedException extends MaaFrameworkException {

    public ControllerModifiedException() {
        super();
    }

    public ControllerModifiedException(String message) {
        super(message);
    }

    public ControllerModifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerModifiedException(Throwable cause) {
        super(cause);
    }
}
