package it.unibo.aknightstale.controllers;

import it.unibo.aknightstale.controllers.interfaces.Controller;
import it.unibo.aknightstale.views.interfaces.View;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class BaseController<V extends View<? extends Controller<V>>> implements Controller<V> {
    /**
     * The view associated to this controller.
     */
    private @Getter(AccessLevel.PROTECTED) V view;

    /**
     * Registers a view with this controller.
     * @param view the view to register.
     */
    @Override
    public void registerView(final V view) {
        this.view = view;
    }

    /**
     * Removes the view from the controller.
     */
    @Override
    public void unregisterView() {
        this.view = null;
    }

    /**
     * Shows the view.
     */
    public void showView() {
        this.getView().show();
    }

    /**
     * Hides the view.
     */
    public void hideView() {
        this.getView().hide();
    }

    /**
     * Closes the view.
     */
    public void closeView() {
        this.getView().close();
    }
}
