package com.app.talk.common;

import java.util.Scanner;

public class User {
    private String name;

    /**
     * Get the name of the user.
     *
     * @return the user name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of the user.
     *
     * @param name the user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets User keyboard input and sets it as the username.
     */
    public void setNameFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");

        this.name = scanner.nextLine();
    }
}
