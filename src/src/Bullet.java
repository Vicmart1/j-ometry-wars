package src;


import java.awt.Color;
import java.awt.Polygon;


public class Bullet extends NeonObject{
	public Vector2 acel = new Vector2(0,0);
	public Bullet(Vector2 pos, Vector2 target, double rads){
		super(pos, target);
		size/=2;
		originalShape.xpoints = new int[]{-size, size, -size};
		originalShape.ypoints = new int[]{(int)(-size/2.0),0,(int)(size/2.0)};
		shape.xpoints = new int[3];
		shape.ypoints = new int[3];
		rotate(rads);
		col = Color.ORANGE;
		changeSpeed(10);
	}
	public void rotate(double rads){
		for(int i=0;i<shape.xpoints.length;i++){
			Vector2 newPos = new Vector2(originalShape.xpoints[i],originalShape.ypoints[i]).rot(Math.toDegrees(rads), new Vector2(0,0));
			shape.xpoints[i] = (int)newPos.getX();
			shape.ypoints[i] = (int)newPos.getY();
		}
	}
}