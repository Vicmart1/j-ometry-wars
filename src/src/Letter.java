package src;


import java.awt.Color;


public class Letter extends NeonObject{
	public Letter(char type, Vector2 pos, Vector2 target, Color col, int size, boolean GUI) {
		super(pos, target);
		this.GUI = GUI;
		this.size = size;
		this.maxSize = size;
		rename(type);
		opacity = 0.06;
		this.col = col;
	}
	public void rename(char type){
		switch(type){
			case '0':
				shape.xpoints = new int[]{-size/2,size/2,size/2,-size/2,size/2,-size/2,-size/2};
				shape.ypoints = new int[]{size/2,size/2,-size/2,-size/2,size/2,size/2,-size/2};
				break;
			case '1':
				shape.xpoints = new int[]{-size/4,size/4,0,0,-size/4,0,0};
				shape.ypoints = new int[]{-size/2,-size/2,-size/2,size/2,size/2,size/2,-size/2};
				break;
			case '2':
				shape.xpoints = new int[]{size/2,-size/2,-size/2,size/2,size/2,-size/2,size/2,size/2,-size/2,-size/2};
				shape.ypoints = new int[]{-size/2,-size/2,0,0,size/2,size/2,size/2,0,0,-size/2};
				break;
			case '3':
				shape.xpoints = new int[]{-size/2,size/2,size/2,-size/2,size/2,size/2,-size/2,size/2,size/2};
				shape.ypoints = new int[]{-size/2,-size/2,0,0,0,size/2,size/2,size/2,-size/2};
				break;
			case '4':
				shape.xpoints = new int[]{size/2,size/2,size/2,-size/2,-size/2,-size/2,size/2};
				shape.ypoints = new int[]{-size/2,size/2,0,0,size/2,0,0};
				break;
			case '5':
				shape.xpoints = new int[]{-size/2,size/2,size/2,-size/2,-size/2,size/2,-size/2,-size/2,size/2,size/2};
				shape.ypoints = new int[]{-size/2,-size/2,0,0,size/2,size/2,size/2,0,0,-size/2};
				break;
			case '6':
				shape.xpoints = new int[]{size/2,-size/2,-size/2,size/2,size/2,-size/2,-size/2};
				shape.ypoints = new int[]{size/2,size/2,-size/2,-size/2,0,0,size/2};
				break;
			case '7':
				shape.xpoints = new int[]{size/2,size/2,-size/2,size/2};
				shape.ypoints = new int[]{-size/2,size/2,size/2,size/2};
				break;
			case '8':
				shape.xpoints = new int[]{size/2,-size/2,-size/2,size/2,size/2,size/2,-size/2,-size/2};
				shape.ypoints = new int[]{size/2,size/2,-size/2,-size/2,size/2,0,0,size/2};
				break;
			case '9':
				shape.xpoints = new int[]{-size/2,size/2,size/2,-size/2,-size/2,size/2,size/2};
				shape.ypoints = new int[]{-size/2,-size/2,size/2,size/2,0,0,-size/2};
				break;
			case 'S':
				shape.xpoints = new int[]{-size/2,size/2,size/2,-size/2,-size/2,size/2,-size/2,-size/2,size/2,size/2};
				shape.ypoints = new int[]{-size/2,-size/2,0,0,size/2,size/2,size/2,0,0,-size/2};
				break;
			case 'C':
				shape.xpoints = new int[]{size/2,-size/2,-size/2,size/2,-size/2,-size/2};
				shape.ypoints = new int[]{size/2,size/2,-size/2,-size/2,-size/2,size/2};
				break;
			case 'O':
				shape.xpoints = new int[]{-size/2,size/2,size/2,-size/2};
				shape.ypoints = new int[]{size/2,size/2,-size/2,-size/2};
				break;
			case 'R':
				shape.xpoints = new int[]{-size/2,-size/2,size/2,size/2,-size/2,size/2,-size/2};
				shape.ypoints = new int[]{-size/2,size/2,size/2,0,0,-size/2,0};
				break;
			case 'E':
				shape.xpoints = new int[]{size/2,-size/2,-size/2,size/2,-size/2,-size/2,size/2,-size/2,-size/2};
				shape.ypoints = new int[]{size/2,size/2,0,0,0,-size/2,-size/2,-size/2,size/2};
				break;
			case 'P':
				shape.xpoints = new int[]{0, (int)(-4.0*size/5), (int)(-3.0*size/5), -size,			     0, 				size, 			  (int)(3.0*size/5), (int)(4.0*size/5)};
				shape.ypoints = new int[]{0, (int)(size/2.0),   size,              (int)(size/4.0),   (int)(-3.0*size/5), (int)(size/4.0), size, 			 (int)(size/2.0) };
				break;
			case 'B':
				int maxpoints = 40;
				shape.xpoints = new int[maxpoints];
				shape.ypoints = new int[maxpoints];
				for(int r=0;r<maxpoints;r++){
					shape.xpoints[r] = (int)((3*size/4 + (r%2)*((size/3.0)*(int)(r/(maxpoints/2))))*Math.cos(r*(Math.PI*2)/(maxpoints/2)));
					shape.ypoints[r] = (int)((3*size/4 + (r%2)*((size/3.0)*(int)(r/(maxpoints/2))))*Math.sin(r*(Math.PI*2)/(maxpoints/2)));
				}	
				break;
			case ',':
				shape.xpoints = new int[]{0,-size/8};
				shape.ypoints = new int[]{-size/2, -5*(size/8)};
				break;
			case 'x':
				shape.xpoints = new int[]{size/4,-size/4,0,size/4,-size/4,0};
				shape.ypoints = new int[]{0,-size/2,-size/4,-size/2,0,-size/4};
				break;
		}
		for(int y=0;y<shape.ypoints.length;y++)
			shape.ypoints[y]*=-1;
	}
	public void animate(){ }
	public void move(){ 
		if(opacity<growthThreshhold&&exploding==false)
			opacity+=0.04;
		else if(exploding==true)
			explode();
	}
	public void explode(){
		opacity-=0.04;
		exploding = true;
	}
	
}
