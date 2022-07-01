package it.unibo.aknightstale.controllers.utils;

import it.unibo.aknightstale.controllers.interfaces.Controller;
import it.unibo.aknightstale.utils.ClassFactory;
import it.unibo.aknightstale.views.interfaces.View;
import it.unibo.aknightstale.views.utils.ViewFactory;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ControllerFactory {
    private static final Map<Class<? extends Controller<?>>, Controller<?>> CONTROLLERS = new HashMap<>();

    /**
     * Creates an instance of the controller and view classes implementing the given interfaces and attaches them (view-controller and controller-view).
     *
     * @param controllerInterface Controller interface to search implementing class to instantiate.
     * @param viewInterface View interface to search implementing class to instantiate and attach to controller and vice-versa.
     * @return An instance of the controller class implementing the interface.
     * @param <V> View interface type.
     * @param <C> Controller interface type.
     */
    public <V extends View<C>, C extends Controller<V>> C createController(final Class<C> controllerInterface, final Class<V> viewInterface) {
        if (CONTROLLERS.containsKey(controllerInterface)) {
            return controllerInterface.cast(CONTROLLERS.get(controllerInterface));
        }

        var controller = ClassFactory.createInstanceFromInterface(controllerInterface, "controllers");
        if (viewInterface != null) {
            var view = ViewFactory.loadView(viewInterface);
            controller.registerView(view);
            view.setController(controller);
        }
        CONTROLLERS.put(controllerInterface, controller);
        return controller;
    }

    /**
     * Creates an instance of the class implementing the given interface.
     *
     * @param controllerInterface Controller interface to search implementing class to instantiate.
     * @return An instance of the controller class implementing the interface.
     * @param <V> View interface type.
     * @param <C> Controller interface type.
     */
    public <V extends View<C>, C extends Controller<V>> C createController(final Class<C> controllerInterface) {
        return createController(controllerInterface, null);
    }
}
