package kohi.components;

import kohi.core.Component;

public class FontRenderer extends Component {
    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found font-kohi.core.renderer.");
        }
    }

    @Override
    public void update(float dt) {

    }
}
