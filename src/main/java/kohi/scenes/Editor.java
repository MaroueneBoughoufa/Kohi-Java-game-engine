package kohi.scenes;

import kohi.components.SpriteRenderer;
import kohi.components.SpriteSheet;
import kohi.core.*;
import kohi.core.util.AssetPool;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Editor extends Scene {
    public Editor() {}

    private GameObject Obj1;
    private GameObject Obj2;
    private SpriteSheet sprites;

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        Obj1 = new GameObject("Obj1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 1);
        Obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(Obj1);

        Obj2 = new GameObject("Obj2", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 0);
        Obj2.addComponent(new SpriteRenderer(sprites.getSprite(14)));
        this.addGameObjectToScene(Obj2);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"), 16, 16, 26, 0))
    }

    private int spriteIndex = 0;
    private int spriteIndex2 = 14;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {
        spriteFlipTimeLeft -= dt;
        if (spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = 0.1f;
            spriteIndex++;
	        spriteIndex2++;
            if (spriteIndex > 3) {
                spriteIndex = 0;
            }
	        if (spriteIndex2 > 15) {
		        spriteIndex2 = 14;
	        }
            Obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
	        Obj2.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex2));
        }

        System.out.println("FPS: " + (1.0f / dt));
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
