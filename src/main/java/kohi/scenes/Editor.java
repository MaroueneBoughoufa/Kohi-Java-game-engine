package kohi.scenes;

import imgui.ImGui;
import kohi.components.SpriteRenderer;
import kohi.components.SpriteSheet;
import kohi.core.*;
import kohi.core.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

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

        Obj1 = new GameObject("Obj1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        Obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(Obj1);

        Obj2 = new GameObject("Obj2", new Transform(new Vector2f(500, 100), new Vector2f(100, 100)));
        Obj2.addComponent(new SpriteRenderer(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)));
        this.addGameObjectToScene(Obj2);
        this.activeGameObject = Obj2;
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"), 16, 16, 26, 0));
    }

    private int spriteIndex = 0;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {
        spriteFlipTimeLeft -= dt;
        if (spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = 0.1f;
            spriteIndex++;
            if (spriteIndex > 3) {
                spriteIndex = 0;
            }
            Obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }

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
