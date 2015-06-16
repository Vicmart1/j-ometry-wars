package src;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Runner extends JPanel{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    //static int boxWidth = screenSize.width;
    //static int boxHeight = screenSize.height;
    static int boxWidth = 1024;
    static int boxHeight = 600;
    static int updateRate = 60;
    
    private static Vector2 size = new Vector2(1500,1500);
    //private static Vector2 windowSize = new Vector2(boxWidth,boxHeight);
    private static Grid grid = new Grid(size, new Vector2(boxWidth, boxHeight));
    
    private static boolean[] keys = new boolean[150];
    
    private static double intensity = 1;
    
    private static int detailLevel = 0;
    private double blur = 0;
    
    private boolean debug = false;
    
    private static int frameskip = 0;
    private int currentFrame = frameskip;
    
    public static double startTime = 1;
    
    public Runner(){
    	ActionListener taskPerformer = new ActionListener() {
    		public void actionPerformed(ActionEvent evt) {
                //while(true){
                    Vector2 previousPlayerPos = grid.getPlayer().getPos().getValue();
                    
                    long start = System.nanoTime();
                    for(int i=0;i<grid.enemies.size();i++)
                        if(startTime<0)
                            grid.enemies.get(i).move();
                    long end = System.nanoTime();
                    if(debug==true){
                        System.out.print("Move enemies: " + (end-start) + " - ");
                    }
                    
                    start = System.nanoTime();
                    grid.checkBullets();
                    //grid.checkEnemies();
                    if(debug==true){
                        end = System.nanoTime();
                        System.out.print("Check enemies: " + (end-start) + " - ");
                    }
                    
                    start = System.nanoTime();
                    NeonObject player1 =  grid.getPlayer();
                    if(player1.opacity>NeonObject.growthThreshhold-0.1){
                        if(keys[65]==true){
                            if(player1.getPos().sub(player1.getTarget()).getX()<0)
                                player1.setTarget(new Vector2(player1.getPos().getX() - ((Player)player1).speed,player1.getTarget().getY()));
                            else
                                player1.setTarget(new Vector2(player1.getTarget().getX() - ((Player)player1).speed,player1.getTarget().getY()));
                        }
                        if(keys[68]==true){
                            if(player1.getPos().sub(player1.getTarget()).getX()>0)
                                player1.setTarget(new Vector2(player1.getPos().getX() + ((Player)player1).speed,player1.getTarget().getY()));
                            else
                                player1.setTarget(new Vector2(player1.getTarget().getX() + ((Player)player1).speed,player1.getTarget().getY()));
                        }
                        if(keys[83]==true){
                            if(player1.getPos().sub(player1.getTarget()).getY()>0)
                                player1.setTarget(new Vector2(player1.getTarget().getX(),player1.getPos().getY() + ((Player)player1).speed));
                            else
                                player1.setTarget(new Vector2(player1.getTarget().getX(),player1.getTarget().getY() + ((Player)player1).speed));
                        }
                        if(keys[87]==true){
                            if(player1.getPos().sub(player1.getTarget()).getY()<0)
                                player1.setTarget(new Vector2(player1.getTarget().getX(),player1.getPos().getY() - ((Player)player1).speed));
                            else
                                player1.setTarget(new Vector2(player1.getTarget().getX(),player1.getTarget().getY() - ((Player)player1).speed));
                        }

                        if(keys[32]==true){
                            if(grid.getPlayer().bombs>0&&grid.getPlayer().bombTimer<0){
                                grid.getPlayer().bombs--;
                                grid.refreshBombs();
                                grid.addBullet(7, 0);
                                grid.getPlayer().bombTimer = grid.getPlayer().bombTimerMax;
                            }
                            keys[32] = false;
                        }
                        grid.getPlayer().bombTimer-=1;
                        List<Double> bulletAngle = new ArrayList<Double>();
                        if(keys[38]==true)
                            bulletAngle.add(Math.PI/2);
                        if(keys[40]==true)
                            bulletAngle.add(-Math.PI/2);
                        if(keys[37]==true)
                            bulletAngle.add(Math.PI);
                        if(keys[39]==true)
                            bulletAngle.add(0.0);
                    
                        double sum = 0;
                        for(int s=0;s<bulletAngle.size();s++)
                            sum += bulletAngle.get(s);
                        sum /= bulletAngle.size();
                    
                        if(keys[37]==true&&keys[40]==true)
                            sum = -3*Math.PI/4;
                        if(bulletAngle.size() > 0)
                            grid.addBullet(grid.getPlayer().level, sum);
                    }
                    if(debug==true){
                        end = System.nanoTime();
                        System.out.print("Take key input: " + (end-start) + " - ");
                    }
                    
                    start = System.nanoTime();
                    if(grid.enemyCount<7&&grid.bossPresent==false){
                        intensity++;
                        int count = 0;
                        try{
                        	for(NeonObject e:grid.enemies){
                        		if((e instanceof Blackhole)||(e instanceof Bully)||(e instanceof Diamond)||(e instanceof DiamondBox)||(e instanceof Heart)||(e instanceof Hourglass)||(e instanceof MegaRotator)||(e instanceof Pinwheel)||(e instanceof Rotator)||(e instanceof SuperMegaRotator)||(e instanceof UltraSuperMegaRotator))
                        			count++;
                        		else if((e instanceof Letter)&&((Letter)e).GUI==false)
                        			grid.enemies.remove(e);
                        	}
                        }catch(java.util.ConcurrentModificationException er){}
                            
                        grid.enemyCount = count;
                        if((int)intensity==12)
                            intensity = 9;
                        addNewEnemies((int)(intensity/2));
                    } 

                    //System.out.println(grid.enemyCount + " - " + intensity);
                    
                    switch((int)intensity){
                        case 5:
                            grid.getPlayer().level = 3;
                            break;
                        case 7:
                            grid.getPlayer().level = 4;
                            break;
                        case 8:
                            grid.getPlayer().level = 5;
                            break;
                        case 9:
                            grid.getPlayer().level = 6;
                            break;
                    }
                    if(grid.getPlayer().burnerTimer<0&&grid.getPlayer().opacity>NeonObject.growthThreshhold&&grid.getPlayer().getPos().sub(previousPlayerPos).magnitude()>1){
                        grid.getPlayer().burnerTimer = grid.getPlayer().burnerTimerMax;
                        //Remove for huge speed improvements 
                        //grid.addExplosion(grid.getPlayer().getPos(), Color.ORANGE, 20, (int)Math.toDegrees(-grid.getPlayer().animate - Math.PI/2)-200, (int)Math.toDegrees(-grid.getPlayer().animate - Math.PI/2)-160, false);
                    }
                    grid.getPlayer().burnerTimer-=2;
                    end = System.nanoTime();
                    if(debug==true){
                        System.out.print("Add enemies, burner particles: " + (end-start) + "  ");
                        System.out.println();
                    }

                    if(keys[10]==true&&grid.getPlayer().lives<0){
                        keys[10] = false;
                        grid.getPlayer().lives=3;
                        grid.getPlayer().bombs=3;
                        intensity = 1;
                        grid.getPlayer().setTarget(size.x(0.5));
                        grid.getPlayer().level = 2;
                        grid.getPlayer().score = 0;
                        grid.getPlayer().multi = 1;
                        grid.refreshLives();
                        grid.refreshMulti();
                        grid.refreshScore();
                        grid.refreshBombs();
                        grid.enemyCount = 0;
                    }
                    
                    if(currentFrame==0){
                        repaint();
                        currentFrame = frameskip;
                    }else
                        currentFrame--;
                    
                    if(startTime>=0)
                        startTime-=0.01;
                    
                    /**try {
                        Thread.sleep(1000/updateRate);
                    } catch (InterruptedException e) { }**/
                //}
            }
        };
        new Timer(1000/updateRate, taskPerformer).start();
        //main.start();
    }
    
    public static void main(String[] args){
    	String number = JOptionPane.showInputDialog("Created by Vicmart Coding Studios \nCopyright 2014, All Rights Reserved\n \nEnter 1 for better performance or 2 for better graphics:","");
		if(Integer.parseInt(number) == 1)
			updateRate = 30;
		
        JFrame frame = new JFrame("J-ometry Wars");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(boxWidth,boxHeight);
        frame.setContentPane(new Runner());
        frame.setVisible(true);
        frame.setResizable(false);
        frame.addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent arg0) { 
                //arg0.getKeyCode() Left=37;down=40;right=39;up=38
                //arg0.getKeyCode() Left=65;down=83;right=68;up=87
                keys[arg0.getKeyCode()] = true;
                //System.out.println(arg0.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent arg0) { 
                keys[arg0.getKeyCode()] = false;
            }

            @Override
            public void keyTyped(KeyEvent arg0) { }
        });
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,boxWidth,boxHeight);
        
        List<NeonObject> GUIobj = new ArrayList<NeonObject>();
        try{
            for(NeonObject e:grid.enemies){
                g.setColor(e.col);
                if(e.GUI==false){
                    Polygon shape = e.getShapeToDraw();
                    for(int i=0;i<shape.xpoints.length;i++)
                        for(double j=-detailLevel;j<=detailLevel;j+=1){
                            float alpha = (float)(Math.cos(j*(Math.PI/6))*e.opacity);
                            Color col = e.col;
                            g.setColor(new Color((float)col.getRed()/255,(float)col.getGreen()/255,(float)col.getBlue()/255, Math.max(Math.min(alpha,1), 0)));
                            if(i<shape.xpoints.length-1){
                                //if(new Vector2(shape.xpoints[i], shape.ypoints[i]).sub(grid.getPlayer().cam.pos.add(windowSize.x(0.5))).magnitude()<800&&new Vector2(shape.xpoints[i+1], shape.ypoints[i+1]).sub(grid.getPlayer().cam.pos.add(windowSize.x(0.5))).magnitude()<800){
                                    Vector2 perpen;
                                    if(e instanceof Letter)
                                        perpen = new Vector2(shape.xpoints[i+1] - shape.xpoints[i], shape.ypoints[i+1] - shape.ypoints[i]).perpendicular().normalize().x(j/2.0);
                                    else
                                        perpen = new Vector2(shape.xpoints[i+1] - shape.xpoints[i], shape.ypoints[i+1] - shape.ypoints[i]).perpendicular().normalize().x(j*blur);
                                    g.drawLine((int)(shape.xpoints[i] + perpen.getX() - grid.getPlayer().cam.pos.getX()), (int)(shape.ypoints[i] + perpen.getY() - grid.getPlayer().cam.pos.getY()), (int)(shape.xpoints[i+1] + perpen.getX() - grid.getPlayer().cam.pos.getX()), (int)(shape.ypoints[i+1] + perpen.getY() - grid.getPlayer().cam.pos.getY()));
                                //}
                            }else{
                                //if(new Vector2(shape.xpoints[i], shape.ypoints[i]).sub(grid.getPlayer().cam.pos.add(windowSize.x(0.5))).magnitude()<800&&new Vector2(shape.xpoints[0], shape.ypoints[0]).sub(grid.getPlayer().cam.pos.add(windowSize.x(0.5))).magnitude()<800){
                                    Vector2 perpen;
                                    if(e instanceof Letter)
                                        perpen = new Vector2(shape.xpoints[0] - shape.xpoints[i], shape.ypoints[0] - shape.ypoints[i]).perpendicular().normalize().x(j/2.0);
                                    else
                                        perpen = new Vector2(shape.xpoints[0] - shape.xpoints[i], shape.ypoints[0] - shape.ypoints[i]).perpendicular().normalize().x(j*blur);
                                    g.drawLine((int)(shape.xpoints[i] + perpen.getX() - grid.getPlayer().cam.pos.getX()), (int)(shape.ypoints[i] + perpen.getY() - grid.getPlayer().cam.pos.getY()), (int)(shape.xpoints[0] + perpen.getX() - grid.getPlayer().cam.pos.getX()), (int)(shape.ypoints[0] + perpen.getY() - grid.getPlayer().cam.pos.getY()));
                                //}
                            }
                        }
                }else
                    GUIobj.add(e);
            }
            for(NeonObject e:GUIobj){
                Polygon shape = e.getShapeToDraw();
                for(int i=0;i<shape.xpoints.length;i++)
                    for(double j=-detailLevel;j<=detailLevel;j+=1){
                        float alpha = (float)(Math.cos(j*(Math.PI/6))*e.opacity);
                        Color col = e.col;
                        g.setColor(new Color((float)col.getRed()/255,(float)col.getGreen()/255,(float)col.getBlue()/255, Math.max(Math.min(alpha,1), 0)));
                        if(i<shape.xpoints.length-1){
                            Vector2 perpen = new Vector2(shape.xpoints[i+1] - shape.xpoints[i], shape.ypoints[i+1] - shape.ypoints[i]).perpendicular().normalize().x(j/2.0);
                            g.drawLine((int)(shape.xpoints[i] + perpen.getX()), (int)(shape.ypoints[i] + perpen.getY()), (int)(shape.xpoints[i+1] + perpen.getX()), (int)(shape.ypoints[i+1] + perpen.getY()));
                        }else{
                            Vector2 perpen = new Vector2(shape.xpoints[0] - shape.xpoints[i], shape.ypoints[0] - shape.ypoints[i]).perpendicular().normalize().x(j/2.0);
                            g.drawLine((int)(shape.xpoints[i] + perpen.getX()), (int)(shape.ypoints[i] + perpen.getY()), (int)(shape.xpoints[0] + perpen.getX()), (int)(shape.ypoints[0] + perpen.getY()));
                        }
                    }
            }
                
                
        }catch(java.util.ConcurrentModificationException e){ }
        
        if(grid.getPlayer().lives<0){
            g.setFont(g.getFont().deriveFont(75.0f));
            g.setColor(Color.GREEN);
            g.drawString("Game Over", boxWidth/2 - 200, boxHeight/2 - 50);
            g.setFont(g.getFont().deriveFont(45.0f));
            g.drawString("Final score:" + grid.getPlayer().score, boxWidth/2 - 180, boxHeight/2 + 25);
            g.setFont(g.getFont().deriveFont(25.0f));
            g.drawString("Press [Enter] to continue", boxWidth/2 - 150, boxHeight/2 + 50);
        }
        if(startTime>=0){
            g.setColor(new Color((float)0.0,(float)0.0,(float)0.0,(float)startTime));
            g.fillRect(0, 0, boxWidth, boxHeight);
        }
            
    }
    public static void addNewEnemies(int num){
        if(intensity!=11)
            for(int i=0;i<num;i++){
                grid.addEnemy(0,new Vector2(Math.random()*size.getX(),Math.random()*size.getY()),grid.enemies.get(grid.p1).getPos());
                grid.addEnemy(1,new Vector2(Math.random()*size.getX(),Math.random()*size.getY()),new Vector2(Math.random()*size.getX(),Math.random()*size.getY()));
                grid.addEnemy(2,new Vector2(Math.random()*size.getX(),Math.random()*size.getY()),grid.enemies.get(grid.p1).getPos());
                grid.addEnemy(3,new Vector2(Math.random()*size.getX(),Math.random()*size.getY()),grid.enemies.get(grid.p1).getPos());
                grid.addEnemy(4,new Vector2(Math.random()*size.getX(),Math.random()*size.getY()),new Vector2(Math.random()*size.getX(),Math.random()*size.getY()));
                grid.addEnemy(5,new Vector2(Math.random()*size.getX(),Math.random()*size.getY()),new Vector2(Math.random()*size.getX(),Math.random()*size.getY()));
                grid.addEnemy(6,new Vector2(Math.random()*size.getX(),Math.random()*size.getY()),new Vector2(Math.random()*size.getX(),Math.random()*size.getY()));
                grid.addEnemy(7,new Vector2(Math.random()*size.getX(),Math.random()*size.getY()),new Vector2(Math.random()*size.getX(),Math.random()*size.getY()));
            }
        else
			grid.addEnemy(10,new Vector2(size.getX()/4,size.getY()/4),new Vector2(Math.random()*size.getX(),Math.random()*size.getY()));
	}
}
