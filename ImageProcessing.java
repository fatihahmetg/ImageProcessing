/* *****************************************************************************
 *  Name:              Fatih Ahmet Gurbuz
 *  Last modified:     June 5, 2021
 **************************************************************************** */

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;

public class ImageProcessing {
    public static void main(String[] args) {

        int[][] imageData = imgToTwoD("https://m.media-amazon.com/images/I/91ON8JIbdvL.jpg");
        int[][] imageData2 = imgToTwoD(
                "https://static.wikia.nocookie.net/spongebob/images/d/d7/SpongeBob_stock_art.png/revision/latest?cb=20190921125147");

        viewImageData(imageData);
        int[][] trimmed = trimBorders(imageData, 60);
        twoDToImage(trimmed, "./ttrimmed_Image.jpg");
        twoDToImage(negativeColor(imageData), "./negativeImage.jpg");
        twoDToImage(stretchHorizontally(imageData), "./stretchedImage.jpg");
        twoDToImage(shrinkVertically(imageData), "./shrinkedImage.jpg");
        twoDToImage(invertImage(imageData), "./invertedImage.jpg");
        twoDToImage(colorFilter(imageData, -110, -100, 110), "./filteredImage.jpg");

        int[][] canvas = new int[500][500];
        twoDToImage(paintRandomImage(canvas), "./randomImage.jpg");

        int[] rgba = { 255, 0, 0, 255 };
        int myColor = getColorIntValFromRGBA(rgba);

        //paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color);
        twoDToImage(paintRectangle(imageData, 200, 200, 300, 100, myColor), "./rectangleImage.jpg");

        //generateRectangles(int[][] canvas, int numRectangles)
        twoDToImage(generateRectangles(imageData, 1000), "./art.jpg");

        int[][] allFilters = stretchHorizontally(shrinkVertically(
                colorFilter(negativeColor(trimBorders(invertImage(imageData2), 50)), 200, 20, 40)));
        twoDToImage(allFilters, "./all.jpg");
        // Painting with pixels
    }

    // Image Processing Methods
    public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {

        if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
            int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length
                    - pixelCount * 2];
            for (int i = 0; i < trimmedImg.length; i++) {
                for (int j = 0; j < trimmedImg[i].length; j++) {
                    trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
                }
            }
            return trimmedImg;
        }
        else {
            System.out.println("Cannot trim that many pixels from the given image.");
            return imageTwoD;
        }
    }

    public static int[][] negativeColor(int[][] imageTwoD) {

        int[][] negativeImage = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < negativeImage.length; i++) {
            for (int j = 0; j < negativeImage[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                rgba[0] = 255 - rgba[0];
                rgba[1] = 255 - rgba[1];
                rgba[2] = 255 - rgba[2];
                negativeImage[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return negativeImage;
    }

    public static int[][] stretchHorizontally(int[][] imageTwoD) {

        int[][] horizontalImage = new int[imageTwoD.length][imageTwoD[0].length * 2];
        int doubledColumnIndex = 0;
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                doubledColumnIndex = j * 2;
                horizontalImage[i][doubledColumnIndex] = imageTwoD[i][j];
                horizontalImage[i][doubledColumnIndex + 1] = imageTwoD[i][j];
            }
        }
        return horizontalImage;
    }

    public static int[][] shrinkVertically(int[][] imageTwoD) {

        int[][] shrinkedImage = new int[imageTwoD.length / 2][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD[0].length; i++) {
            for (int j = 0; j < imageTwoD.length - 1; j += 2) {
                shrinkedImage[j / 2][i] = imageTwoD[j][i];
            }
        }
        return shrinkedImage;
    }

    public static int[][] invertImage(int[][] imageTwoD) {

        int[][] invertedImage = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[0].length; j++) {
                invertedImage[i][j] = imageTwoD[(imageTwoD.length - 1) - i][
                        (imageTwoD[i].length - 1) - j];
            }
        }
        return invertedImage;
    }

    public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue,
                                      int blueChangeValue) {

        int[][] filteredImage = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[0].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                int newRed = rgba[0] + redChangeValue;
                int newGreen = rgba[1] + greenChangeValue;
                int newBlue = rgba[2] + blueChangeValue;
                if (newRed > 255) {
                    newRed = 255;
                }
                else if (newRed < 0) {
                    newRed = 0;
                }
                if (newGreen > 255) {
                    newGreen = 255;
                }
                else if (newGreen < 0) {
                    newGreen = 0;
                }
                if (newBlue > 255) {
                    newBlue = 255;
                }
                else if (newBlue < 0) {
                    newBlue = 0;
                }

                rgba[0] = newRed;
                rgba[1] = newGreen;
                rgba[2] = newBlue;

                filteredImage[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return filteredImage;
    }

    // Painting Methods
    public static int[][] paintRandomImage(int[][] canvas) {
        // TODO: Fill in the code for this method
        Random rand = new Random();
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                int randRed = rand.nextInt(256);
                int randGreen = rand.nextInt(256);
                int randBlue = rand.nextInt(256);
                int[] rgbaValues = { randRed, randGreen, randBlue, 255 };
                canvas[i][j] = getColorIntValFromRGBA(rgbaValues);
            }
        }
        return canvas;
    }

    public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition,
                                         int colPosition, int color) {

        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                if ((i >= rowPosition && i <= rowPosition + height) && (j >= colPosition
                        && j <= colPosition + width)) {
                    canvas[i][j] = color;
                }
            }
        }
        return canvas;
    }

    public static int[][] generateRectangles(int[][] canvas, int numRectangles) {

        Random rand = new Random();
        for (int i = 0; i < numRectangles; i++) {
            int randomWidth = rand.nextInt(canvas[0].length);
            int randomHeight = rand.nextInt(canvas.length);
            int randomRow = rand.nextInt(canvas.length - randomHeight);
            int randomColumn = rand.nextInt(canvas[0].length - randomWidth);
            int[] rgbaValues = { rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255 };
            int randomColor = getColorIntValFromRGBA(rgbaValues);

            canvas = paintRectangle(canvas, randomWidth, randomHeight, randomRow, randomColumn,
                                    randomColor);
        }

        return canvas;
    }

    // Utility Methods
    public static int[][] imgToTwoD(String inputFileOrLink) {
        try {
            BufferedImage image = null;
            if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
                URL imageUrl = new URL(inputFileOrLink);
                image = ImageIO.read(imageUrl);
                if (image == null) {
                    System.out.println("Failed to get image from provided URL.");
                }
            }
            else {
                image = ImageIO.read(new File(inputFileOrLink));
            }
            int imgRows = image.getHeight();
            int imgCols = image.getWidth();
            int[][] pixelData = new int[imgRows][imgCols];
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    pixelData[i][j] = image.getRGB(j, i);
                }
            }
            return pixelData;
        }
        catch (Exception e) {
            System.out.println("Failed to load image: " + e.getLocalizedMessage());
            return null;
        }
    }

    public static void twoDToImage(int[][] imgData, String fileName) {
        try {
            int imgRows = imgData.length;
            int imgCols = imgData[0].length;
            BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    result.setRGB(j, i, imgData[i][j]);
                }
            }
            File output = new File(fileName);
            ImageIO.write(result, "jpg", output);
        }
        catch (Exception e) {
            System.out.println("Failed to save image: " + e.getLocalizedMessage());
        }
    }

    public static int[] getRGBAFromPixel(int pixelColorValue) {
        Color pixelColor = new Color(pixelColorValue);
        return new int[] {
                pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(),
                pixelColor.getAlpha()
        };
    }

    public static int getColorIntValFromRGBA(int[] colorData) {
        if (colorData.length == 4) {
            Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
            return color.getRGB();
        }
        else {
            System.out.println("Incorrect number of elements in RGBA array.");
            return -1;
        }
    }

    public static void viewImageData(int[][] imageTwoD) {
        if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
            int[][] rawPixels = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rawPixels[i][j] = imageTwoD[i][j];
                }
            }
            System.out.println("Raw pixel data from the top left corner.");
            System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");
            int[][][] rgbPixels = new int[3][3][4];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
                }
            }
            System.out.println();
            System.out.println("Extracted RGBA pixel data from top the left corner.");
            for (int[][] row : rgbPixels) {
                System.out.print(Arrays.deepToString(row) + System.lineSeparator());
            }
        }
        else {
            System.out.println(
                    "The image is not large enough to extract 9 pixels from the top left corner");
        }
    }
}
