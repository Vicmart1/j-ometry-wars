package src;


import java.awt.Color;


public class Particle extends NeonObject{
	private double timer = 0.8;
	private boolean implode = false;
	public Particle(Vector2 pos, Vector2 end, Color col, Vector2 target, boolean GUI, boolean implode) {
		super(pos, target);
		if(implode==false)
			timer = GUI==true ? 1 : 0.8;
		else{
			timer = 0.06;
			opacity = 0.06;
		}
		this.implode = implode;
		size = (int)end.magnitude()/10;
		this.GUI = GUI;
		shape.xpoints = new int[]{0, (int)end.getX()};
		shape.ypoints = new int[]{0, (int)end.getY()};
		this.col = col;
	}
	public void animate(){
		
	}
	public void move(){
		Vector2 previousPos = getPos().getValue();
		if(implode==false)
			setPos(getPos().add(getTarget().sub(getPos()).x(0.015)));
		else
			setPos(getPos().add(getTarget().sub(getPos()).x(0.06)));
			
		shape.xpoints[1] = (int)getPos().sub(previousPos).getX()*size;
		shape.ypoints[1] = (int)getPos().sub(previousPos).getY()*size;
		
		if(implode==false)
			timer-=0.007;
		else
			timer+=0.007;
			
		if(timer<0.4&&implode==false)
			timer-=0.03;
		else if(timer>0.66&&implode==true)
			timer+=0.03;
		
		opacity = timer;
		if(GUI==false&&implode==false){
			if(getPos().getX()<Math.abs(shape.xpoints[1]*2)||getPos().getX()>boxSize.getX() - Math.abs(shape.xpoints[1]*2))
				setTarget(new Vector2(getTarget().sub(getPos()).x(-1).add(getPos()).getX(),getTarget().getY()));
			if(getPos().getY()<Math.abs(shape.ypoints[1]*2)||getPos().getY()>boxSize.getY() - Math.abs(shape.ypoints[1]*2))
				setTarget(new Vector2(getTarget().getX(),getTarget().sub(getPos()).x(-1).add(getPos()).getY()));
		}
		
		if(getPos().sub(getTarget()).magnitude()<10)
			opacity = timer = 0;
	}
}
