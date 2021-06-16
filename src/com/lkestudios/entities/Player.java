package com.lkestudios.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.lkestudios.main.Game;
import com.lkestudios.world.Camera;
import com.lkestudios.world.World;

public class Player extends Entity{

	public boolean right, left;
	
	public static double life = 100;
	
	public static int currentCoins = 0;
	public static int maxCoins = 0;
	
	public int dir = 1;
	private double gravity = 0.3;
	public double vspd = 0;
	
	public boolean jump = false;
	
	public boolean isJumping = false;
	public int jumpHeight = 48;
	public int jumpFrames = 0;
	
	public boolean inercia = false;
	public int inerciaMax = 16;
	public int inerciaFrames = 0;
	
	private int framesAnimation = 0;
	private int maxFrames = 15;
	
	private int maxSprite = 2;
	private int curSprite = 0;
	
	public Player(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);

	
	}
	
	public void tick() {
		depth = 2;
		
		   if(World.isFree((int)x,(int)(y+1)) && vspd > 0) {
				y+=gravity;
				for(int i = 0; i < Game.entities.size(); i++) {
				Entity e = Game.entities.get(i);
				if(e instanceof Enemy) {
					if(Entity.isColidding(this, e)) {
						vspd = -3; //no caso, utilize o número que represente a altura do segundo pulo.
						((Enemy) e).vida--;
						if(((Enemy) e).vida == 0) {
							//Destruir inimigo!
							Game.entities.remove(i);
							break;
						}
					}
				}
				
			}
		   }
		
		vspd+=gravity;
		if(!World.isFree((int)x,(int)(y+1)) && jump){
			vspd = -5;
			jump = false;
			
		}
		
		if(!World.isFree((int)x,(int)(y+vspd))) {
			
			
			int signVsp = 0;
			if(vspd >= 0)
			{
				signVsp = 1;
			}else  {
				signVsp = -1;
			}
			while(World.isFree((int)x,(int)(y+signVsp))) {
				y = y+signVsp;
			}
			vspd = 0;
		}
		
		y = y + vspd;
		
		if(right && World.isFree((int)(x+speed), (int)y)) {
			//direita
			x+=speed;
			dir = 1;
		}
		else if(left && World.isFree((int)(x-speed), (int)y)){
			//esquerda
			x-=speed;
			dir = -1;
		}
		
		if(jump) {
			if(!World.isFree(this.getX(), this.getY() + 1)) {
				isJumping = true;
			}
			else {
				jump = false;
			}
		}
		
		if(isJumping) {
			if(World.isFree(this.getX(), this.getY() - 2)) {
				y-=2;
				jumpFrames+=2;
			/*	
				if(left) {
					x-=2;
					inerciaFrames+=1;
				}
				if(right) {
					x+=2;
					inerciaFrames+=1;
				}
			*/
				
				if(jumpFrames == jumpHeight) {
					isJumping = false;
					jump = false;
					jumpFrames = 0;		
				}
			/*	
				if(inerciaFrames == inerciaMax) {
					inercia = false;
					inerciaFrames = 0;
				}
			*/
			}
			else {
				isJumping = false;
				jump = false;
				jumpFrames = 0;
				//inercia = false;
				//inerciaFrames = 0;
			}
		}
		
		//DETECTAR DANO
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Enemy) {
				if(Entity.isColidding(this, e)) {
					if(Entity.rand.nextInt(100) < 5) {
						life--;	
					}
				}
			}
		}
		
		//Detectar colisao com a moeda
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Moeda) {
				if(Entity.isColidding(this, e)) {
					Game.entities.remove(i);
					currentCoins++;
					break;
				}
			}
		}
		
		
		Camera.x = Camera.clamp((int)x - Game.WIDTH/2, 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp((int)y - Game.HEIGHT/2, 0, World.HEIGHT * 16 - Game.HEIGHT);
	
	}
	
	public void render(Graphics g) {
		framesAnimation++;
		if(framesAnimation == maxFrames) {
			curSprite++;
			framesAnimation = 0;
			if(curSprite == maxSprite) {
				curSprite = 0;
			}
		}
		if(dir == 1) {
			sprite = Entity.PLAYER_SPRITE_RIGHT[curSprite];
		}
		else if(dir == -1){
			sprite = Entity.PLAYER_SPRITE_LEFT[curSprite];
		}
		super.render(g);
	}
}
	
