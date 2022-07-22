package it.unibo.aknightstale.views;

import com.simtechdata.sceneonefx.SceneOne;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.aknightstale.views.interfaces.View;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressFBWarnings("EI_EXPOSE_REP2") // View must be passed as reference to allow view loader caching.
public class Window {
    private static final Map<String, Window> WINDOWS = new HashMap<>();
    private final String windowId;
    private View<?> view;
    private Boolean isOpen = false;

    public Window() {
        this(UUID.randomUUID().toString());
    }

    public Window(final String windowId) {
        this.windowId = windowId;
        SceneOne.set(this.windowId, new Scene(new VBox())).build();
        WINDOWS.put(this.windowId, this);
    }

    public Window(final String windowId, final Stage stage) {
        this.windowId = windowId;
        SceneOne.set(this.windowId, new Scene(new VBox())).stage(stage).build();
        WINDOWS.put(this.windowId, this);
    }

    /**
     * Switch the scene in the window.
     */
    public void switchTo(final @NotNull View<?> view) {
        final var scene = SceneOne.getScene(view.getClass().getSimpleName());
        SceneOne.swapScene(this.windowId, scene);
        SceneOne.setTitle(this.windowId, view.getWindowTitle());
        this.view = view;
    }

    /**
     * Open the window.
     */
    public void open() {
        SceneOne.show(this.windowId);
        this.isOpen = true;
    }

    /**
     * Close the window.
     */
    public void close() {
        SceneOne.close(this.windowId);
        this.isOpen = false;
    }

    /**
     * Close the window only if the view showing is the given one.
     *
     * @param view View to check if it's showing in the window.
     */
    public void close(final @NotNull View<?> view) {
        if (view.getViewName().equals(this.getCurrentViewName())) {
            this.close();
        }
    }

    /**
     * Hide the window.
     */
    public void hide() {
        SceneOne.hide(this.windowId);
        this.isOpen = false;
    }

    /**
     * Hide the window only if the view showing is the given one.
     *
     * @param view View to check if it's showing in the window.
     */
    public void hide(final @NotNull View<?> view) {
        if (view.getViewName().equals(this.getCurrentViewName())) {
            this.hide();
        }
    }

    /**
     * Unhide the window.
     */
    public void unhide() {
        SceneOne.unHide(this.windowId);
        this.isOpen = true;
    }

    /**
     * Get the current view showing in the window.
     * @return Current view.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public View<?> getCurrentView() {
        return view;
    }

    /**
     * Get the name of the current view showing in the window.
     * @return Current view name.
     */
    public String getCurrentViewName() {
        return view.getViewName();
    }

    /**
     * Checks if the window is open.
     *
     * @return true if the window is open, false otherwise.
     */
    public Boolean isOpened() {
        return isOpen;
    }

    /**
     * Get the window with this id if it exists.
     *
     * @param windowId The id of the window to get.
     * @return The window with this id if it exists, null otherwise.
     */
    public static Window get(final String windowId) {
        return WINDOWS.get(windowId);
    }

    /**
     * Get the window with this id if it exists.
     *
     * @param windowId The id of the window to get.
     * @param stage    The stage to use to create the window.
     * @return The window with this id if it exists, it will be created otherwise.
     */
    public static Window getOrCreate(final String windowId, final Stage stage) {
        var window = get(windowId);

        if (window == null) {
            window = new Window(windowId, stage);
        }

        return window;
    }

    /**
     * Get the window with this id if it exists.
     *
     * @param windowId The id of the window to get.
     * @return The window with this id if it exists, it will be created otherwise.
     */
    public static Window getOrCreate(final String windowId) {
        return getOrCreate(windowId, null);
    }
}
