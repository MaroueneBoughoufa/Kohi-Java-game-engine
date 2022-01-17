package kohi.components;

import kohi.Component;

public class Sprite extends Component {
    boolean firstTime = true;

    @Override
    public void start() {
        // Executes once the game starts
        System.out.println("Component 'Sprite' started.");
    }

    @Override
    public void update(float dt) {
        // Executes once every frame
        if (firstTime) {
            System.out.println("Component 'Sprite' updating.");
            firstTime = false;
        }
    }
}
