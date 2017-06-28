package security;

public class GenerateReport {

	public static void main(String[] args) throws Exception {
		GenerateHTML.startHTML();
		@SuppressWarnings("unused")	
		TrackerGet tg = new TrackerGet(args[0]);
		GenerateHTML.endHTML();
	}

}