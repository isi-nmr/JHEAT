import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class JHEAT {
    private final GridPane frame ;
    private double[][] data;
    private ArrayList<ImageView> imageViewArrayList;
    private VBox root = new VBox();
    private int roll_width;
    private int roll_height;
    private double image_width;
    private double image_height;
    private int numbeOfResponses;
    private int numberOfPoints;
    private double max;
    private double min;
    private Text hLabel;
    private Text vLabel;


    public JHEAT(double image_width, double image_height) {
        this.image_width = image_width;
        this.image_height = image_height;
        frame = new GridPane();
        frame.setHgap(5);
        frame.setVgap(5);
        frame.setPadding(new Insets(20,20,20,20));
    }
    public void plot (double[][] data) {
        this.data = data;
        this.numbeOfResponses = data.length;
        this.numberOfPoints = data[0].length;
        this.roll_height = (int) (image_height / numbeOfResponses);
        this.roll_width = numberOfPoints;
        this.max = Arrays.stream(data).flatMapToDouble(Arrays::stream).max().getAsDouble();
        this.min = Arrays.stream(data).flatMapToDouble(Arrays::stream).min().getAsDouble();
        imageViewArrayList = new ArrayList();
        for(int i=0;i<numbeOfResponses;i++){
            Image colorScale = createRollImages(i);
            ImageView imageView = new ImageView(colorScale);
            imageView.setFitWidth(image_width);
            imageView.setId(String.valueOf(i));
            imageViewArrayList.add(imageView);
        }
        root.getChildren().addAll(imageViewArrayList);
        frame.add(this.getRoot(),2,0,1,1);
        Group vGrid = createVGrid(image_height);
        Group hGrid = createHGrid(image_width);
        frame.add(vGrid, 1,0,1,1);
        frame.add(hGrid, 2,1,1,1);

        hLabel = new Text("h label");
        vLabel = new Text("v label");
        vLabel.setRotate(-90);
        frame.setHalignment(hLabel, HPos.CENTER);

        frame.add(hLabel,2,2,1,1);
        frame.add(vLabel,0,0,1,1);
        Node colorBar = this.getColorBar();
        frame.add(colorBar,3,0);
        frame.setValignment(colorBar,VPos.CENTER);



    }

    private Group createHGrid(double size) {
        Group grp = new Group();
        for(int x=0; x<=data[0].length; x+=(data[0].length/16)) {
            Text text = new Text(String.valueOf(x));
            text.setRotate(90);
            double transX = x*(((size-text.getLayoutBounds().getHeight())/data[0].length));
            text.setTranslateX(transX);
            grp.getChildren().add(text);
        }
        return grp;
    }

    private Group createVGrid(double size) {
        Group grp = new Group();
        for(int y=0; y<data.length; y++) {
            Text text = new Text(String.valueOf(y+1));
            double transY = y*((size/data.length)-text.getLayoutBounds().getHeight()/(2*data.length));
            text.setTranslateY(transY);
            grp.getChildren().add(text);

        }
        return grp;
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
        for (int y=0; y<colorbar_height; y++) {
            double value = 1-(((double) y)/colorbar_height);
            Color color = getColorForValue(value);
            for (int x=0; x<colorbar_width; x++) {
                pixelWriter.setColor(x, y, color);
            }
        }
        ImageView imageView = new ImageView(image);
        HBox colorBarFrame = new HBox();
        colorBarFrame.getChildren().add(imageView);
        Group vGrid = colorBarGrid(colorbar_height);
        colorBarFrame.getChildren().add(vGrid);
        colorBarFrame.setAlignment(Pos.CENTER);
        return colorBarFrame;
    }

    private Group colorBarGrid(double image_height) {
        Group grp = new Group();
        double[] values = new double[]{min, 0, max};
        for(double value : values) {
            Text text = new Text("  " + String.format("%.1f", value));
            double transY =  (1- ((value-min)/(max-min)))* image_height;
            text.setTranslateY(transY);
            grp.getChildren().add(text);
        }
        return grp;
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

    public GridPane getFrame() {
        return frame;
    }
}
