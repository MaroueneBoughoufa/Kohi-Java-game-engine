package core;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMat, viewMat, inverseProjection, inverseView;
    public Vector2f position;
    private Vector2f projectionSize = new Vector2f(32.0f * 40.0f, 32.0f * 21.0f);

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMat = new Matrix4f();
        this.viewMat = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        // NOTE: be careful where you call the identity function, it can cause unexpected bugs.
        projectionMat.identity();
        // NOTE: matrix.ortho(left, right, down, up, near_plane, far_plane)
        projectionMat.ortho(0.0f, projectionSize.x, 0.0f, projectionSize.y, 0.0f, 100.0f);
        projectionMat.invert(inverseProjection);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMat.identity();
        viewMat.lookAt(new Vector3f(position.x, position.y, 20.0f), cameraFront.add(position.x, position.y, 0.0f),
                       cameraUp);
        // NOTE: view.lookAt(eye, center, up):
        // - eye is where the camera is
        // - center is where the camera is looking towards
        // - up is witch direction is up
        this.viewMat.invert(inverseView);
        return this.viewMat;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMat;
    }

    public Matrix4f getInverseProjection() {
        return this.inverseProjection;
    }

    public Matrix4f getInverseView() {
        return this.inverseView;
    }

    public Vector2f getProjectionSize() {
        return projectionSize;
    }
}
