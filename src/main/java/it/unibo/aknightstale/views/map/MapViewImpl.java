package it.unibo.aknightstale.views.map;

import it.unibo.aknightstale.models.entity.Direction;
import it.unibo.aknightstale.models.entity.EntityType;
import it.unibo.aknightstale.views.BaseView;
import it.unibo.aknightstale.views.entity.EntityView;
import it.unibo.aknightstale.controllers.interfaces.MapController;
import it.unibo.aknightstale.views.interfaces.MapView;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class MapViewImpl extends BaseView<MapController> implements MapView  {

    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane pane;

    private GraphicsContext gc;

    private final List<Tile> tiles = new ArrayList<>();
    private final List<String> keyPressed = new ArrayList<>();

    private double tileWidth;

    private double tileHeight;

    private AnimationTimer gameLoop;
    private boolean gameFinished = false;

    public MapViewImpl() {
        super("Game");
        // adding tiles
        tiles.add(new CrossableTile("grass01.png", 0, EntityType.TILE));
        tiles.add(new SolidTile("tree.png", 1, EntityType.OBSTACLE));
        tiles.add(new SolidTile("wall.png", 2, EntityType.OBSTACLE));
        tiles.add(new SolidTile("water03.png", 3, EntityType.OBSTACLE));
        tiles.add(new SolidTile("water04.png", 4, EntityType.OBSTACLE));
        tiles.add(new SolidTile("water05.png", 5, EntityType.OBSTACLE));
        tiles.add(new SolidTile("water06.png", 6, EntityType.OBSTACLE));
        tiles.add(new SolidTile("water07.png", 7, EntityType.OBSTACLE));
        tiles.add(new SolidTile("water08.png", 8, EntityType.OBSTACLE));
        tiles.add(new SolidTile("water09.png", 9, EntityType.OBSTACLE));
        tiles.add(new SolidTile("water10.png", 10, EntityType.OBSTACLE));
        tiles.add(new SolidTile("water11.png", 11, EntityType.OBSTACLE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

        getWindow().getCurrentScene().setOnKeyPressed(event -> {
            final var keyName = event.getCode().toString();
            if (!keyPressed.contains(keyName)) {
                keyPressed.add(keyName);
            }
        });

        getWindow().getCurrentScene().setOnKeyReleased(event -> {
            final var keyName = event.getCode().toString();
            if (keyPressed.contains(keyName)) {
                keyPressed.remove(keyName);
            }
        });

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        this.gc = this.canvas.getGraphicsContext2D();

        canvas.widthProperty().addListener(evt -> {
            getController().repositionEntities();
            getController().updateScreenSize();
            getController().drawMap();
        });
        canvas.heightProperty().addListener(evt -> {
            getController().repositionEntities();
            getController().updateScreenSize();
            getController().drawMap();
        });

        final var player = getController().getPlayer();

        this.gameLoop = new AnimationTimer() {

            @Override
            public void handle(final long now) {
                clearMap();
                getController().drawMap();
                getController().update();
                getController().drawEnemies();
                getController().drawPlayer();

                gc.save();

                handleInput();

                player.getView().drawHealthBar(
                        gc,
                        player.getModel().getPosition().getX(),
                        player.getModel().getPosition().getY() - 10,
                        player.getModel().getHealth(),
                        player.getModel().getMaxHealth()
                );
                getController().getEnemies().forEach(enemy -> {
                    enemy.getView().drawHealthBar(
                            gc,
                            enemy.getModel().getPosition().getX(),
                            enemy.getModel().getPosition().getY() - 10,
                            enemy.getModel().getHealth(),
                            enemy.getModel().getMaxHealth()
                    );
                });
                gc.restore();

            }
        };
        gameLoop.start();

        final Runnable myThread = () -> {
            while (!gameFinished) {
                try {
                    getController().moveEnemies();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println("Error");
                }
            }
        };

        final Thread run = new Thread(myThread);

        run.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopGame() {
        this.gameLoop.stop();
        this.gameFinished = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tile getFloor() {
        return this.tiles.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tile getTree() {
        return this.tiles.get(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTileWidth() {
        return tileWidth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTileHeight() {
        return tileHeight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tile> getTiles() {
        return tiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getScreenWidth() {
        return canvas.getWidth();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getScreenHeight() {
        return canvas.getHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearMap() {
        this.gc.clearRect(0, 0, this.gc.getCanvas().getWidth(), this.gc.getCanvas().getHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(final EntityView tile, final double x, final double y) {
        gc.drawImage(tile.getImage(), x, y);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void resizeTiles(final double tileWidth, final double tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tiles.forEach(t -> {
            t.setHeight(this.tileHeight);
            t.setWidth(this.tileWidth);
        });
    }

    private void handleInput() {
        if (keyPressed.isEmpty()) {
            getController().setIdlePlayer();
        } else {
            if (keyPressed.contains("A")) {
                getController().updatePlayer(Direction.LEFT);
            }
            if (keyPressed.contains("D")) {
                getController().updatePlayer(Direction.RIGHT);
            }
            if (keyPressed.contains("W")) {
                getController().updatePlayer(Direction.UP);
            }
            if (keyPressed.contains("S")) {
                getController().updatePlayer(Direction.DOWN);
            }
            if (keyPressed.contains("SPACE")) {
                getController().playerAttack();
            }
        }
    }
}
