package kohi;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Camera camera;

    private boolean isRunning = false;
    private List<GameObject> gameObjects = new ArrayList<>();

    public Scene() {}

    public void init() {}

    public void start() {
        for (GameObject g : gameObjects) {
            g.start();
        }
    }

    public void addGameObject(GameObject g) {
        if (!isRunning) {
            gameObjects.add(g);
        } else {
            gameObjects.add(g);
            g.start();

        }
    }

    public abstract void update(float dt);
}
