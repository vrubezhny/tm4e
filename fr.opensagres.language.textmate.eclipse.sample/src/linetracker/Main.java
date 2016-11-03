package linetracker;

import org.eclipse.jface.text.BadLocationException;

public class Main {

	public static void main(String[] args) throws BadLocationException {
		ListLineTracker tracker = new ListLineTracker();
		tracker.set("ab\ncd");
		display(tracker);
		tracker.replace(0, 4, "");
		display(tracker);
		
	}

	private static void display(ListLineTracker tracker) {
		for (Line line : tracker.getLines()) {
			System.out.println(line);
		}
	}
}
