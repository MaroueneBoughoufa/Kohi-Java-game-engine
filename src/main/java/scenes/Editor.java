package scenes;

import components.RigidBody;
import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import core.Prefabs;
import imgui.ImGui;
import core.Camera;
import core.GameObject;
import core.Transform;
import util.AssetPool;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Editor extends Scene {

    private GameObject Obj;
    private SpriteSheet sprites;

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());
        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        if (levelLoaded) {
            this.activeGameObject = gameObjects.get(0);
            return;
        }


        Obj = new GameObject("Obj", new Transform(new Vector2f(500, 100), new Vector2f(200, 200)));
        SpriteRenderer Obj2Sprite = new SpriteRenderer();
        Obj2Sprite.setColor(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
        Obj.addComponent(Obj2Sprite);
        Obj.addComponent(new RigidBody());
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

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i=0; i < sprites.size(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTextureID();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                GameObject object = Prefabs.generateSpriteObject(sprite, spriteWidth, spriteHeight);
                // Attach to the mouse
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}
