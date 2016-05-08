/**
 * Created by Gilbert on 6/16/2015.
 */
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import javax.imageio.ImageIO;

/*
    This code is pretty gacky. It's checking the face as if the cube were
    always placed the same way; which is terrible.
*/
public class CheckFaces {

    private BufferedImage image;
    private static final int ROW_COL_SIZE = 3;
    private final int HEIGHT;
    private final int WIDTH;
    private static final double Y_BLUE_START_PERCENTAGE = 0.15;
    private static final double X_BLUE_START_PERCENTAGE = 0.155;
    private static final double Y_RED_START_PERCENTAGE = 0.5;
    private static final double X_RED_START_PERCENTAGE = 0.611;
    private static final double Y_WHITE_START_PERCENTAGE = 0.5;
    private static final double X_WHITE_START_PERCENTAGE = 0.397;

    //Constructor for CheckFaces class
    public CheckFaces(File file) {

        try{
            this.image = ImageIO.read(file);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        HEIGHT = image.getHeight();
        WIDTH = image.getWidth();
        System.out.println("Height is " + HEIGHT);
        System.out.println("Width is " + WIDTH);
    }

    //Determines if the the Rubik's cube is solved or not
    public boolean isSolved() {
        return checkBlueFace() && checkRedFace() && checkWhiteFace();
    }

    //Checks to see if the blue face is solved. The blue face should be oriented to be on the top.
    private boolean checkBlueFace() {
        boolean isSolved = true;
        // Starting positions
        int yStart = (int) (Y_BLUE_START_PERCENTAGE * HEIGHT);
        int xStart = (int) (X_BLUE_START_PERCENTAGE * WIDTH);

        // x and y is the position of the rows of the starting blue face
        int y = yStart;
        int x = xStart;
        // yChange is taking into account the angle the picture is taken
        int yChange = 0;
        while(y < HEIGHT * .47 && x < WIDTH * .55 && isSolved) {
            int currentX = x;
            int currentY = y;
            int iter = 0;

            while(iter < 3 && isSolved) {
                int c = image.getRGB(currentX, currentY);
                Color color = new Color(c);

                if(!(color.getRed() < 130 && color.getGreen() < 130 && color.getBlue() > 120 &&
                        (Math.abs(color.getRed() - color.getGreen()) < 70))) {
                    isSolved = false;
                }
                System.out.println("Red: "+ color.getRed()+ " Green: " +color.getGreen() + " Blue: " +color.getBlue() + " X: "  +currentX+ " Y: " +currentY+ " is " +isSolved);
                currentX += WIDTH * 0.18;
                currentY -= HEIGHT * 0.059 + (yChange * 45);
                iter++;
            }

            y += HEIGHT * 0.09289;
            x += WIDTH * 0.18;
            yChange++;
        }

        System.out.println("\n***Blue is solved: " + isSolved+ "***");
        return isSolved;
    }

    // Checks to see if the red face is solved. The red face should be oriented to be on the right side.
    private boolean checkRedFace() {
        System.out.println("********************");
        boolean isSolved = true;
        // Starts with the pieces closest to the center
        int yStart = (int) (Y_RED_START_PERCENTAGE * HEIGHT);
        int xStart = (int) (X_RED_START_PERCENTAGE * WIDTH);

        // x and y is the position of the rows on the red face
        int y = yStart;
        int x = xStart;
        // To take into account the angle of the picture taken
        int xChange = 0;
        int yChange = 0;
        while(y < HEIGHT * 0.95 && isSolved) {
            int currentX = x;
            int currentY = y;
            int iter = 0;

            while(iter < 3 && isSolved) {
                int c = image.getRGB(currentX, currentY);
                Color color = new Color(c);
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                if(!(red > 110 && green < red && blue < red && red - green > 40 && red - blue > 40 &&
                        (Math.abs(green - blue) < 45))) {
                    isSolved = false;
                }
                System.out.println("Red: "+ color.getRed()+ " Green: " +color.getGreen() + " Blue: " +color.getBlue() + " X: "  +currentX+ " Y: " +currentY+ " is " +isSolved);
                currentX += WIDTH * 0.152 - (xChange * 40);
                currentY -= HEIGHT * 0.1004 + (yChange *20);
                iter++;
            }

            y += HEIGHT * 0.1746;
            xChange++;
            yChange++;
        }

        System.out.println("\n***Red is solved: " + isSolved+ "***");
        return isSolved;
    }

    //Checks to see if the white face is solved. The white face should be oriented to be on the left side.
    private boolean checkWhiteFace() {
        System.out.println("********************");
        boolean isSolved = true;
        // Starts with the pieces closest to the middle of the picture (Right to left)
        int yStart = (int) (Y_WHITE_START_PERCENTAGE * HEIGHT);
        int xStart = (int) (X_WHITE_START_PERCENTAGE * WIDTH);

        // x and y is the positions of the rows of the white face
        int y = yStart;
        int x = xStart;
        // To take into account the angle of the picture taken
        int xChange = 0;
        int yChange = 0;

        int row = 0;
        while(y < HEIGHT * .95  && isSolved) {
            int currentX = x;
            int currentY = y;
            int iter = 0;
            int col = 0;

            while(iter < 3 && isSolved) {
                if(!(row == 1 && col == 1)) {
                    int c = image.getRGB(currentX, currentY);
                    Color color = new Color(c);
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();

                    if(!(red > 100 && green > 100 && blue > 100)) {
                        isSolved = false;
                    }
                    System.out.println("Red: "+ color.getRed()+ " Green: " +color.getGreen() + " Blue: " +color.getBlue() + " X: "  +currentX+ " Y: " +currentY+ " is " +isSolved);
                }
                currentX -= WIDTH * 0.152 - (xChange * 20);
                currentY -= HEIGHT * 0.1004 + (yChange *20);
                iter++;
                col++;
            }

            y += HEIGHT * 0.1746;
            xChange++;
            yChange++;
            row++;
        }

        System.out.println("\n***White is solved: " + isSolved+ "***");
        return isSolved;
    }


}
