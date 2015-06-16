package src;


import java.awt.Color;
import java.awt.Polygon;


public class Hourglass extends NeonObject{
	Vector2[] squares = new Vector2[2];
	public Hourglass(Vector2 pos, Vector2 target) {
		super(pos, target);
		size*=2;
		shape.xpoints = new int[]{-size/3,-size/3,size/3,size/3,-size/3,-size/3,0,-size/3,-size/3,size/3,size/3,-size/3,-size/3,size/3,size/3,0,size/3,size/3};
		shape.ypoints = new int[]{size,size/3,size/3,size,size,2*size/3,0,-2*size/3,-size,-size,-size/3,-size/3,-size,-size,-2*size/3,0,2*size/3,size};
		squares[0] = new Vector2(0,5*size/8);
		squares[1] = new Vector2(0,-5*size/8);
		//shape.xpoints = new int[originalShape.xpoints.length];
		//shape.ypoints = new int[originalShape.ypoints.length];
		col = Color.YELLOW;
		health = 15;
		maxHealth = 15;
		opacity = 0.06;
		freeRoam = true;
		super.newTarget();
	}
	public void animate(){
		super.animate();
		if(exploding==false){
			Vector2 squareOne = squares[0].rot(animate, new Vector2(0,-5*size/8));
			Vector2 squareTwo = squares[1].rot(animate, new Vector2(0,5*size/8));
			shape.xpoints = new int[]{-3*size/8 + (int)squareOne.getX(),-3*size/8  + (int)squareOne.getX(),3*size/8  + (int)squareOne.getX(),3*size/8  + (int)squareOne.getX(),-3*size/8  + (int)squareOne.getX(),-3*size/8  + (int)squareOne.getX(),0,-3*size/8  + (int)squareTwo.getX(),-3*size/8  + (int)squareTwo.getX(),3*size/8 + (int)squareTwo.getX(),3*size/8 + (int)squareTwo.getX(),-3*size/8 + (int)squareTwo.getX(),-3*size/8 + (int)squareTwo.getX(),3*size/8 + (int)squareTwo.getX(),3*size/8 + (int)squareTwo.getX(),0,3*size/8 + (int)squareOne.getX(),3*size/8 + (int)squareOne.getX()};
			shape.ypoints = new int[]{size + (int)squareOne.getY(),size/4 + (int)squareOne.getY(),size/4 + (int)squareOne.getY(),size + (int)squareOne.getY(),size + (int)squareOne.getY(),2*size/4  + (int)squareOne.getY(),0,-2*size/4  + (int)squareTwo.getY(),-size + (int)squareTwo.getY(),-size + (int)squareTwo.getY(),-size/4 + (int)squareTwo.getY(),-size/4 + (int)squareTwo.getY(),-size + (int)squareTwo.getY(),-size + (int)squareTwo.getY(),-2*size/4 + (int)squareTwo.getY(),0,2*size/4 + (int)squareOne.getY(),size + (int)squareOne.getY()};
		}
	}
}
