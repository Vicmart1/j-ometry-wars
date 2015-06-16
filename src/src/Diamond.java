package src;


import java.awt.Color;
import java.awt.Polygon;


public class Diamond extends NeonObject{
	public Diamond(Vector2 pos, Vector2 target) {
		super(pos, target);
		shape.xpoints = new int[]{size,0,-size,0};
		shape.ypoints = new int[]{0,-size,0,size};
		col = Color.CYAN;
		health = 2;
		maxHealth = 2;
		opacity = 0.06;
	}
	public void animate(){
		super.animate();
		if(exploding==false)
			for(int i=0;i<shape.xpoints.length;i++){
				if(i%2==0)
					shape.xpoints[i] = (int)(((Math.cos(i*(Math.PI/2)))*size)+Math.cos(Math.toRadians(animate))*((double)size/2)*(i-1));
				if(i%2==1)
					shape.ypoints[i] = (int)(((Math.sin(i*(Math.PI/2)))*size)+Math.sin(Math.toRadians(animate))*((double)size/2)*(i-2));
			}
	}
}