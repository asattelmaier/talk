package main.java.com.app.talk.common;

import java.io.Serializable;
import java.util.Scanner;

public class User implements Serializable {
	private static final long serialVersionUID = -8205683033807579298L;
	private String name;

	/**
	 * Get the name of the user.
	 *
	 * @return The user name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the name of the user.
	 *
	 * @param name
	 *            The user name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets User keyboard input and sets it as the user name.
	 */
	public void setNameFromUserInput() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter your username: ");

		this.name = scanner.nextLine();
	}
}
