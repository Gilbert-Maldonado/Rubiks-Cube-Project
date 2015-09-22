import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;

/*
    The driver class that operates the project
 */
public class AlarmMain{
    
	public static final int START_MIN_HOUR = 0;
	public static final int HOUR_LIMIT = 24;
	public static final int MINUTE_LIMIT = 60;
	
    public static void main(String [] args) throws FileNotFoundException {


//        File cube = new File("solvedCube.jpg");
//        CheckFaces checkFaces = new CheckFaces(cube);
//        checkFaces.isSolved();

        Scanner kb = new Scanner(System.in);
        TextReader textReader = new TextReader();
        StringBuilder text = textReader.getText();
        TextToSpeech tts = new TextToSpeech(text.toString());
        //tts.convertText(text.toString());


        String hour = getChoice(kb, HOUR_LIMIT);
        String minute = getChoice(kb, MINUTE_LIMIT);

        Alarm alarm = new Alarm(tts, text);
        alarm.setAlarm(hour,minute);
        Calendar cal = Calendar.getInstance();
    }
    
    // get choice from the user
 	// keyboard != null and is connected to System.in
 	// return an int that is >= SEARCH and <= QUIT
 	private static String getChoice(Scanner kb, int limit) {
 		if(kb == null){
 			throw new IllegalArgumentException("Keyboard is null in getChoice method.");
 		}
 		int choice = 0;
 		//Depending if this method is used for getting hour or minute
 		if(limit == HOUR_LIMIT){
 			choice = getInt(kb, "Enter a valid hour for a 24 hour clock(0-23): ");
 		}
 		else{
 			choice = getInt(kb, "Enter a valid minute(0-59): ");
 		}
 		kb.nextLine();
 		while( choice < START_MIN_HOUR || choice >= limit){
	 			System.out.println("\n" + choice + " is not a valid choice");
	 			choice = getInt(kb, "Enter choice: ");
	 			kb.nextLine();
 		}
 		String output = "" + choice;
        if(choice >= 0 && choice <= 9) {
            output = "0" + output;
        }
 		return output;
 	}
 	
 	// ensure an int is entered from the keyboard
 	// pre: s != null and is connected to System.in
     private static int getInt(Scanner s, String prompt) {
         System.out.print(prompt);
         while (!s.hasNextInt()) {
             s.next();
             System.out.println("That was not an int.");
             System.out.print(prompt);
         }
         return s.nextInt();
     }
}