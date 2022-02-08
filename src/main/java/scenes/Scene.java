package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.Camera;
import core.GameObject;
import components.Component;
import imgui.ImGui;
import renderer.Renderer;
import util.ComponentAdapter;
import util.GameObjectAdapter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public void sceneImgui() {
        if (activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }

        imgui();
    }

    public void imgui() {}

    public abstract void update(float dt);

    public Camera getCamera() {
        return this.camera;
    }

    public void export() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentAdapter())
                .registerTypeAdapter(GameObject.class, new GameObjectAdapter())
                .create();

        try {
            FileWriter writer = new FileWriter("level.json");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentAdapter())
                .registerTypeAdapter(GameObject.class, new GameObjectAdapter())
                .create();

        String inFile = "";

        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxGoID = -1;
            int maxCompID = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (GameObject obj : objs) {
                addGameObjectToScene(obj);

                for (Component c : obj.getComponents()) {
                    if (c.getUid() > maxCompID) {
                        maxCompID = c.getUid();
                    }
                }

                if (obj.getUid() > maxGoID) {
                    maxGoID = obj.getUid();
                }
            }

            maxGoID++;
            maxCompID++;
            GameObject.init(maxGoID);
            Component.init(maxCompID);

            this.levelLoaded = true;
        }
    }
}
