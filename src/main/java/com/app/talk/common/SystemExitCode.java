package com.app.talk.common;

/**
 * Enumerates possible system exit codes.
 */
public enum SystemExitCode {
    ABORT(-1),
    NORMAL(0);

    private final int code;

    /**
     * Constructor for the exit code enum.
     *
     * @param code the exit code
     */
    SystemExitCode(int code) {
        this.code = code;
    }

    /**
     * Returns the exit code.
     *
     * @return the exit code
     */
    public int getCode() {
        return code;
    }
}
