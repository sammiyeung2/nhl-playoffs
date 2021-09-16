
public class TestFileInput {

	public TestFileInput() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main (String[] args) {
		int j = 1;

		MyFileReader scoresFile = new MyFileReader("scores" + j + ".txt");
		
		while (scoresFile.endOfFile() == false) {
			
			/**
			 * You may want to use the String's split() method to convert the String of one line
				into an array of 4 Strings based on the comma delimiter
			 */

			String fileString = scoresFile.readString();
			String[] fileValues = fileString.split(",");
			System.out.println(fileValues[3]);
			
			
		}
	}

}
