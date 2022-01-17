package kohi;

import components.Sprite;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Editor extends Scene {

    private float[] vertexArray = {
            // position                 // color                       // UV coordinates
            300.0f, 200.0f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f,        1, 1, // Bottom right 0
            200.0f, 300.0f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f,        0, 0, // Top left     1
            300.0f, 300.0f, 0.0f,       1.0f, 0.0f, 1.0f, 1.0f,        1, 0, // Top right    2
            200.0f, 200.0f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f,        0, 1, // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3  // bottom left triangle
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;
    private Texture testTexture;

    boolean firstTime = true;

    GameObject testObj;

    @Override
    public void init() {
        this.testObj = new GameObject("test");
        System.out.println("Creating object '"+ testObj.name +"'");
        this.testObj.addComponent(new Sprite());
        this.addGameObject(testObj);

        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/itchio-profile.jpg");

        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int UVSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + UVSize) * Float.BYTES;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, UVSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        // Moves the square
        //camera.position.x -= dt * 50.0f;

        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjMat", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uViewMat", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", (Time.getTime())*2.0f);

        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

        if (firstTime) {
            GameObject test2 = new GameObject("test2");
            System.out.println("Creating object '" + test2.name + "'");
            test2.addComponent(new Sprite());
            this.addGameObject(test2);
            firstTime = false;
        }

        for (GameObject g: gameObjects) {
            g.update(dt);
        }
    }
}