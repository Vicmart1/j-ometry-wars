package src;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Axis extends NeonObject{
	private Vector2 Acel = new Vector2(0,0);
	public Vector2 OriginalPos = new Vector2(0,0);
	public Vector2 start;
	public Vector2 end;
	public boolean fluid = false;
	public Vector2 startIndex;
	public Vector2 endIndex;	
	public boolean vertical = false;
	public double targetTimer = 0;
	public Axis(Vector2 start, Vector2 end, Color col, boolean box) {
		super(start, end);
		this.start = start;
		this.end = end;
		if(box==true){
			shape.xpoints = new int[]{(int)start.getX(), (int)end.getX(), (int)end.getX(), (int)start.getX()};
			shape.ypoints = new int[]{(int)start.getY(), (int)start.getY(), (int)end.getY(), (int)end.getY()};
		}else{
			shape.xpoints = new int[]{(int)start.getX(), (int)end.getX()};
			shape.ypoints = new int[]{(int)start.getY(), (int)end.getY()};
		}
		OriginalPos = start.getValue();
		this.col = col;
	}
	public Axis(Axis start, Axis end, Color col, boolean box) {
		super(start.start, end.end);
		this.start = start.start;
		this.end = end.end;
		if(box==true){
			shape.xpoints = new int[]{(int)start.start.getX(), (int)end.end.getX(), (int)end.end.getX(), (int)start.start.getX()};
			shape.ypoints = new int[]{(int)start.start.getY(), (int)start.start.getY(), (int)end.end.getY(), (int)end.end.getY()};
		}else{
			shape.xpoints = new int[]{(int)start.start.getX(), (int)end.end.getX()};
			shape.ypoints = new int[]{(int)start.start.getY(), (int)end.end.getY()};
		}
		OriginalPos = start.start.getValue();
		fluid = true;
		this.col = col;
	}
	public void setStartorEnd(Vector2 newPoint, boolean startOrEnd, boolean box){
		if(startOrEnd==true)
			start = newPoint;
		else
			end = newPoint;
		if(box==true){
			shape.xpoints = new int[]{(int)start.getX(), (int)end.getX(), (int)end.getX(), (int)start.getX()};
			shape.ypoints = new int[]{(int)start.getY(), (int)start.getY(), (int)end.getY(), (int)end.getY()};
		}else{
			shape.xpoints = new int[]{(int)start.getX(), (int)end.getX()};
			shape.ypoints = new int[]{(int)start.getY(), (int)end.getY()};
		}
		OriginalPos = start.getValue();
	}
	public void move(){ 
		if(fluid == false){
			if(targetTimer>0){
				setVel(getTarget().sub(getPos()).x(0.1));
				targetTimer-=0.1;
			}else{
				Acel = getPos().sub(OriginalPos).neg().x(0.1);
				setVel(getVel().add(Acel).x(0.9));	
			}
			setPos(getPos().add(getVel()));	
		}
	}
}
