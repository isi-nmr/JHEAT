import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class JHEAT {
    private double[][] data;
    private ArrayList<ImageView> imageViewArrayList;
    private VBox root;
    private int roll_width;
    private int roll_height;
    private double image_width;
    private double image_height;
    private int numbeOfResponses;
    private int numberOfPoints;
    private double max;
    private double min;


    public JHEAT(double image_width, double image_height) {
        this.image_width = image_width;
        this.image_height = image_height;
    }
    public void plot (double[][] data) {
        this.data = data;
        this.numbeOfResponses = data.length;
        this.numberOfPoints = data[0].length;
        this.roll_height = (int) (image_height / numbeOfResponses);
        this.roll_width = numberOfPoints;
        this.max = Math.round((Arrays.stream(data).flatMapToDouble(Arrays::stream).max().getAsDouble()+99)/100)*100;
        this.min = Math.round((Arrays.stream(data).flatMapToDouble(Arrays::stream).min().getAsDouble() + 99) / 100) * 100;
        imageViewArrayList = new ArrayList();
        for(int i=0;i<numbeOfResponses;i++){
            Image colorScale = createRollImages(i);
            ImageView imageView = new ImageView(colorScale);
            imageView.setFitWidth(image_width);
            imageView.setId(String.valueOf(i));
            imageViewArrayList.add(imageView);
        }
        root.getChildren().addAll(imageViewArrayList);

    }
    private Image createRollImages(int i) {
        WritableImage image = new WritableImage(roll_width, roll_height);
        PixelWriter pixelWriter = image.getPixelWriter();
        for (int x=0; x<roll_width; x++) {
            double dataOfPoint = data[i][x] ;
            double value = (dataOfPoint-min)/(max-min);
            Color color = getColorForValue(value);
            for (int y=0; y<roll_height; y++) {
                pixelWriter.setColor(x, y, color);
            }
        }
        return image;
    }
    private Color getColorForValue(double value) {
        double hueValue = 255 * (1 - Mapper.getInstance().mapValue(value));
        if(Mapper.getInstance().isLogScale()) {
            hueValue = 255 * (1 - Math.log10(9 * Mapper.getInstance().mapValue(value) + 1));
        }
        return Color.hsb(hueValue, 1, 1);
    }
    public Node getColorBar() {
        return getColorBar(30,400);
    }
    public Node getColorBar(int colorbar_width, int colorbar_height) {
        WritableImage image = new WritableImage(colorbar_width, colorbar_height );
        PixelWriter pixelWriter = image.getPixelWriter();


        for (int y=0; y<400; y++) {
            double value = 1-(((double) y)/400);
            Color color = getColorForValue(value);
            for (int x=0; x<30; x++) {
                pixelWriter.setColor(x, y, color);
            }
        }
        ImageView imageView = new ImageView(image);
        return imageView;
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    public ArrayList<ImageView> getImageViewArrayList() {
        return imageViewArrayList;
    }

    public void setImageViewArrayList(ArrayList<ImageView> imageViewArrayList) {
        this.imageViewArrayList = imageViewArrayList;
    }

    public VBox getRoot() {
        return root;
    }

    public int getRoll_width() {
        return roll_width;
    }

    public void setRoll_width(int roll_width) {
        this.roll_width = roll_width;
    }

    public int getRoll_height() {
        return roll_height;
    }

    public void setRoll_height(int roll_height) {
        this.roll_height = roll_height;
    }

    public double getImage_width() {
        return image_width;
    }

    public void setImage_width(double image_width) {
        this.image_width = image_width;
    }

    public double getImage_height() {
        return image_height;
    }

    public void setImage_height(double image_height) {
        this.image_height = image_height;
    }

    public double getMax() {
        return max;
    }


    public double getMin() {
        return min;
    }

}
