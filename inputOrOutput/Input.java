package inputOrOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * <p>
 * This class initialize a input class and get input string form keyboard using
 * Scanner.
 *
 */
public class Input {

	// create new scanner to scan the information from keyboard
	public static Scanner keyboard = new Scanner(System.in);

	// create an array list to store all the input
	public static ArrayList<String> storage = new ArrayList<String>();

	/**
	 * get the keyboard input line by line using scanner.
	 * 
	 * @return command the line just scanned
	 */
	public static String getInput() {
		// read whole line user input
		String command = keyboard.nextLine();
		// add the input into storage
		storage.add(command);
		return command;
	}

	/**
	 * add a specific input line.
	 * 
	 * @param input is a string with the input
	 */
	public static void addInput(String input) {
		// add the input into storage
		storage.add(input);
	}

	/**
	 * get the array list that contains all the input.
	 * 
	 * @return array list that contains all the input
	 */
	public static ArrayList getStorage() {
		return storage;
	}

}
