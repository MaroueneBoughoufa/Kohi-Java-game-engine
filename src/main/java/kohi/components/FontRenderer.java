package kohi.components;

import kohi.Component;

public class FontRenderer extends Component {
    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found font-kohi.renderer.");
        }
    }

    @Override
    public void update(float dt) {

    }
}
