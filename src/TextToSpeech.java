import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.xml.internal.fastinfoset.stax.factory.StAXOutputFactory;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.*;

public class TextToSpeech {
	
	private String text;
    private final String NOT_SOLVED = "Sorry, the Rubik's cube was not solved.";
	private URL url;
	
	public TextToSpeech() {
		text = "";
	}

	public TextToSpeech(String text) {
		this.text = text;
	}

    public void notSolved() {
        convertText(NOT_SOLVED);
    }

    public void convertText(String text) {
        ArrayList<String> textChunks = textInfo(text);
        playAudio(textChunks);
    }
	
	public ArrayList<String> textInfo(String text) {
        ArrayList<String> textList = new ArrayList<String>();
         if(text.length() > 100) {
             textInChunks(text, textList);
         }
         else {
             textList.add(text);
         }
        return textList;
    }

    /*
        The TTS can only take in 100 characters at a time so the text has to be broken up
        into chunks of 100 and then adds the segments to an ArrayList of Strings.
     */
    public void textInChunks(String text, ArrayList<String> textChunks) {
        int pos = 0;

        while(pos < text.length()) {
            StringBuilder result = new StringBuilder();
            int count = 0;

            //Can only send chucks of 100 chars in TextToSpeech
            while(pos < text.length() && count < 90) {
                if(text.charAt(pos) == '"') {
                    result.append("'");
                }
                else {
                    result.append(text.charAt(pos));
                }
                pos++;
                count++;
            }
            while(pos < text.length() && text.charAt(pos) != ' ') {
                if(text.charAt(pos) == '"') {
                    result.append("'");
                }
                else {
                    result.append(text.charAt(pos));
                }
                pos++;
            }
            textChunks.add(result.toString());
            System.out.println(result.toString());
        }
    }

    public void playAudio(ArrayList<String> textList) {
        // did this to make sure the bytes writen would fit
        byte[] audioInfo = new byte[3000000];
        try {
            OutputStream out = new FileOutputStream("text.mp3");
            int index = 0;

	// TODO: fix the api key thing
            while (index < textList.size()) {
//                HttpResponse<InputStream> response = Unirest.get("https://montanaflynn-text-to-speech.p.mashape.com/speak?text={param}")
//                        .header("X-Mashape-Key", "").routeParam("param", textList.get(index)).asBinary();
                //r is the rate at which the text to speech talks, valid inputs (-10,10)
                HttpResponse<InputStream> response = Unirest.get("https://voicerss-text-to-speech.p.mashape.com/?c=mp3&f=8khz_8bit_mono&hl=en-us&r=0&src={param}")
                        .header("X-Mashape-Key", "").routeParam("param", textList.get(index)).asBinary();
                InputStream in = (InputStream) response.getBody();

                int len;
                while ((len = in.read(audioInfo)) > 0) {
                    out.write(audioInfo, 0, len);
                }
                in.close();
                index++;
            }
            out.close();
            Unirest.shutdown();

            String bits = "file:///C:/Users/super_000/Desktop/Rubik's%20Cube%20Proj/text.mp3";

            final CountDownLatch latch = new CountDownLatch(1);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new JFXPanel(); // initializes JavaFX environment
                    latch.countDown();
                }
            });
            latch.await();

            Media hit = new Media(bits);
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
        }
        catch(Exception e) {
            System.out.println("Play Audio method.");
            e.printStackTrace();
        }
    }
}
