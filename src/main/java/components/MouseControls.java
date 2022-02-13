package components;

import core.GameObject;
import core.MouseListener;
import core.Window;
import util.constants.Settings;

import static org.lwjgl.glfw.GLFW.*;

public class MouseControls extends Component{
    GameObject holdingObject = null;

    public void pickupObject(GameObject g) {
        this.holdingObject = g;
        Window.getCurrentScene().addGameObjectToScene(g);
    }

    public void place() {
        this.holdingObject = null;
    }

    @Override
    public void update(float dt) {
        if (holdingObject != null) {
            holdingObject.transform.position.x = MouseListener.getOrthoX();
            holdingObject.transform.position.y = MouseListener.getOrthoY();
            holdingObject.transform.position.x = (int)(holdingObject.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            holdingObject.transform.position.y = (int)(holdingObject.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
