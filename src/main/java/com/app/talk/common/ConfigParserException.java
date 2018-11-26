package com.app.talk.common;

import static com.app.talk.common.ConfigParserException.ErrorCode.*;

/**
 * Exception handling for Talk.
 */
public class ConfigParserException extends Exception {
    private static final long serialVersionUID = 646024665573766896L;
    private ErrorCode errorCode = OK;
    private String errorParameter = null;

    /**
     * Simple Exception constructor.
     *
     * @param errorCode of the thrown error
     */
    ConfigParserException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Exception constructor with additional parameter.
     *
     * @param errorCode      of the thrown error
     * @param errorParameter of the thrown error
     */
    ConfigParserException(ErrorCode errorCode, String errorParameter) {
        this.errorCode = errorCode;
        this.errorParameter = errorParameter;
    }

    /**
     * Handles the given Error Code and returns the mapped Error message.
     *
     * @return the error message
     */
    public String errorMessage() {
        switch (errorCode) {
            case OK:
                return "Everything looks fine, you should not get here.";
            case INVALID_PORT:
                return String.format("Given Port '%s' invalid.", errorParameter);
            case TOO_MANY_ARGUMENTS:
                return "Too many arguments given.";
        }
        return "";
    }

    /**
     * The Error Codes
     */
    public enum ErrorCode {
        OK, INVALID_PORT, TOO_MANY_ARGUMENTS
    }
}
