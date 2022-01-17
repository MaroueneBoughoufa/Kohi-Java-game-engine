package components;

import kohi.Component;

public class Font extends Component {
    @Override
    public void start() {
        if (gameObject.getComponent(Sprite.class) != null) {
            System.out.println("Found font-renderer.");
        }
    }

    @Override
    public void update(float dt) {

    }
}
