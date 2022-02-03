package kohi.scenes;

import imgui.ImGui;
import kohi.components.SpriteRenderer;
import kohi.components.SpriteSheet;
import kohi.core.*;
import kohi.core.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Editor extends Scene {

    private GameObject Obj;
    private SpriteSheet sprites;

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        if (levelLoaded) {
            this.activeGameObject = gameObjects.get(0);
            return;
        }

        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        Obj = new GameObject("Obj", new Transform(new Vector2f(500, 100), new Vector2f(200, 200)));
        SpriteRenderer Obj2Sprite = new SpriteRenderer();
        Obj2Sprite.setColor(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
        Obj.addComponent(Obj2Sprite);
        this.addGameObjectToScene(Obj);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"), 16, 16, 26, 0));
    }

    @Override
    public void update(float dt) {
        for (GameObject g : this.gameObjects) {
            g.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Test window");
        ImGui.text("Some random text");
        ImGui.end();
    }
}
