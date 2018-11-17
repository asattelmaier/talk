package com.app.talk.common;

import java.util.Scanner;

public class User {
    private String name;

    public String getName() {
        return this.name;
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
