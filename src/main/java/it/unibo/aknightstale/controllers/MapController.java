package it.unibo.aknightstale.controllers;

import it.unibo.aknightstale.models.map.SpawnerImpl;
import it.unibo.aknightstale.views.map.MapView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class MapController /*implements Initializable*/ {

    /*@FXML Canvas canvas;
    @FXML AnchorPane pane;

    private GraphicsContext gc;*/

    Map<Pair<Integer, Integer>, Integer> mapTileNum = new HashMap<>();
    List<ObstacleController> obstacleControllers = new LinkedList<>();

    private static final int NUM_COL = 48;
    private static final int NUM_ROW = 27;
    private double screenWidth;
    private double screenHeight;

    private MapView mapView;

    //private static final int TILE_SIZE = 16;    // size of a single tile

    public MapController(final MapView mapView){
        this.mapView = mapView;
        mapView.setMapController(this);
        updateScreenSize();
        // converting map
        readTextMap();
        //adding trees
        Spawner treeSpawner = new Spawner(mapView.getTree(), 30, this.mapTileNum);
        this.mapTileNum = treeSpawner.getMap();
    }

   /* @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        canvas.widthProperty().addListener(evt -> {updateScreenSize();drawMap();});
        canvas.heightProperty().addListener(evt -> {updateScreenSize();drawMap();});
        this.gc = this.canvas.getGraphicsContext2D();*/
    //this.mapView = new MapView(this.gc);
        /*updateScreenSize();
        // converting map
        readTextMap();
        //adding trees
        Spawner treeSpawner = new Spawner(mapView.getTree(), 30, this.mapTileNum);
        this.mapTileNum = treeSpawner.getMap();
    }*/

    /*public double getScreenWidth() { return screenWidth; }

    public double getScreenHeight() { return screenHeight; }*/

    public static int getNumCol() {
        return NUM_COL;
    }

    public static int getNumRow() {
        return NUM_ROW;
    }

    /*public GraphicsContext getGraphic(){
        return this.canvas.getGraphicsContext2D();
    }*/

    public void drawMap() {

        int row = 0;
        int col = 0;
        int x = 0;
        int y = 0;

        mapView.clearMap();
        int num;
        while (col < NUM_COL && row < NUM_ROW) {
            num = mapTileNum.get(new Pair<Integer, Integer>(row, col));
            //num = 0;
            //gc.drawImage(mapView.getTiles().get(num).getImage(), x, y);
            mapView.drawTile(mapView.getTiles().get(num), x, y);
            col++;
            x += this.mapView.getTiles().get(num).getWidth();
            if (col == NUM_COL) {
                x = 0;
                y += this.mapView.getTiles().get(num).getHeight();;
                col = 0;
                row++;
            }
        }
    }

    private void readTextMap() {
        InputStream is = getClass().getResourceAsStream("/maps/map.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        int col = 0;
        int row = 0;
        try {
            while (col < NUM_COL && row < NUM_ROW) {

                String line = br.readLine();

                while (col < NUM_COL) {
                    List<String> numLine = Arrays.asList(line.split(" "));
                    assert(numLine.size() == NUM_COL);
                    int num = Integer.parseInt(numLine.get(col));
                    mapTileNum.put(new Pair<>(row, col), num);
                    // If I have to draw a tile that represent an obstacle, then I'll create an obstacle entity
                    if(mapView.getTiles().get(num).getEntityType() == EntityType.OBSTACLE){
                        // create obstacle entity and adds it to list
                        double x = col * mapView.getTiles().get(num).getWidth();
                        double y = col * mapView.getTiles().get(num).getHeight();

                        this.obstacleControllers.add(new ObstacleController(new ObstacleEntity(new Point2D(x,y)), (SolidTile) mapView.getTiles().get(num)));
                    }
                    col++;
                }

                if (col == NUM_COL) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void updateScreenSize(){
        this.screenWidth = mapView.getScreenWidth();
        this.screenHeight = mapView.getScreenHeight();

        final double tileWidth = Math.ceil (this.screenWidth/NUM_COL);
        final double tileHeight = Math.ceil (this.screenHeight/NUM_ROW);

        mapView.resize(tileWidth, tileHeight);
    }

}