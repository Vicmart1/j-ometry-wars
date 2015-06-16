package src;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class Grid {
    private Vector2 size;
    private Vector2 windowSize;
    public Color colorBorder = Color.GREEN;
    public Color colorGrid = Color.BLUE;
    public List<NeonObject> enemies = new ArrayList<NeonObject>(); 
    public int p1 = -1; 
    public NeonObject[] scoreLocs = new NeonObject[13]; 
    public NeonObject[] livesLocs = new NeonObject[10]; 
    public NeonObject[] bombsLocs = new NeonObject[10]; 
    public NeonObject[] multiLocs = new NeonObject[10]; 
    public static int enemyCount = 0;
    public int particleCount = 0;
    public int blackholeCount = 0;
    public static int particleMax = 7500;
    public static int gridSize = 25; 
    public static int letterSpace = 10; 
    public static int newLife = 100000; 
    public static int newBomb = 150000; 
    public static int incMulti = 10000;
    public static double particleEffects = 4; 
    public Axis[][] gridSpace; 
    public static boolean bossPresent = false;
    
    public Grid(Vector2 size, Vector2 windowSize){
        this.size = size;
        NeonObject.boxSize = size;
        for(int x=0;x<200;x++){
            Vector2 randPos = new Vector2(Math.random()*size.getX() - size.getX()/4, Math.random()*size.getY() - size.getY()/4);
            enemies.add(new Axis(randPos, randPos.add(new Vector2(1,1)), Color.white, true));
        }
        int letterSpace = 25;
        addString("S",new Vector2(120,50), Color.green, letterSpace - 5, true);
        addString("C",new Vector2(120 + letterSpace - 5 + 5,50), Color.green, letterSpace - 5, true);
        addString("O",new Vector2(120 + (letterSpace)*2,50), Color.green, letterSpace - 5, true);
        addString("R",new Vector2(120 + (letterSpace)*3,50), Color.green, letterSpace - 5, true);
        addString("E",new Vector2(120 + (letterSpace)*4,50), Color.green, letterSpace - 5, true);
        
        /**
        for(int x=gridSize;x<size.getX();x+=gridSize){
            enemies.add(new Axis(new Vector2(size.getX()/2,0), new Vector2(size.getX()/2-x,x), colorGrid, true));
            if(x>0){
                enemies.add(new Axis(new Vector2(size.getX()/2 - 25,25), new Vector2(size.getX()/2-x-25,x+25), colorGrid, true));
                enemies.get(enemies.size()-1).opacity = 0.4;
                enemies.add(new Axis(new Vector2(25, size.getY()/2 - 25), new Vector2(x+25, size.getY()/2 - x - 25), colorGrid, true));
                enemies.get(enemies.size()-1).opacity = 0.4;
            }
            enemies.add(new Axis(new Vector2(0, size.getY()/2), new Vector2(x, size.getY()/2-x), colorGrid, true));
        }
        **/
        
        gridSpace = new Axis[(int)(size.getX()/gridSize)][(int)(size.getY()/gridSize)];
        
        for(int x=0;x<size.getX()/2 + 1;x+=gridSize)
            for(int y=0;y<size.getY()/2 + 1;y+=gridSize){
                gridSpace[x/gridSize][y/gridSize] = new Axis(new Vector2(x,y), new Vector2(x,y), Color.blue, true);
                enemies.add(gridSpace[x/gridSize][y/gridSize]);
                if(x>0&&y>0){
                	Color col = new Color(0,51,255);
                	enemies.add(new Axis(gridSpace[x/gridSize][y/gridSize], gridSpace[x/gridSize][y/gridSize - 1], col, false));
                	((Axis)enemies.get(enemies.size()-1)).startIndex = new Vector2(x/gridSize,y/gridSize);
                	((Axis)enemies.get(enemies.size()-1)).endIndex = new Vector2(x/gridSize,y/gridSize - 1);
                	((Axis)enemies.get(enemies.size()-1)).vertical = true;
                	enemies.add(new Axis(gridSpace[x/gridSize][y/gridSize], gridSpace[x/gridSize - 1][y/gridSize], col, false));
                	((Axis)enemies.get(enemies.size()-1)).startIndex = new Vector2(x/gridSize,y/gridSize);
                	((Axis)enemies.get(enemies.size()-1)).endIndex = new Vector2(x/gridSize - 1,y/gridSize);
                }	
            }
        enemies.add(new Axis(new Vector2(0,0), size, colorBorder, true));
    
        addPlayer(size.x(0.5), windowSize);
    }
    public Player getPlayer(){
        return ((Player)enemies.get(p1));
    }
    public void addEnemy(int type, Vector2 pos, Vector2 target){
    	if(getPlayer().lives>=0)
        switch(type){
            case 0:
                enemies.add(new Diamond(pos, target));
                break;
            case 1:
                enemies.add(new Pinwheel(pos, target));
                break;
            case 2:
                enemies.add(new DiamondBox(pos, target));
                break;
            case 3:
                enemies.add(new Heart(pos, target));
                break;
            case 4:
                enemies.add(new Hourglass(pos, target));
                break;
            case 5:
            	if(blackholeCount<1){
            		enemies.add(new Blackhole(pos, target));
                	blackholeCount++;
            	}
                break;
            case 6:
                enemies.add(new Rotator(pos, target));
                enemies.get(enemies.size()-1).setTarget(getPlayer().getPos());
                Rotator rotator = ((Rotator)enemies.get(enemies.size()-1));
                for(int p=0;p<rotator.pinwheels.length;p++){
                    enemies.add(rotator.pinwheels[p]);
                    enemyCount++;
                }
                break;
            case 7:
                enemies.add(new Bully(pos, target));
                Bully bully = ((Bully)enemies.get(enemies.size()-1));
                for(int p=0;p<bully.diamondboxes.length;p++){
                    enemies.add(bully.diamondboxes[p]);
                    enemyCount++;
                }
                break;
            case 8:
                enemies.add(new MegaRotator(pos, target));
                MegaRotator megarotator = ((MegaRotator)enemies.get(enemies.size()-1));
                for(int p=0;p<megarotator.rotators.length;p++){
                    enemies.add(megarotator.rotators[p]);
                    Rotator rotator1 = ((Rotator)enemies.get(enemies.size()-1));
                    for(int q=0;q<rotator1.pinwheels.length;q++){
                        enemies.add(rotator1.pinwheels[q]);
                        enemyCount++;
                    }
                    enemyCount++;
                }
                break;
            case 9:
                enemies.add(new SuperMegaRotator(pos, target));
                SuperMegaRotator supermegarotator = ((SuperMegaRotator)enemies.get(enemies.size()-1));
                for(int p=0;p<supermegarotator.megarotators.length;p++){
                    enemies.add(supermegarotator.megarotators[p]);
                    MegaRotator megarotator1 = ((MegaRotator)enemies.get(enemies.size()-1));
                    for(int q=0;q<megarotator1.rotators.length;q++){
                        enemies.add(megarotator1.rotators[q]);
                        Rotator rotator1 = ((Rotator)enemies.get(enemies.size()-1));
                        for(int r=0;r<rotator1.pinwheels.length;r++){
                            enemies.add(rotator1.pinwheels[r]);
                            enemyCount++;
                        }
                        enemyCount++;
                    }
                    enemyCount++;
                }
                break;
            case 10:
                enemies.add(new UltraSuperMegaRotator(pos, target));
                enemies.get(enemies.size()-1).boss = true;
                UltraSuperMegaRotator ultrasupermegarotator = ((UltraSuperMegaRotator)enemies.get(enemies.size()-1));
                for(int p=0;p<ultrasupermegarotator.supermegarotators.length;p++){
                    enemies.add(ultrasupermegarotator.supermegarotators[p]);
                    enemies.get(enemies.size()-1).boss = true;
                    SuperMegaRotator supermegarotator1 = ((SuperMegaRotator)enemies.get(enemies.size()-1));
                    for(int q=0;q<supermegarotator1.megarotators.length;q++){
                        enemies.add(supermegarotator1.megarotators[q]);
                        enemies.get(enemies.size()-1).boss = true;
                        MegaRotator megarotator1 = ((MegaRotator)enemies.get(enemies.size()-1));
                        for(int r=0;r<megarotator1.rotators.length;r++){
                            enemies.add(megarotator1.rotators[r]);
                            enemies.get(enemies.size()-1).boss = true;
                            Rotator rotator1 = ((Rotator)enemies.get(enemies.size()-1));
                            for(int s=0;s<rotator1.pinwheels.length;s++){
                                enemies.add(rotator1.pinwheels[s]);
                                enemies.get(enemies.size()-1).boss = true;
                                enemyCount++;
                            }
                            enemyCount++;
                        }
                        enemyCount++;
                    }
                    enemyCount++;
                }
                bossPresent = true;
                break;
            default:
                break;
        }
        enemyCount++;
    }
    public void addPlayer(Vector2 pos, Vector2 windowSize){
        if(p1==-1){
            this.windowSize = windowSize;
            enemies.add(new Player(pos, pos, windowSize));
            p1 = enemies.size()-1;
            refreshScore();
            refreshLives();
            refreshBombs();
            refreshMulti();
        }
    }
    public NeonObject[] addString(String word, Vector2 pos, Color col, int size ,boolean GUI){
        NeonObject[] locs = new NeonObject[word.length()];
        for(int s=0;s<word.length();s++){
            enemies.add(new Letter(word.charAt(s),pos.add(new Vector2(s*(size+5) - (size+5)*2,0)),pos.add(new Vector2(s*(size+5) - (size+5)*2,0)), col, size, GUI));
            locs[s] = enemies.get(enemies.size()-1);
            if(GUI==true)
                locs[s].opacity = NeonObject.growthThreshhold;
        }
        return locs;
    }
    public void addBullet(int num, double rads){
        if(((Player)enemies.get(p1)).bulletTimer<0||num==7){
            if(getPlayer().bulletRads>rads&&Math.abs(rads - getPlayer().bulletRads)>Math.PI)
                rads += Math.PI*2;
            else if(getPlayer().bulletRads<rads&&Math.abs(rads - getPlayer().bulletRads)>Math.PI)
                rads -= Math.PI*2;
            getPlayer().bulletRads += (rads - getPlayer().bulletRads)/2;
            getPlayer().bulletRads = getPlayer().bulletRads%(Math.PI*2);
            switch(num){
                case 2:
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() + Math.sin(getPlayer().bulletRads)*10 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() + Math.cos(getPlayer().bulletRads)*10 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() - Math.sin(getPlayer().bulletRads)*10 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() - Math.cos(getPlayer().bulletRads)*10 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    break;
                case 3:
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() + Math.sin(getPlayer().bulletRads)*15 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() + Math.cos(getPlayer().bulletRads)*15 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() - Math.sin(getPlayer().bulletRads)*15 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() - Math.cos(getPlayer().bulletRads)*15 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    break;
                case 4:
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() + Math.sin(getPlayer().bulletRads)*10 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() + Math.cos(getPlayer().bulletRads)*10 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    enemies.get(enemies.size()-1).changeSize(2);
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() - Math.sin(getPlayer().bulletRads)*10 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() - Math.cos(getPlayer().bulletRads)*10 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    enemies.get(enemies.size()-1).changeSize(2);
                    break;  
                case 5:
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() + Math.sin(getPlayer().bulletRads)*15 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() + Math.cos(getPlayer().bulletRads)*15 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads - Math.toRadians(10)),-1500*Math.sin(getPlayer().bulletRads - Math.toRadians(10)))), getPlayer().bulletRads));
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() + Math.sin(getPlayer().bulletRads)*15 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() + Math.cos(getPlayer().bulletRads)*15 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() - Math.sin(getPlayer().bulletRads)*15 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() - Math.cos(getPlayer().bulletRads)*15 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() - Math.sin(getPlayer().bulletRads)*15 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() - Math.cos(getPlayer().bulletRads)*15 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads + Math.toRadians(10)),-1500*Math.sin(getPlayer().bulletRads + Math.toRadians(10)))), getPlayer().bulletRads));
                    break;
                case 6:
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() + Math.sin(getPlayer().bulletRads)*15 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() + Math.cos(getPlayer().bulletRads)*15 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads - Math.toRadians(20)),-1500*Math.sin(getPlayer().bulletRads - Math.toRadians(20)))), getPlayer().bulletRads));
                    //enemies.get(enemies.size()-1).health=2;
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() + Math.sin(getPlayer().bulletRads)*15 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() + Math.cos(getPlayer().bulletRads)*15 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads - Math.toRadians(10)),-1500*Math.sin(getPlayer().bulletRads - Math.toRadians(10)))), getPlayer().bulletRads));
                    //enemies.get(enemies.size()-1).health=2;
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() + Math.sin(getPlayer().bulletRads)*15 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() + Math.cos(getPlayer().bulletRads)*15 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    //enemies.get(enemies.size()-1).health=2;
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    //enemies.get(enemies.size()-1).health=2;
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() - Math.sin(getPlayer().bulletRads)*15 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() - Math.cos(getPlayer().bulletRads)*15 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads),-1500*Math.sin(getPlayer().bulletRads))), getPlayer().bulletRads));
                    //enemies.get(enemies.size()-1).health=2;
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() - Math.sin(getPlayer().bulletRads)*15 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() - Math.cos(getPlayer().bulletRads)*15 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads + Math.toRadians(10)),-1500*Math.sin(getPlayer().bulletRads + Math.toRadians(10)))), getPlayer().bulletRads));
                    //enemies.get(enemies.size()-1).health=2;
                    enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() - Math.sin(getPlayer().bulletRads)*15 + Math.cos(getPlayer().bulletRads)*((Player)enemies.get(p1)).size, enemies.get(p1).getPos().getY() - Math.cos(getPlayer().bulletRads)*15 - Math.sin(getPlayer().bulletRads)*((Player)enemies.get(p1)).size), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads + Math.toRadians(20)),-1500*Math.sin(getPlayer().bulletRads + Math.toRadians(20)))), getPlayer().bulletRads));
                    //enemies.get(enemies.size()-1).health=2;
                    break;
                case 7:
                    for(int d = 0;d<360;d+=10){
                        enemies.add(new Bullet(new Vector2(enemies.get(p1).getPos().getX() + Math.cos(getPlayer().bulletRads + Math.toRadians(d))*((Player)enemies.get(p1)).size*(3*(d/360)), enemies.get(p1).getPos().getY() - Math.sin(getPlayer().bulletRads + Math.toRadians(d))*((Player)enemies.get(p1)).size * (3*(d/360))), enemies.get(p1).getPos().add(new Vector2(1500*Math.cos(getPlayer().bulletRads + Math.toRadians(d)),-1500*Math.sin(getPlayer().bulletRads + Math.toRadians(d)))), getPlayer().bulletRads + Math.toRadians(d)));
                        enemies.get(enemies.size()-1).col = Color.white;
                        enemies.get(enemies.size()-1).changeSize(10); 
                        enemies.get(enemies.size()-1).changeSpeed(1);
                        enemies.get(enemies.size()-1).health = 999999;
                    }
            }
            ((Player)enemies.get(p1)).bulletTimer = ((Player)enemies.get(p1)).bulletTimerMax;
        }
    }
    public void addExplosion(Vector2 pos, Color col, int size, int degreeFrom, int degreeTo, boolean GUI){
        for(double d = degreeFrom;d<degreeTo;d+=20*(1.0/particleEffects)){
            if(particleCount<particleMax){
            	enemies.add(new Particle(pos, new Vector2(Math.cos(Math.toRadians(d)),Math.sin(Math.toRadians(d))).x(size + (Math.random()*size - size/2)), col, pos.add(new Vector2(Math.cos(Math.toRadians(d + Math.random()*size*2 - size)),Math.sin(Math.toRadians(d + Math.random()*size*2 - size))).x(700 + Math.random()*size - size/2)), GUI, false));
            	particleCount++;
            }
        }
        if(degreeTo-degreeFrom>=360){
        	int xIndex = (int)(pos.getX()/gridSize)/2;
        	int yIndex = (int)(pos.getY()/gridSize)/2;
        	if(pos.getX()%gridSize > gridSize/2)
            	xIndex++;
        	if(pos.getY()%gridSize > gridSize/2)
        		yIndex++;
        	int size2 = 5;
        	for(int x=-size2;x<=size2;x++)
            	for(int y=-size2;y<=size2;y++){
                	Axis gridPoint = gridSpace[Math.max(1, Math.min(xIndex+x, gridSpace.length/2 - 1))][Math.max(1, Math.min(yIndex+y, gridSpace[0].length/2 - 1))];
                	gridPoint.addVel(new Vector2(Math.random(), Math.random()).x(15/Math.max(1, Math.sqrt(x*x + y*y))));
            	}
        }
    }
    public void addImplosion(Vector2 pos, Color col, int size, int degreeFrom, int degreeTo, boolean GUI){
        for(double d = degreeFrom;d<degreeTo;d+=40*(1.0/particleEffects)){
        	if(particleCount<particleMax){
        		enemies.add(new Particle(pos.add(new Vector2(Math.cos(Math.toRadians(d + Math.random()*size - size/2)),Math.sin(Math.toRadians(d + Math.random()*size - size/2))).x(700 + Math.random()*size - size/2)), new Vector2(Math.cos(Math.toRadians(d)),Math.sin(Math.toRadians(d))).x(size + (Math.random()*size - size/2)), col, pos, GUI, true));
        		particleCount++;
        	}
        }
        int xIndex = (int)(pos.getX()/gridSize)/2;
        int yIndex = (int)(pos.getY()/gridSize)/2;
        if(pos.getX()%gridSize > gridSize/2)
            xIndex++;
        if(pos.getY()%gridSize > gridSize/2)
            yIndex++;
        int size2 = 15;
        for(int x=-size2;x<=size2;x++)
            for(int y=-size2;y<=size2;y++){
            	if(Math.sqrt(x*x + y*y)<8&&xIndex+x>0&&xIndex+x<gridSpace.length/2&&yIndex+y>0&&yIndex+y<gridSpace.length/2){
            		Axis gridPoint = gridSpace[xIndex+x][yIndex+y];
                	//if(gridPoint.getPos().sub(gridPoint.OriginalPos).magnitude()<gridSize/2)
                	//gridPoint.addVel(gridPoint.getPos().sub(this.size.x(0.5)).normalize().x(-10));
                	gridPoint.setTarget(pos.x(0.5).add(new Vector2(x,y).x(-22*Math.cos(Math.toRadians(gridPoint.OriginalPos.sub(pos.x(0.5)).magnitude())))));
                	gridPoint.targetTimer = 1;
            	}
            }
    }
    /**
     * Checks to see if a bullet has hit an enemy
     */
    public void checkBullets(){
        for(int i=0;i<enemies.size();i++){
            if(enemies.get(i) instanceof Bullet){
                if(enemies.get(i).getTarget().sub(enemies.get(i).getPos()).magnitude()<50||enemies.get(i).exploding==true){
                    enemies.remove(i);
                }else{
                    for(int j=0;j<enemies.size();j++){
                        if(i<enemies.size())
                            if(enemies.get(i) instanceof Bullet && !(enemies.get(j) instanceof Letter || enemies.get(j) instanceof Particle || enemies.get(j) instanceof Bullet || enemies.get(j) instanceof Player || enemies.get(j) instanceof Axis) && enemies.get(j).exploding==false && enemies.get(i).getPos().sub(enemies.get(j).getPos()).magnitude()<enemies.get(i).size + enemies.get(j).size){   
                                boolean bomb = (enemies.get(i).col == Color.WHITE);
                                if(!(enemies.get(j).boss==true&&bomb&&getPlayer().lives>=0))
                                	enemies.get(j).health-=enemies.get(i).health;
                                if(enemies.get(j) instanceof Blackhole){
                                    ((Blackhole)enemies.get(j)).active = true;
                                    addExplosion(enemies.get(j).getPos(), enemies.get(j).col,20,0,360,false);
                                }
                                if(!bomb)
                                    enemies.get(i).explode();
                                if(j>=enemies.size())
                                    j = enemies.size() - 1;
                                if(enemies.get(j) instanceof Heart)
                                    enemies.get(j).changeSize(1.1);
                                if(enemies.get(j).health<=0){
                                    removeNeonObject(j, bomb);
                                    if(j<i)
                                    	i--;
                                }
                            }
                    }
                }
            }
            //Break
            if(i>=enemies.size())
                i = enemies.size() - 1;
        	if(enemies.get(i) instanceof Axis && ((Axis)enemies.get(i)).fluid == true){
        		Axis thisObj = ((Axis)enemies.get(i));
        		if(thisObj.startIndex!=null&&thisObj.endIndex!=null){
        			if(thisObj.vertical==true){
        				((Axis)enemies.get(i)).setStartorEnd(gridSpace[(int)thisObj.startIndex.getX()][(int)thisObj.startIndex.getY()].getPos(), true, false);
        				((Axis)enemies.get(i)).setStartorEnd(gridSpace[(int)thisObj.endIndex.getX()][(int)thisObj.endIndex.getY()].getPos().add(new Vector2(0,-gridSize)), false, false);
        			}else{
        				((Axis)enemies.get(i)).setStartorEnd(gridSpace[(int)thisObj.startIndex.getX()][(int)thisObj.startIndex.getY()].getPos(), true, false);
        				((Axis)enemies.get(i)).setStartorEnd(gridSpace[(int)thisObj.endIndex.getX()][(int)thisObj.endIndex.getY()].getPos().add(new Vector2(-gridSize,0)), false, false);
        			}
        		}
            }
        	
            if(!((enemies.get(i) instanceof Axis)||(enemies.get(i) instanceof Letter)||(enemies.get(i) instanceof Particle))){
                int xIndex = (int)(enemies.get(i).getPos().getX()/gridSize)/2;
                int yIndex = (int)(enemies.get(i).getPos().getY()/gridSize)/2;
                if(enemies.get(i).getPos().getX()%gridSize > gridSize/2)
                    xIndex++;
                if(enemies.get(i).getPos().getY()%gridSize > gridSize/2)
                    yIndex++;
                int size = (enemies.get(i) instanceof Bullet) ? 0 : 1;
                if(xIndex<gridSpace.length/2&&yIndex<gridSpace.length/2&&xIndex>0&&yIndex>0)
                	for(int x=-size;x<=size;x++)
                		for(int y=-size;y<=size;y++){
                			Axis compare = gridSpace[Math.max(1, Math.min(xIndex+x, gridSpace.length/2 - 1))][Math.max(1, Math.min(yIndex+y, gridSpace[0].length/2 - 1))];
                			if(compare.getPos().sub(compare.OriginalPos).magnitude()<gridSize/2)
                				gridSpace[Math.max(1, Math.min(xIndex+x, gridSpace.length/2 - 1))][Math.max(1, Math.min(yIndex+y, gridSpace[0].length/2 - 1))].addVel(enemies.get(i).getVel().x(0.1));
                		}
            }
            
            if(enemies.get(i) instanceof Blackhole&& enemies.get(i).opacity>NeonObject.growthThreshhold)
                if(((Blackhole)enemies.get(i)).active==true){
                    if(((Blackhole)enemies.get(i)).impTimer<0){
                        addImplosion(enemies.get(i).getPos(), enemies.get(i).col,20,0,360,false);
                        ((Blackhole)enemies.get(i)).impTimer = ((Blackhole)enemies.get(i)).impTimerMax;
                    }else{
                        ((Blackhole)enemies.get(i)).impTimer-=2;
                    }
                    for(int j=0;j<enemies.size();j++){
                        if(enemies.get(j).getPos().sub(enemies.get(i).getPos()).magnitude()<500&&!(enemies.get(j) instanceof Letter || enemies.get(j) instanceof Particle || enemies.get(j) instanceof Bullet || enemies.get(j) instanceof Axis))
                            if(enemies.get(j) instanceof Blackhole){
                                enemies.get(j).setPos(enemies.get(j).getPos().add(enemies.get(i).getPos().sub(enemies.get(j).getPos()).x(0)));
                            }else if(enemies.get(j) instanceof Player)
                                enemies.get(j).setPos(enemies.get(j).getPos().add(enemies.get(i).getPos().sub(enemies.get(j).getPos()).normalize().x(((Blackhole)enemies.get(i)).strength)));
                            else{
                                enemies.get(j).setPos(enemies.get(j).getPos().add(enemies.get(i).getPos().sub(enemies.get(j).getPos()).normalize().x(((Blackhole)enemies.get(i)).strength/2)));
                                if(enemies.get(j).getPos().sub(enemies.get(i).getPos()).magnitude()<50){
                                    enemies.get(i).health+=0.25;
                                    enemies.get(i).maxHealth+=0.25;
                                    ((Blackhole)enemies.get(i)).strength+=0.01;
                                	enemies.get(j).explode();
                                }
                            }
                    }
                }
            
            if(enemies.get(i).opacity<0.05){
                if(enemies.get(i) instanceof Letter || enemies.get(i) instanceof Particle){
                	if(enemies.get(i) instanceof Particle)
                		particleCount--;
                    enemies.remove(i);
                }else{
                    if((enemies.get(i) instanceof DiamondBox && enemies.get(i).size == 10) || !(enemies.get(i) instanceof DiamondBox)){
                        Vector2 pos;
                        if((int)(Math.random()*5)==1)
                            pos = new Vector2(getPlayer().getPos().getX(),getPlayer().getPos().getY());
                        else
                            pos = new Vector2(Math.random()*size.getX(), Math.random()*size.getY());
                        if(enemies.get(i).attached==false&&(int)(Math.random()*2)==1){
                            int choice = (int)(Math.random()*8);
                            if(choice==5)
                                addEnemy(choice-1, pos, getPlayer().getPos());
                            else
                                addEnemy(choice, pos, getPlayer().getPos());
                        }
                    }
                    if(enemies.get(i) instanceof Player){
                        addBullet(7, 0);
                        ((Player)enemies.get(i)).lives--;
                        ((Player)enemies.get(i)).multi=1;
                        refreshLives();
                        refreshMulti();
                        enemies.get(i).reincarnate();
                    }else{
                        if(enemies.get(i).scoreLocation!=null)
                            for(int k=0;k<enemies.get(i).scoreLocation.length;k++)
                                enemies.get(i).scoreLocation[k].explode();
                        if(enemies.get(i) instanceof Bully)
                            for(int j=0;j<((Bully)enemies.get(i)).diamondboxes.length;j++)
                                ((Bully)enemies.get(i)).diamondboxes[j].setTarget(getPlayer().getPos());
                        if(enemies.get(i) instanceof Blackhole)
                        	blackholeCount--;
                        bossPresent = (enemies.get(i) instanceof UltraSuperMegaRotator) ? false : bossPresent;
                        enemyCount--;
                        if(enemies.get(i) instanceof UltraSuperMegaRotator)
                        	bossPresent = false;
                        enemies.remove(i);
                    }
                }
            }else
                if(!((enemies.get(i) instanceof Bullet)||(enemies.get(i) instanceof Player)||(enemies.get(i) instanceof Axis)||(enemies.get(i) instanceof Letter)||(enemies.get(i) instanceof Particle)))
                    if(enemies.get(i).getPos().sub(getPlayer().getPos()).magnitude()<enemies.get(i).size+getPlayer().size&&getPlayer().opacity>NeonObject.growthThreshhold&&enemies.get(i).opacity>NeonObject.growthThreshhold)
                        getPlayer().explode();
        }
    }
    /**
     * Removes object from the grid, and creates an explosion if necessary
     */
    public void removeNeonObject(int index, boolean bomb){
        if(index < enemies.size() && enemies.get(index) instanceof Bullet){
            enemies.get(index).explode();
        }else if(index < enemies.size()){
            if(enemies.get(index) instanceof DiamondBox && enemies.get(index).size == 20){
                addEnemy(2, enemies.get(index).getPos(), getPlayer().getPos());
                enemies.get(enemies.size() - 1).changeSize(0.5);
            }
            if(bomb==false){
                int score = (int)(enemies.get(index).maxHealth*2.5*getPlayer().multi);
                if(enemies.get(index) instanceof Heart)
                    score = (int)((enemies.get(index).maxHealth/3)*2.5*getPlayer().multi);
                else if(enemies.get(index) instanceof Blackhole)
                    score = (int)((enemies.get(index).maxHealth*10)*2.5*getPlayer().multi);
                if((int)(getPlayer().score/newLife)<(int)((getPlayer().score+score)/newLife)){
                    getPlayer().lives+=score/newLife + 1;
                    addExplosion(new Vector2(windowSize.getX() - 105,42), Color.green,20,0,720, true);
                    refreshLives();
                }
                if((int)(getPlayer().score/newBomb)<(int)((getPlayer().score+score)/newBomb)){
                    getPlayer().bombs+=score/newBomb + 1;
                    addExplosion(new Vector2(windowSize.getX() - 105,72), Color.green,20,0,720, true);
                    refreshBombs();
                }
                if((int)(getPlayer().score/incMulti)<(int)((getPlayer().score+score)/incMulti)){
                    getPlayer().multi++;
                    addExplosion(new Vector2(400,55), Color.green,10,0,360, true);
                    refreshMulti();
                }
                getPlayer().score += score;
            
                refreshScore();
                
                if(enemies.get(index).maxHealth!=1&&!((enemies.get(index) instanceof Bullet)||(enemies.get(index) instanceof Player)||(enemies.get(index) instanceof Axis)||(enemies.get(index) instanceof Letter)||(enemies.get(index) instanceof Particle)))
                    enemies.get(index).scoreLocation = addString(score + "", enemies.get(index).getPos(),Color.green, 15, false);
            }
            if(!(enemies.get(index) instanceof Bullet))
                addExplosion(enemies.get(index).getPos().getValue(), enemies.get(index).col, 20, 0, 360, false);
            enemies.get(index).explode();
        }
    }
    public void refreshScore(){
        String scoreString = "";
        int length = (getPlayer().score + "").length();
        for(int n=length-1;n>=0;n--){
            scoreString += (getPlayer().score + "").substring(length-n-1, length-n);
            if(n%3==0&&n!=0)
                scoreString+=",";
        }
        
        for(int i=0;i<scoreLocs.length;i++)
            enemies.remove(scoreLocs[i]);
        
        scoreLocs = addString(scoreString, new Vector2(130,80), Color.green, 25, true);
    }
    public void refreshMulti(){
        String multiString = "x";
        int length = (getPlayer().multi + "").length();
        for(int n=length-1;n>=0;n--){
            multiString += (getPlayer().multi + "").substring(length-n-1, length-n);
            if(n%3==0&&n!=0)
                multiString+=",";
        }
        
        for(int i=0;i<multiLocs.length;i++)
            enemies.remove(multiLocs[i]);
        
        multiLocs = addString(multiString, new Vector2(400,50), Color.green, 25, true);
    }
    public void refreshLives(){
        if(getPlayer().lives<=livesLocs.length-1){
            for(int k = livesLocs.length - 1;k>=0;k--){
                if(k<livesLocs.length&&livesLocs[k]!=null){
                    enemies.remove(livesLocs[k]);
                }
                if(k<getPlayer().lives)
                    livesLocs[k] = addString("P",new Vector2(windowSize.getX() - 75 - (letterSpace+15)*k,42), Color.green, letterSpace, true)[0];
            }
        }else{
            for(int k=getPlayer().lives-1;k>=0;k--){
                if(k<livesLocs.length&&livesLocs[k]!=null){
                    enemies.remove(livesLocs[k]);
                    livesLocs[k] = null;
                }
            }
            
            NeonObject[] temp = addString(getPlayer().lives + "", new Vector2(windowSize.getX() - 65 - (letterSpace+15)*((getPlayer().lives + "").length()),40), Color.green, letterSpace + 5, true);
            for(int t=0;t<temp.length;t++)
                livesLocs[t+1] = temp[t];
            livesLocs[0] = addString("P", new Vector2(windowSize.getX() - 75,42), Color.green, letterSpace, true)[0];
        }
    }
    public void refreshBombs(){
        if(getPlayer().bombs<=bombsLocs.length - 1){
            for(int k = bombsLocs.length - 1;k>=0;k--){
                if(k<bombsLocs.length&&bombsLocs[k]!=null){
                    enemies.remove(bombsLocs[k]);
                }
                if(k<getPlayer().bombs)
                    bombsLocs[k] = addString("B",new Vector2(windowSize.getX() - 75 - (letterSpace+15)*k,60), Color.green, letterSpace, true)[0];
            }
        }else{
            for(int k=getPlayer().bombs-1;k>=0;k--){
                if(k<bombsLocs.length&&bombsLocs[k]!=null){
                    enemies.remove(bombsLocs[k]);
                    bombsLocs[k] = null;
                }
            }
            
            NeonObject[] temp = addString(getPlayer().bombs + "", new Vector2(windowSize.getX() - 65 - (letterSpace+15)*((getPlayer().bombs + "").length()),60), Color.green, letterSpace + 5, true);
            for(int t=0;t<temp.length;t++)
                bombsLocs[t+1] = temp[t];
            bombsLocs[0] = addString("B", new Vector2(windowSize.getX() - 75,62), Color.green, letterSpace, true)[0];
        }
    }
}