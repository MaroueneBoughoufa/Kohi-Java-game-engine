package kohi.scenes;

import kohi.*;
import kohi.components.Sprite;
import kohi.components.SpriteRenderer;
import kohi.components.SpriteSheet;
import kohi.util.AssetPool;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Editor extends Scene {
    public Editor() {}

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        SpriteSheet sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        GameObject Obj1 = new GameObject("Obj1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        Obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(Obj1);

        GameObject Obj2 = new GameObject("Obj20", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        Obj2.addComponent(new SpriteRenderer(sprites.getSprite(15)));
        this.addGameObjectToScene(Obj2);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"), 16, 16, 26, 0));
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
