package src;


import java.awt.Color;
import java.awt.Polygon;


public class Pinwheel extends NeonObject{
	public Pinwheel(Vector2 pos, Vector2 target) {
		super(pos, target);
		originalShape.xpoints = new int[]{0,0,-size,0,-size,-size,0,0,size,0,size,size};
		originalShape.ypoints = new int[]{0,size,size,0,0,-size,0,-size,-size,0,0,size};
		shape.xpoints = new int[12];
		shape.ypoints = new int[12];
		col = Color.MAGENTA;
		health = 5;
		maxHealth = 5;
		opacity = 0.06;
		freeRoam = true;
		super.newTarget();
	}
	public void animate(){
		super.animate();
		if(exploding==false)
			for(int i=0;i<shape.xpoints.length;i++){
				Vector2 newPos = new Vector2(originalShape.xpoints[i],originalShape.ypoints[i]).rot(animate, new Vector2(0,0));
				shape.xpoints[i] = (int)newPos.getX();
				shape.ypoints[i] = (int)newPos.getY();
			}
	}
}
