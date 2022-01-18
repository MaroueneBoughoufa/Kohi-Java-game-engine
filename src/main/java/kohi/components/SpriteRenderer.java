package kohi.components;

import kohi.Component;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }

    @Override
    public void start() {
        // Executes once the game starts
    }

    @Override
    public void update(float dt) {
        // Executes once every frame
    }

    public Vector4f getColor() {
        return this.color;
    }
}
