package com.lkestudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.lkestudios.world.World;

public class Enemy extends Entity{

	private int framesAnimation = 0;
	private int maxFrames = 15;
	
	private int maxSprite = 2;
	private int curSprite = 0;
	
	public boolean right = true, left = false;
	public int lastdir = 1;
	
	public int vida = 3;
	
	public Enemy(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
	
	}

	public void tick() {
		if(World.isFree((int)x,(int) (y+1))) {
			//cair (gravidade)
			y+=1;
		}
		
		if(right) {
			if(World.isFree((int)(x+speed), (int)y)) {
				x+=speed;
				if(World.isFree((int)(x+16), (int)y+1)) {
					right = false;
					left = true;
				}
			}
			else {
				right = false;
				left = true;
			}
		}
		
		if(left) {
			if(World.isFree((int)(x-speed), (int)y)) {
				x-=speed;
				if(World.isFree((int)(x-16), (int)y+1)) {
					right = true;
					left = false;
				}
			}
			else {
				right = true;
				left = false;
			}
		}
		
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
		if(lastdir == 1) {
			sprite = Entity.ENEMY1[curSprite];
		}
		else if(lastdir == -1){
			sprite = Entity.ENEMY1[curSprite];
		}
		super.render(g);
	}
}

