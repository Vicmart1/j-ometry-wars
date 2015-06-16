package src;


import java.awt.Color;
import java.awt.Polygon;


public class DiamondBox extends NeonObject{
	public DiamondBox(Vector2 pos, Vector2 target) {
		super(pos, target);
		changeSpeed(2);
		originalShape.xpoints = new int[]{size,0,   -size,0,   size, size, -size, -size, size};
		originalShape.ypoints = new int[]{0,   -size,0,   size,0,    size, size, -size, -size};
		shape.xpoints = new int[9];
		shape.ypoints = new int[9];
		col = Color.PINK;
		health = 10;
		maxHealth = 10;
		opacity = 0.06;
	}
	public void animate(){
		super.animate();
		if(exploding==false)
			for(int i=0;i<shape.xpoints.length;i++){
				Vector2 newPos = new Vector2(originalShape.xpoints[i],originalShape.ypoints[i]).rot(animate/2, new Vector2(0,0));
				shape.xpoints[i] = (int)newPos.getX();
				shape.ypoints[i] = (int)newPos.getY();
			}
	}
}