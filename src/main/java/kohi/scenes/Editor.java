package kohi.scenes;

import kohi.*;
import kohi.components.SpriteRenderer;
import kohi.util.AssetPool;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Editor extends Scene {
    public Editor() {}

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        GameObject Obj1 = new GameObject("Obj1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        Obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/bomb-nut.png")));
        this.addGameObject(Obj1);

        GameObject Obj2 = new GameObject("Obj20", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        Obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/itchio-profile.jpg")));
        this.addGameObject(Obj2);

        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
    }

    @Override
    public void update(float dt) {
        //System.out.println("FPS: " + (1.0f / dt));
        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            camera.position.x += 100 * dt;
        } else if (KeyListener.isKeyPressed((GLFW_KEY_LEFT))) {
            camera.position.x -= 100 * dt;
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            camera.position.y += 100 * dt;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.position.y -= 100 * dt;
        }

        for (GameObject g : this.gameObjects) {
            g.update(dt);
        }

        this.renderer.render();
    }
}
