package src;


import java.awt.Color;
import java.awt.Polygon;


public class Heart extends NeonObject{
	public Heart(Vector2 pos, Vector2 target) {
		super(pos, target);
		originalShape.xpoints = new int[]{0,-size/3,-2*size/3,-size,0,size,2*size/3,size/3};
		originalShape.ypoints = new int[]{-size/4,-size/2,-size/2,0,size,0,-size/2,-size/2};
		shape.xpoints = new int[originalShape.xpoints.length];
		shape.ypoints = new int[originalShape.ypoints.length];
		col = Color.red;
		maxHealth = 9;
		health = maxHealth;
		opacity = 0.06;
	}
	public void changeSizeFromOriginal(double factor){
		for(int i=0;i<shape.xpoints.length;i++){
			shape.xpoints[i] = originalShape.xpoints[i];
			shape.ypoints[i] = originalShape.ypoints[i];
			shape.xpoints[i]*=factor;
			shape.ypoints[i]*=factor;			
		}
	}
	public void animate(){
		super.animate();
		if(exploding==false)
			changeSizeFromOriginal(Math.tan(Math.abs(Math.toRadians(animate/2)))/40 + 1);
	}
}