package physics2D.primitives;

import org.joml.Vector2f;
import physics2D.rigidbody.RigidBody2D;

public class Circle {
    private float radius = 1.0f;
    private RigidBody2D rigidBody = null;

    public float getRadius() {
        return radius;
    }

    public Vector2f getCenter() {
        return rigidBody.getPosition();
    }
}
