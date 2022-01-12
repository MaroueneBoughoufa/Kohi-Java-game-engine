package kohi;

import java.awt.event.KeyEvent;

public class Editor extends Scene{
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public Editor() {
        System.out.println("Inside level editor scene");
    }

    @Override
    public void update(double dt) {
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        } else if (changingScene) {
            Window.changeScene(1);
        }
    }
}
