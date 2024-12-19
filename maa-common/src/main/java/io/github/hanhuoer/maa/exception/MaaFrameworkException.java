package io.github.hanhuoer.maa.exception;

/**
 * TODO
 */
public class MaaFrameworkException extends RuntimeException {

    public MaaFrameworkException() {
        super();
    }

    public MaaFrameworkException(String message) {
        super(message);
    }

    public MaaFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaaFrameworkException(Throwable cause) {
        super(cause);
    }

    public static MaaFrameworkException build() {
        return new MaaFrameworkException();
    }

    public static MaaFrameworkException build(String message) {
        return new MaaFrameworkException(message);
    }

    public static MaaFrameworkException build(String message, Throwable cause) {
        return new MaaFrameworkException(message, cause);
    }

    public static MaaFrameworkException build(Throwable cause) {
        return new MaaFrameworkException(cause);
    }

}
