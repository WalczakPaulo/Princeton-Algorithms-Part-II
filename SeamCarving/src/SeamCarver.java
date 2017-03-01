import edu.princeton.cs.algs4.Picture;
import java.awt.*;
import java.util.Arrays;

/**
 * Created by Paul on 2017-03-01.
 */
public class SeamCarver {

    private Picture picture;
    private double energy[][];
    private int edgeTo[][];
    double[][] distTo = new double[width()][height()];
    private final int MIN_ENERGY = 0;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        energy = new double[picture.width()][picture.height()];
        edgeTo = new int[picture.width()][picture.height()];
        for(int x = 0; x < width(); x++)
            for(int y = 0; y < height(); y++) {
                energy[x][y] = energy(x, y);
                edgeTo[x][y] = -1;
            }
    }               // create a seam carver object based on the given picture

    public Picture picture() {
        return picture;
    }                         // current picture

    public int width() {
        return picture.width();
    }                           // width of current picture

    public int height() {
        return picture.height();
    }                          // height of current picture

    private double energy(int x, int y) {
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1)
            return MIN_ENERGY;
        if(isIndexOufOfBound(x,y))
            throw new IndexOutOfBoundsException();
        double xGrad = calculateGradient(x-1,y,x+1,y);
        double yGrad =  calculateGradient(x,y-1,x,y+1);
        return (xGrad + yGrad);
    }               // energy of pixel at column x and row y

    private double calculateGradient(int x, int y, int x1, int y1) {
        int redGradient = Math.abs(picture.get(x,y).getRed() - picture.get(x1,y1).getRed());
        int greenGradient = Math.abs(picture.get(x,y).getGreen() - picture.get(x1,y1).getGreen());
        int blueGradient = Math.abs(picture.get(x,y).getBlue() - picture.get(x1,y1).getBlue());
        return Math.sqrt(redGradient^2 + greenGradient^2 + blueGradient^2);
    }

    private boolean isIndexOufOfBound(int x, int y) {
        if(x < 0 || x > picture.width() || y < 0 || y > picture.height())
            return true;
        else
            return false;
    }

    private void transposePicture(){
        Picture transposedPic = new Picture(width(), height());
        double[][] transposedEnergy = new double[width()][height()];
        for(int x = 0; x < width(); x++)
            for(int y = 0; y < height(); y++) {
                transposedPic.set(y, x, picture.get(x,y));
                transposedEnergy[y][x] = energy[x][y];
            }

        picture = transposedPic;
        energy = transposedEnergy;
        edgeTo = new int[width()][height()];
    }


    public int findLastMinCol() {
        double minDistance = Double.MAX_VALUE;
        int minCol = -1;
        for(int col = 0; col < width(); col++){
            if(distTo[col][height() - 1] < minDistance){
                minDistance = distTo[col][height() - 1];
                minCol = col;
            }
        }
        return minCol;
    }

    public void relax(int previousRow, int previousCol, int row, int col, double[][] distTo) {
        if(isIndexOufOfBound(row, col))
            return;
        double newDistance = distTo[previousCol][previousRow] + energy[col][row];
        if( newDistance < distTo[col][row] ) {
            distTo[col][row] = newDistance;
            edgeTo[col][row] = previousCol;
        }
    }


    public int[] findHorizontalSeam() {
        transposePicture();
        int[] seam = findVerticalSeam();
        removeVerticalSeam(seam);
        transposePicture();
        return seam;
    }            // sequence of indices for horizontal seam

    public int[] findVerticalSeam() {
        int seamToCut[] = new int[height()];
        for (int row = 1; row < height(); row++) {
            Arrays.fill(distTo[row], Double.POSITIVE_INFINITY);
            for (int col = 0; col < width(); col++) {
                relax(row - 1, col, row, col - 1, distTo);
                relax(row - 1, col, row, col, distTo);
                relax(row - 1, col, row, col + 1, distTo);
            }
        }

        seamToCut[height()-1] = findLastMinCol();
        for(int y = height() - 2; y >= 0; y--) {
            seamToCut[y] = edgeTo[y][seamToCut[y+1]];
        }
        return seamToCut;
    }                // sequence of indices for vertical seam

    public void removeVerticalSeam(int[] seam) {
        Color tempPixel;
        for(int row = 0; row < height(); row++){
            for(int col = width() - 1;col > seam[row]  ; col--){
                if(col != seam[row])
                    tempPixel = picture.get(col - 1, row);
                picture.set(col - 1, row, picture.get(col, row));
            }
        }
    }

}