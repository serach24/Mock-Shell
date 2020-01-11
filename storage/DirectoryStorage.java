package storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import inputOrOutput.StandardError;

public class DirectoryStorage<E> implements Iterable {

	public static ArrayList<Directory> storage = new ArrayList<Directory>();

	/**
	 * get the size of directory stack.
	 * 
	 * @return size the size of directory stack
	 */
	public int size() {
		return storage.size();
	}

	/**
	 * check if the stack is empty
	 * 
	 * @return true iff stack is nonempty
	 */
	public boolean isEmpty() {
		return storage.size() == 0;
	}

	/**
	 * takes a directory object and add it to top of directory stack.
	 * 
	 * @param dir dir to add.
	 */
	public static void addDir(Directory dir) {
		storage.add(dir);
	}

	/**
	 * remove and return the top Directory object from storage(i.e. directory
	 * stack). Display error message to standardOut if the stack is empty.
	 * 
	 * @return item the top directory on stack, if exists. or null if not exists
	 */
	public static Directory getDir() {
		Directory item;
		// check if the stack is empty
		if (storage.size() != 0) {
			// return and remove the top item
			item = storage.remove(storage.size() - 1);

		} else {
			// pass error to standardOut
			StandardError.displayError(
					"the directory stack is currently empty, cannot pop a directory path from it");
			item = null;
		}
		// return the poped item. or null if empty.
		return item;
	}

	public ArrayList<Directory> getStorage() {
		return storage;
	}

	/**
	 * list the path of all dirs in the directory stack, seperated by space list
	 * from bottom to top
	 * 
	 * @return one string containing all the directory names
	 */
	public String listing() {
		String result = "";
		for (int i = 0; i < this.size(); i++) {
			result = result + storage.get(i).getName() + " ";
		}
		return result;
	}

	@Override
	/**
	 * create and return the iterator of DirectoryStorage
	 */
	public Iterator iterator() {
		return new DirStorageBottomToTopIterator(storage);
	}

	/**
	 * This class represents an iterator of directory Stack. This iterator will
	 * iterate through the directory stack from bottom to top.
	 * 
	 * @author Candice
	 *
	 */
	private static class DirStorageBottomToTopIterator implements Iterator {

		private List<Directory> items;
		private int counter;

		/**
		 * constructor. construct an DirStorageBottomToTopIterator, whose items
		 * to iterate through is i, and counter is reset to 0 once created.
		 * 
		 * @param i the directory stack that will iterate through by this
		 *          iterator
		 */
		public DirStorageBottomToTopIterator(List<Directory> i) {
			this.items = i;
			this.counter = 0;
		}

		@Override
		/**
		 * check if the stack has a next element.
		 * 
		 * @return true iff the stack is not exhausted
		 */
		public boolean hasNext() {
			return (counter < items.size() - 1);
		}

		@Override
		/**
		 * get the next element of the stack.
		 */
		public Directory next() {
			if (hasNext()) {
				// from bottom to top, bottom of stack is front item of
				// arraylist
				// increate counter while iterating
				return (Directory) items.get(counter++);

			}
			return null;
		}
	}
}
