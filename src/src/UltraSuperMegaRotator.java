package src;


import java.awt.Color;
import java.awt.Polygon;


public class UltraSuperMegaRotator extends NeonObject{
	public SuperMegaRotator[] supermegarotators = new SuperMegaRotator[4];
	public UltraSuperMegaRotator(Vector2 pos, Vector2 target) {
		super(pos, target);
		size*=16;
		originalShape.xpoints = new int[]{size/8,0,-size/8,-size,-size/8,0,size/8,size,3*size/4,0,-3*size/4,-size,-3*size/4,0,3*size/4,size,size/8,-size/8,-size/8,size/8};
		originalShape.ypoints = new int[]{size/8,size,size/8,0,-size/8,-size,-size/8,0,3*size/4,size,3*size/4,0,-3*size/4,-size,-3*size/4,0,size/8,size/8,-size/8,-size/8};
		shape.xpoints = new int[20];
		shape.ypoints = new int[20];
		for(int p=0;p<4;p++){
			supermegarotators[p] = new SuperMegaRotator(pos.add(new Vector2(Math.cos((Math.PI)/2 * p)*size, Math.sin((Math.PI)/2 * p)*size)), this.getPos().add(new Vector2(Math.cos((Math.PI)/2 * p)*size, Math.sin((Math.PI)/2 * p)*size)));
			supermegarotators[p].attached = true;
		}
		col = Color.white;
		health = 2500;
		maxHealth = 2500;
		opacity = 0.06;
		freeRoam = true;
		super.newTarget();
	}
	public void animate(){
		super.animate();
		if(exploding==false){
			for(int i=0;i<shape.xpoints.length;i++){
				Vector2 newPos = new Vector2(originalShape.xpoints[i],originalShape.ypoints[i]).rot(-animate, new Vector2(0,0));
				shape.xpoints[i] = (int)newPos.getX();
				shape.ypoints[i] = (int)newPos.getY();
			}
			for(int p=0;p<4;p++){
				supermegarotators[p].setPos(getPos().add(new Vector2(Math.cos((Math.PI)/2 * p)*size, Math.sin((Math.PI)/2 * p)*size).rot(-animate, new Vector2(0,0))));
			}
		}
	}
}
