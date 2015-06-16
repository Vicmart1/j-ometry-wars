package src;


import java.awt.Color;
import java.awt.Polygon;


public class Bully extends NeonObject{
	public DiamondBox[] diamondboxes = new DiamondBox[4];
	public Bully(Vector2 pos, Vector2 target) {
		super(pos, target);
		size*=2;
		originalShape.xpoints = new int[]{-size,size,size,-size,-size,-3*size/4,3*size/4,size,3*size/4,3*size/4,size,3*size/4,-3*size/4,-size,-3*size/4,-3*size/4};
		originalShape.ypoints = new int[]{size,size,-size,-size,size,3*size/4,3*size/4,size,3*size/4,-3*size/4,-size,-3*size/4,-3*size/4,-size,-3*size/4,3*size/4};
		shape.xpoints = new int[originalShape.xpoints.length];
		shape.ypoints = new int[originalShape.xpoints.length];
		for(int p=0;p<4;p++){
			diamondboxes[p] = new DiamondBox(pos.add(new Vector2(Math.cos(Math.PI/4 + (Math.PI)/2 * p)*size, Math.sin(Math.PI/4 + (Math.PI)/2 * p)*size)), this.getPos().add(new Vector2(Math.cos(Math.PI/4 + (Math.PI)/2 * p)*size, Math.sin(Math.PI/4 + (Math.PI)/2 * p)*size)));
			diamondboxes[p].attached = true;
		}
		col = Color.white;
		health = 25;
		maxHealth = 25;
		freeRoam = true;
		opacity = 0.06;
		super.newTarget();
	}
	public void animate(){
		super.animate();
		if(exploding==false){
			for(int i=0;i<shape.xpoints.length;i++){
				Vector2 newPos = new Vector2(originalShape.xpoints[i],originalShape.ypoints[i]).rot(Math.cos(Math.toRadians(animate))*15, new Vector2(0,0));
				shape.xpoints[i] = (int)newPos.getX();
				shape.ypoints[i] = (int)newPos.getY();
			}
			for(int p=0;p<4;p++){
				diamondboxes[p].setPos(getPos().add(new Vector2(Math.cos(Math.PI/4 + (Math.PI)/2 * p)*size*1.25, Math.sin(Math.PI/4 + (Math.PI)/2 * p)*size*1.25).rot(Math.cos(Math.toRadians(animate))*15, new Vector2(0,0))));
			}
		}
	}
}
