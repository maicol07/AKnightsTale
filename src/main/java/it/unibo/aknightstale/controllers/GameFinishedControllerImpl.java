package it.unibo.aknightstale.controllers;

import it.unibo.aknightstale.controllers.interfaces.Controller;
import it.unibo.aknightstale.controllers.interfaces.GameFinishedController;
import it.unibo.aknightstale.controllers.interfaces.MainMenuController;
import it.unibo.aknightstale.controllers.interfaces.MapController;
import it.unibo.aknightstale.controllers.interfaces.ScoreboardController;
import it.unibo.aknightstale.models.ScoreboardImpl;
import it.unibo.aknightstale.views.interfaces.GameFinishedView;
import it.unibo.aknightstale.views.interfaces.MainMenuView;
import it.unibo.aknightstale.views.interfaces.MapView;
import it.unibo.aknightstale.views.interfaces.ScoreboardView;

public class GameFinishedControllerImpl extends BaseController<GameFinishedView> implements GameFinishedController {
    private int score;

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveScore(final String name) {
        final var scoreboard = new ScoreboardImpl();
        scoreboard.load();
        scoreboard.setScore(name, score);
        scoreboard.save();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMainMenu() {
        Controller.of(MainMenuController.class, MainMenuView.class).get().showView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showScoreboard() {
        Controller.of(ScoreboardController.class, ScoreboardView.class).get().showView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showView() {
        this.getView().setScoreLabel(this.score);
        super.showView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScore() {
        return this.score;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setScore(final int score) {
        this.score = score;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startNewGame() {
        Controller.of(MapController.class, MapView.class).forceCreation().get().showView();
    }
}
