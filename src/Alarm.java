import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class Alarm {

    // time of the alarm
    private String alarmHour;
    private String alarmMinute;

    // whether alarm is on or off
    private boolean alarmOn;

    private static Calendar calendar;
    private static Timer timer;
    private SimpleDateFormat currentTime;
    
    private static final int delay = 1000;
    private static final int period = 5000;
    
    private CheckFaces checkFaces;
    private boolean isSolved;
    private TextToSpeech tts;
    private StringBuilder text;

    // this is the constructor - this is called when "new"
    // is called, when the object is first created
    // this contains code to initialize all variables in object
    // this is a special method - it has no return value (and no void)
    public Alarm(TextToSpeech tts, StringBuilder text)
    {
        calendar = calendar.getInstance();
        timer = new Timer();
        currentTime = new SimpleDateFormat("HH:mm");

        alarmHour = "";
        alarmMinute = "";
        alarmOn = false;
        isSolved = false;
        this.tts = tts;
        this.text = text;
    }
    

    // now we put all of the methods
    // each method that we want code from outside the class to call is public
    // each method that we only want code from inside the class to call is private
    /* setAlarm
     * inputs: alarm number, hour, minute
     * outputs: none
     * purpose: sets one of the alarms to the hour and minute specified
     * It also turns on the alarm
     */
    public void setAlarm(String hour, String minute)
    {   
        // set that alarm
        alarmHour = hour;
        alarmMinute = minute;


        turnOnAlarm();

    }
    
    /* turnOffAlarm
     * outputs: none
     * purpose: This turns off an alarm. 
     */
    public void turnOffAlarm() {
        alarmOn = false;
    }

    //Turns on alarm
    public void turnOnAlarm() {
        alarmOn = true;
        tick();
    }

    /* setTime
     * inputs: hour, minute, second
     * outputs: none
     * purpose: This updates the current time to the values
     * passed in.
     */
    public void setTime(int hour, int minute, int second)
    {
//        currentHour = hour;
//        currentMinute = minute;
//        currentSecond = second;
    }

    /*
     * purpose: This gets called each second.  It updates the current time
     * of the clock and sets of the alarm if necessary
     */
    public void tick()
    {
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                calendar = Calendar.getInstance();
                String currHour = currentTime.format(calendar.getTime()).substring(0,2);
                String currMinute = currentTime.format(calendar.getTime()).substring(3,5);
                
                String thisMinute = currentTime.format(calendar.getTime()).substring(3,5);
                System.out.println("The minute is " +thisMinute+".");
                
                if(startAlarm(currHour, currMinute, alarmHour, alarmMinute)){
                    timer.cancel();
                    timer.purge();
                    soundAlarm();
                }
            }
       },delay,period);
    }

    //Helper method that checks to see if the alarm should be started
    public static boolean startAlarm(String currHour, String currMinute, String alarmHour, String alarmMinute) {
        return currHour.equals(alarmHour) && currMinute.equals(alarmMinute);
    }
    
    //When the alarm should be sounded; have to synchronize the calls to the media player
    // and the text to speech
    private void soundAlarm()
    {
        tts.convertText(text.toString());

        try {
            String bits = "file:///C:/Users/super_000/Desktop/Rubik's%20Cube%20Proj/I%20Am%20An%20Astronaut.mp3";
            final CountDownLatch latch = new CountDownLatch(1);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new JFXPanel(); // initializes JavaFX environment
                    latch.countDown();
                }
            });
            latch.await();

            Media hit = new Media(bits);
            final MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();

            // Look into this
            while(!isSolved) {
                soundAlarmHelper(mediaPlayer);
            }

        }
        catch(Exception e) {
            System.out.println("soundAlarm method");
            e.printStackTrace();
        }
    }

    // Helper method for when the alarm sounds
    private void soundAlarmHelper(MediaPlayer mediaPlayer) {

        try {
            // Look into this whole segment.
            Process p = Runtime.getRuntime().exec("cmd /C button.py");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;

            while ((line = in.readLine()) != null) {
                if(line.equals("captured")) {
                    System.out.println("YES");

                    File cube = new File("solvedCube.jpg");
                    checkFaces = new CheckFaces(cube);
                    if(checkFaces.isSolved()) {
                        mediaPlayer.stop();
                        isSolved = true;
                    }
                    turnOffAlarm();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
