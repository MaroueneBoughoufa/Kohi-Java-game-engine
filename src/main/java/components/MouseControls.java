package components;

import core.GameObject;
import core.Window;

public class MouseControls extends Component{
    GameObject holdingObject = null;

    public void pickupObject(GameObject g) {
        this.holdingObject = g;
        Window.getCurrentScene().addGameObjectToScene(g);
    }

    public void place() {
        this.holdingObject = null;
    }
}
