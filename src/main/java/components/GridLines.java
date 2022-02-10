package components;

import core.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;
import util.Constants;

public class GridLines extends Component{

    @Override
    public void update(float dt) {
        Vector2f cameraPos = Window.getCurrentScene().getCamera().position;
        Vector2f projectionSize = Window.getCurrentScene().getCamera().getProjectionSize();

        int firstX = ((int)cameraPos.x / Constants.GRID_WIDTH) + 2;
        int firstY = ((int)cameraPos.y / Constants.GRID_HEIGHT) + 2;

        int numVtLines = (int)(projectionSize.x / Constants.GRID_WIDTH) + 2;
        int numHzLines = (int)(projectionSize.y / Constants.GRID_HEIGHT) + 2;

        int height = (int)projectionSize.y + Constants.GRID_HEIGHT * 2;
        int width = (int)projectionSize.x + Constants.GRID_WIDTH * 2;

        int maxLines = Math.max(numVtLines, numHzLines);
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);
        for (int i=0; i < maxLines; i++) {
            int x = firstX + (Constants.GRID_WIDTH * i);
            int y = firstY + (Constants.GRID_HEIGHT * i);

            if (i < numVtLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }

            if (i < numHzLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }
}
