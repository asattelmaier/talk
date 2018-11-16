package com.app.talk;

import static com.app.talk.TalkException.ErrorCode.*;

/**
 * Exception handling for Talk.
 */
class TalkException extends Exception {
    private ErrorCode errorCode = OK;
    private String errorParameter = null;

    /**
     * Simple Exception constructor.
     *
     * @param errorCode of the thrown error
     */
    TalkException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Exception constructor with additional parameter.
     *
     * @param errorCode      of the thrown error
     * @param errorParameter of the thrown error
     */
    TalkException(ErrorCode errorCode, String errorParameter) {
        this.errorCode = errorCode;
        this.errorParameter = errorParameter;
    }

    /**
     * Handles the given Error Code and returns the mapped Error message.
     *
     * @return the error message
     */
    String errorMessage() {
        switch (errorCode) {
            case OK:
                return "Everything looks fine, you should not get here.";
            case INVALID_PORT:
                return String.format("Given Port '%s' invalid.", errorParameter);
        }
        return "";
    }


    /**
     * The Error Codes
     */
    public enum ErrorCode {
        OK, INVALID_PORT
    }
}
