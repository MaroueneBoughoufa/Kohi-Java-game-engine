package physics2D.rigidbody;

import org.joml.Vector2f;
import physics2D.primitives.*;
import renderer.Line2D;
import util.KMath;

public class CollisionDetect2D {

    //===============================================================
    //  Point vs. Shapes Collision Methods
    //===============================================================

    public static boolean pointOnLine(Vector2f point, Line2D line) {
        float dy = line.getEnd().y - line.getStart().y;
        float dx = line.getEnd().x - line.getStart().x;
        if (dx == 0.0f) {
            return KMath.compare(point.x, line.getStart().x);
        }

        float m = dy / dx;
        float b = line.getEnd().y - (m * line.getEnd().x);

        return point.y == m * point.x + b;
    }

    public static boolean pointInCircle(Vector2f point, Circle circle) {
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToPoint = new Vector2f(point).sub(circleCenter);

        return centerToPoint.lengthSquared() < circle.getRadius() * circle.getRadius();
    }

    public static boolean pointInAABB(Vector2f point, AABB box) {
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        return point.x <= max.x && min.x <= point.x && point.y <= max.y && min.y <= point.y;
    }

    public static boolean pointInBox2D(Vector2f point, Box2D box) {
        Vector2f pointLocalBox = new Vector2f(point);
        KMath.rotate(pointLocalBox, box.getRigidBody().getRotation(), box.getRigidBody().getPosition());

        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        return pointLocalBox.x <= max.x && min.x <= pointLocalBox.x && pointLocalBox.y <= max.y && min.y <= pointLocalBox.y;
    }

    //===============================================================
    //  Line vs. Shapes Collision Methods
    //===============================================================

    public static boolean lineInCircle(Line2D line, Circle circle) {
        if (pointInCircle(line.getStart(), circle) || pointInCircle(line.getEnd(), circle)) {
            return true;
        }

        Vector2f ab  = new Vector2f(line.getEnd()).sub(line.getStart());

        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToLineStart = new Vector2f(circleCenter).sub(line.getStart());
        float t = centerToLineStart.dot(ab) / ab.dot(ab);

        if (t < 0.0f || t > 1.0f) {
            return false;
        }

        Vector2f lineSegment = new Vector2f(line.getStart()).add(ab.mul(t));

        return pointInCircle(lineSegment, circle);
    }

    public static boolean lineInAABB(Line2D line, AABB box) {
        if (pointInAABB(line.getStart(), box) || pointInAABB(line.getEnd(), box)) {
            return true;
        }

        Vector2f unitVector = new Vector2f(line.getEnd()).sub(line.getStart());
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0.0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0.0f;

        Vector2f min = box.getMin();
        min.sub(line.getStart()).mul(unitVector);
        Vector2f max = box.getMax();
        max.sub(line.getStart()).mul(unitVector);

        float trueMin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float trueMax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));

        if (trueMax < 0 || trueMin > trueMax) {
            return false;
        }

        float t = (trueMin < 0.0f) ? trueMax : trueMin;

        return t > 0.0f && (t * t) < line.lengthSquared();
    }

    public static boolean lineInBox2D(Line2D line, Box2D box) {
        float theta = - box.getRigidBody().getRotation();
        Vector2f center = box.getRigidBody().getPosition();
        Vector2f localStart = new Vector2f(line.getStart());
        Vector2f localEnd = new Vector2f(line.getEnd());
        KMath.rotate(localStart, theta, center);
        KMath.rotate(localEnd, theta, center);

        Line2D localLine = new Line2D(localStart, localEnd);
        AABB aabb = new AABB(box.getMin(), box.getMax());

        return lineInAABB(localLine, aabb);
    }

    //===============================================================
    //  RayCast vs. Shapes Methods
    //===============================================================

    public static boolean rayCast(Circle circle, Ray2D ray, RayCastResult result) {
        RayCastResult.reset(result);

        Vector2f originToCircle = new Vector2f(circle.getCenter()).sub(ray.getOrigin());
        float radiusSqrd = circle.getRadius() * circle.getRadius();
        float originToCircleLenSqrd = originToCircle.lengthSquared();

        float a = originToCircle.dot(ray.getDirection());
        float bSqrd = originToCircleLenSqrd - (a * a);
        if (radiusSqrd - bSqrd < 0.0f) {
            return false;
        }

        float f = (float)Math.sqrt(radiusSqrd - bSqrd);
        float t = 0;
        if (originToCircleLenSqrd < radiusSqrd) {
            t = a + f;
        } else {
            t = a - f;
        }

        if (result != null) {
            Vector2f point = new Vector2f(ray.getOrigin()).add(new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(point).sub(circle.getCenter());

            result.init(point, normal, t, true);
        }

        return true;
    }

    public static boolean rayCast(AABB box, Ray2D ray, RayCastResult result) {
        RayCastResult.reset(result);

        Vector2f unitVector = ray.getDirection();
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0.0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0.0f;

        Vector2f min = box.getMin();
        min.sub(ray.getOrigin()).mul(unitVector);
        Vector2f max = box.getMax();
        max.sub(ray.getOrigin()).mul(unitVector);

        float trueMin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float trueMax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));

        if (trueMax < 0 || trueMin > trueMax) {
            return false;
        }

        float t = (trueMin < 0.0f) ? trueMax : trueMin;
        boolean hit = t > 0.0f; //&& t * t < ray.getMaximum();
        if (!hit) {
            return false;
        }

        if (result != null) {
            Vector2f point = new Vector2f(ray.getOrigin()).add(new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(ray.getOrigin()).sub(point);
            normal.normalize();

            result.init(point, normal, t, true);
        }

        return true;
    }

    public static boolean rayCast(Box2D box, Ray2D ray, RayCastResult result) {
        RayCastResult.reset(result);

        Vector2f halfSize = box.getHalfSize();

        Vector2f xAxis = new Vector2f(1, 0);
        Vector2f yAxis = new Vector2f(0, 1);
        KMath.rotate(xAxis, -box.getRigidBody().getRotation(), new Vector2f(0, 0));
        KMath.rotate(yAxis, -box.getRigidBody().getRotation(), new Vector2f(0, 0));

        Vector2f p = new Vector2f(box.getRigidBody().getPosition()).sub(ray.getOrigin());

        Vector2f f = new Vector2f(xAxis.dot(ray.getDirection()), yAxis.dot(ray.getDirection()));

        Vector2f e = new Vector2f(xAxis.dot(p), yAxis.dot(p));

        float[] tArr = {0, 0, 0, 0};
        for (int i = 0; i < 2; i++) {
            if (KMath.compare(f.get(i), 0)) {
                if (-e.get(i) - halfSize.get(i) > 0 || -e.get(i) + halfSize.get(i) < 0) {
                    return false;
                }
                f.setComponent(i, 0.00001f);
            }
            tArr[i * 2] = (e.get(i) + halfSize.get(i)) / f.get(i);
            tArr[i * 2 + 1] = (e.get(i) - halfSize.get(i)) / f.get(i);
        }

        float tMin = Math.max(Math.min(tArr[0], tArr[1]), Math.min(tArr[2], tArr[3]));
        float tMax = Math.min(Math.max(tArr[0], tArr[1]), Math.max(tArr[2], tArr[3]));

        float t = (tMin < 0.0f) ? tMax : tMin;
        boolean hit = t > 0.0f; //&& t * t < ray.getMaximum();
        if (!hit) {
            return false;
        }

        if (result != null) {
            Vector2f point = new Vector2f(ray.getOrigin()).add(new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(ray.getOrigin()).sub(point);
            normal.normalize();

            result.init(point, normal, t, true);
        }

        return true;
    }

    //===============================================================
    //  Circle vs. Shapes Methods
    //===============================================================

    public static boolean circleInLine(Circle circle, Line2D line) {
        return lineInCircle(line, circle);
    }

    public static boolean circleInCircle(Circle c1, Circle c2) {
        Vector2f vecBetweenCenters = new Vector2f(c1.getCenter()).sub(c2.getCenter());
        float radiiSum = c1.getRadius() + c2.getRadius();
        return vecBetweenCenters.lengthSquared() <= radiiSum * radiiSum;
    }

    public static boolean circleInAABB(Circle circle, AABB box) {
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        Vector2f closestPointToCircle = new Vector2f(circle.getCenter());
        if (closestPointToCircle.x < min.x) {
            closestPointToCircle.x = min.x;
        } else if (closestPointToCircle.x > max.x) {
            closestPointToCircle.x = max.x;
        }
        if (closestPointToCircle.y < min.y) {
            closestPointToCircle.y = min.y;
        } else if (closestPointToCircle.y > max.y) {
            closestPointToCircle.y = max.y;
        }

        Vector2f circleToBox = new Vector2f(circle.getCenter()).sub(closestPointToCircle);
        return circleToBox.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    public static boolean circleInBox2D(Circle circle, Box2D box) {
        Vector2f min = new Vector2f();
        Vector2f max = new Vector2f(box.getHalfSize()).mul(2.0f);

        Vector2f r = new Vector2f(circle.getCenter()).sub(box.getRigidBody().getPosition());
        KMath.rotate(r, -box.getRigidBody().getRotation(), new Vector2f(0, 0));
        Vector2f localCirclePos = new Vector2f(r).add(box.getHalfSize());

        Vector2f closestPointToCircle = new Vector2f(localCirclePos);
        if (closestPointToCircle.x < min.x) {
            closestPointToCircle.x = min.x;
        } else if (closestPointToCircle.x > max.x) {
            closestPointToCircle.x = max.x;
        }
        if (closestPointToCircle.y < min.y) {
            closestPointToCircle.y = min.y;
        } else if (closestPointToCircle.y > max.y) {
            closestPointToCircle.y = max.y;
        }

        Vector2f circleToBox = new Vector2f(localCirclePos).sub(closestPointToCircle);

        return circleToBox.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }
}
