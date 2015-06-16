package src;


import java.awt.Color;
import java.awt.Polygon;

public class Blackhole extends NeonObject{
	public boolean active = false;
	public int impTimerMax = 5;
	public double impTimer = impTimerMax;
	public double strength = 10;
	public Blackhole(Vector2 pos, Vector2 target) {
		super(pos, target);
		originalShape.xpoints = new int[12];
		originalShape.ypoints = new int[12];
		for(int r=0;r<12;r++){
			originalShape.xpoints[r] = (int)(Math.cos(r*(Math.PI/6))*size);
			originalShape.ypoints[r] = (int)(Math.sin(r*(Math.PI/6))*size);
		}
		shape.xpoints = new int[12];
		shape.ypoints = new int[12];
		col = Color.WHITE;
		health = 40;
		maxHealth = 40;
		opacity = 0.06;
		freeRoam = true;
		changeSpeed(0.5);
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
	public void move(){
			super.move();
			if(active==true)
				setTarget(getPos().add(new Vector2(Math.random()*2 - 1,Math.random()*2 - 1)));
	}
}
