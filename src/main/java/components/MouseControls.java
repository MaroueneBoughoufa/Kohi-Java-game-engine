package components;

import core.GameObject;
import core.MouseListener;
import core.Window;

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
            holdingObject.transform.position.x = MouseListener.getOrthoX() - 16;
            holdingObject.transform.position.y = MouseListener.getOrthoY() - 16;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
