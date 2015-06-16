package src;


import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;


public class Player extends NeonObject{
	public int speed = 15;
	public int maxSpeed = 5;
	private double animateTarget = 0;
	public static double bulletTimerMax = 2.5;
	public double bulletTimer = bulletTimerMax;
	public double bulletRads = 0;
	public static double burnerTimerMax = 5;
	public double burnerTimer = burnerTimerMax;
	public static double bombTimerMax = 5;
	public double bombTimer = bombTimerMax;
	public Camera cam; 
	public int level = 2;
	public int score = 0;
	public int multi = 1;
	public int lives = 3;
	public int bombs = 3;
	public Player(Vector2 pos, Vector2 target, Vector2 windowSize) {
		super(pos, target);
		cam = new Camera(pos.sub(windowSize.x(0.5)));
		cam.offset = windowSize.x(0.5);
		originalShape.xpoints = new int[]{0, (int)(-4.0*size/5), (int)(-3.0*size/5), -size,			     0, 				size, 			  (int)(3.0*size/5), (int)(4.0*size/5)};
		originalShape.ypoints = new int[]{0, (int)(-size/2.0),   -size,              (int)(-size/4.0),   (int)(3.0*size/5), (int)(-size/4.0), -size, 			 (int)(-size/2.0) };
		shape.xpoints = new int[8];
		shape.ypoints = new int[8];
		this.size = size;
		col = Color.WHITE;
	}
	public void animate(){
		if(exploding==false){
			if(getVel().magnitude()>1)
				animateTarget = getVel().arcTan();
			if(animate>animateTarget&&Math.abs(animate-animateTarget)>Math.PI)
				animateTarget+=Math.PI*2;
			else if(animate<animateTarget&&Math.abs(animate-animateTarget)>Math.PI)
				animateTarget-=Math.PI*2;
			animate += (animateTarget-animate)/4;
			shape.xpoints = new int[originalShape.xpoints.length];
			shape.ypoints = new int[originalShape.ypoints.length];
			for(int i=0;i<8;i++){
				Vector2 newPos = new Vector2(originalShape.xpoints[i],originalShape.ypoints[i]).rot(Math.toDegrees(animate), new Vector2(0,0));
				shape.xpoints[i] = (int)newPos.getX();
				shape.ypoints[i] = (int)newPos.getY();
			}
			bulletTimer--;
			if(opacity<growthThreshhold&&(opacity<growthThreshhold-0.035||opacity>growthThreshhold-0.03)&&(opacity<growthThreshhold-0.025||opacity>growthThreshhold-0.02)&&(opacity<growthThreshhold-0.015||opacity>growthThreshhold-0.01)&&(opacity<growthThreshhold-0.005||opacity>growthThreshhold)){
				Polygon tempShape = new Polygon();
				tempShape.xpoints = new int[originalShape.xpoints.length + 10];
				tempShape.ypoints = new int[originalShape.ypoints.length + 10];
				for(int i=0;i<originalShape.xpoints.length;i++){
					tempShape.xpoints[i] = shape.xpoints[i];
					tempShape.ypoints[i] = shape.ypoints[i];
				}

				tempShape.xpoints[originalShape.xpoints.length] = shape.xpoints[0];
				tempShape.ypoints[originalShape.xpoints.length] = shape.ypoints[0];

				for(int i=0;i<=8;i++){
					tempShape.xpoints[1+i+originalShape.xpoints.length] = (int)(Math.cos(i*Math.PI/4)*size*1.35);
					tempShape.ypoints[1+i+originalShape.xpoints.length] = (int)(Math.sin(i*Math.PI/4)*size*1.35);
				}
				shape.xpoints = tempShape.xpoints;
				shape.ypoints = tempShape.ypoints;
			}
		}
	}
	public void move(){
		super.move();
		cam.pos = getPos().sub(cam.offset);
	}
}
