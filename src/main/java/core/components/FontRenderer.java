package core.components;

import core.Component;

public class FontRenderer extends Component {
    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found font-core.renderer.");
        }
    }

    @Override
    public void update(float dt) {

    }
}
