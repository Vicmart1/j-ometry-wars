package src;


import java.awt.Color;
import java.awt.Polygon;


public abstract class NeonObject {
	private Vector2 pos = new Vector2(0,0);
	private Vector2 vel = new Vector2(0,0);
	private Vector2 target = new Vector2(0,0);
	public Polygon shape = new Polygon();
	public Polygon originalShape = new Polygon();
	public Color col;
	public double animate = 0;
	private double speed = 2;
	public int size = 20;
	public int maxSize = 20;
	public boolean exploding = false;
	public double opacity = 1.0;
	public double health = 1;
	public double maxHealth = 1;
	public int id;
	public NeonObject[] scoreLocation;
	public boolean GUI = false;
	public boolean attached = false;
	public boolean freeRoam = false;
	public static double growthThreshhold = 0.99;
	public static Vector2 boxSize;
	public static int idNum = 0;
	public boolean boss = false;
	public NeonObject(Vector2 pos, Vector2 target){
		this.pos = pos;
		this.target = target;
		id = idNum;
		idNum++;
	}
	public void growingAndNurturing(){
		if(this instanceof Player){
			if(opacity<growthThreshhold-0.1)
				opacity+=0.01;
			else
				opacity+=0.0005;
		}else{
			opacity+=0.01;
		}
	}
	public void setTarget(Vector2 tar){
		target = tar;
	}
	public void changeSpeed(double factor){
		this.speed *= factor;
	}
	public void changeSize(double factor){
		for(int i=0;i<shape.xpoints.length;i++){
			if(originalShape!=null&&originalShape.xpoints.length==shape.xpoints.length){
				originalShape.xpoints[i] *= factor;
				originalShape.ypoints[i] *= factor;
			}
			shape.xpoints[i] *= factor;
			shape.ypoints[i] *= factor;
		}
		size *= factor;
		health *= factor;
		maxHealth *= factor;
	}
	public Vector2 getPos(){
		return pos;
	}
	public void setPos(Vector2 pos){
		this.pos = pos;
	}
	public Vector2 getVel(){
		return vel;
	}
	public void setVel(Vector2 vel){
		this.vel = vel;
	}
	public void addVel(Vector2 vel){
		this.vel = this.vel.add(vel);
	}
	public Vector2 getTarget(){
		return target;
	}
	public Polygon getShape(){
		return shape;
	}
	public Polygon getShapeToDraw(){
		Polygon draw = new Polygon();
		draw.xpoints = new int[shape.xpoints.length];
		draw.ypoints = new int[shape.ypoints.length];
		
		for(int i=0;i<shape.xpoints.length;i++){
			draw.xpoints[i] = (int)(getShape().xpoints[i] + pos.getX());
			draw.ypoints[i] = (int)(getShape().ypoints[i] + pos.getY());
		}
		return draw;
	}
	public void animate(){ 
		if(!(this instanceof Player)){
			if(this instanceof UltraSuperMegaRotator)
				animate+=0.5;
			else if(this instanceof SuperMegaRotator)
				animate+=0.75;
			else if(this instanceof MegaRotator)
				animate+=2;
			else
				animate+=5;
			if(animate>=360)
				animate = 0;
		}
	}
	public void move(){
		animate();
		
		if(exploding==false&&(opacity>growthThreshhold||(opacity>growthThreshhold-0.1&&this instanceof Player))){
			if(this instanceof Player)
				vel = target.sub(pos).x(0.75);
			else
				vel = target.sub(pos).normalize().x(speed);

			if(this instanceof Bullet&&this.col == Color.ORANGE){
				if(pos.add(vel).getX()<size/2||pos.add(vel).getX()>boxSize.getX()-size/2||pos.add(vel).getY()<size/2||pos.add(vel).getY()>boxSize.getY()-size/2)
					explode();
			}else if(this.freeRoam==true)
				if(pos.add(vel).getX()<size||pos.add(vel).getX()>boxSize.getX()-size||pos.add(vel).getY()<size||pos.add(vel).getY()>boxSize.getY()-size)
					newTarget();
			
			Vector2 newPos = new Vector2(pos.add(vel).getX(), pos.add(vel).getY());
			
			if(!(this instanceof Bullet && col == Color.WHITE)||(this.freeRoam==false&&!(this instanceof Bullet && col == Color.WHITE))){
				if(newPos.getX()<size){
					vel.setX(size-getPos().getX());
				}else if(newPos.getX()>boxSize.getX()-size){
					vel.setX(boxSize.getX()-size-getPos().getX());
				}
			
				if(newPos.getY()<size){
					vel.setY(size-getPos().getY());
				}else if(newPos.getY()>boxSize.getY()-size){
					vel.setY(boxSize.getY()-size-getPos().getY());
				}
			}
			
			if(!(this instanceof Player)){
				pos.setX(pos.getX() + vel.getX() + (Math.random()*2 - 1));
				pos.setY(pos.getY() + vel.getY() + (Math.random()*2 - 1));
			}else{
				pos.setX(pos.getX() + vel.getX());
				pos.setY(pos.getY() + vel.getY());
			}
		
			if(target.sub(pos).magnitude()<50||(this.freeRoam==true&&target.sub(pos).magnitude()<100))
				if(this instanceof Player)
					target = new Vector2(pos.getX(), pos.getY());
				else if(this.freeRoam==true)
					newTarget();
		}else if(exploding==false&&opacity<1)
			growingAndNurturing();
		else
			explode();
		if(this instanceof Player&&opacity<growthThreshhold&&opacity>growthThreshhold-0.1)
			growingAndNurturing();
	}
	public void newTarget(){
		setTarget(new Vector2(Math.random()*boxSize.getX(),Math.random()*boxSize.getY()));
	}
	public void explode(){
		shape.xpoints = new int[18];
		shape.ypoints = new int[18];
		size+=2;
		opacity-=0.02;
		for(int r=0;r<18;r++){
			shape.xpoints[r] = (int)(Math.cos(Math.toRadians(r*20))*size*(Math.random()+0.2));
			shape.ypoints[r] = (int)(Math.sin(Math.toRadians(r*20))*size*(Math.random()+0.2));
		}
		exploding = true;
	}
	public void reincarnate(){
		size = 20;
		opacity = 0.25;
		exploding = false;
		shape.xpoints = new int[originalShape.xpoints.length];
		shape.ypoints = new int[originalShape.xpoints.length];
	}
}