package com.san22.flappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.graphics.Color;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.management.timer.Timer;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture bottomtube;
	Texture toptube;
	Texture gameover;
	Texture start;
	Texture[] bird = new Texture[2];
	int flapstate = 0;
	BitmapFont font;
	int gamestate = 0;
	int birdY;
	int gap;
	int tubex[];
	int dist;
	int offset[];
	int score = 0;
	int scoringtube = 0;
	Circle birdcircle;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	@Override
	public void create() {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		bird[0] = new Texture("bird.png");
		bird[1] = new Texture("bird2.png");
		gameover=new Texture("Gameover.png");
//		birdY=Gdx.graphics.getHeight()/2-bird[flapstate].getHeight()/2;
		bottomtube = new Texture("bottomtube.png");
		toptube = new Texture("toptube.png");
		tubex = new int[4];
		birdcircle = new Circle();
		offset = new int[4];
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		start=new Texture("start.png");
		font.getData().setScale(10);
		topTubeRectangles = new Rectangle[4];
		bottomTubeRectangles = new Rectangle[4];
		dist = Gdx.graphics.getWidth() * 3 / 4;
		startgame();

		gap = (int) ((Math.random() * (500 - 160)) + 160);

	}

	void startgame() {
		birdY = Gdx.graphics.getHeight() / 2 - bird[flapstate].getHeight() / 2;
		for (int i = 0; i < 4; i++) {


				tubex[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 + (i+1)* (dist);

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();

		}

	}

	void mywait(int n) {
		long curr = System.currentTimeMillis();
		while (System.currentTimeMillis() < curr + n) {

		}
	}

	void changestate() {

		Runnable r = new Runnable() {
			@Override
			public void run() {
				mywait(75);
			}
		};


		if (flapstate == 0) {
			Gdx.app.postRunnable(r);
			flapstate = 1;
		} else {
			Gdx.app.postRunnable(r);
			flapstate = 0;
		}

	}

	void vmovement() {

		int velocity = 0;
		int gravity = 60;


		if (gamestate == 1) {
			birdY -= (velocity + gravity);
			if (Gdx.input.justTouched()) {
				velocity = -300;
				birdY -= (velocity + gravity);
				gamestate = 1;
			}
		} else {
			if (Gdx.input.justTouched())
				gamestate = 1;
		}


	}

	void checkhieght() {
		if (birdY < 0)
			birdY = 0;
	}


	@Override
	public void render() {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if (gamestate == 2) {

//			font.draw(batch,"GAME OVER",100,200);
			batch.draw(gameover,Gdx.graphics.getWidth()/2- gameover.getWidth()/2,Gdx.graphics.getHeight() *3/4);
			if (Gdx.input.justTouched())
			{
				startgame();
			     score=0;

				gamestate=0;

				batch.draw(bird[flapstate],Gdx.graphics.getWidth()/2,birdY);

			}

		}


		else if (gamestate == 1) {
			score++;

			for (int i = 0; i < 4; i++) {
				if (tubex[i] <= -toptube.getWidth()) {
					tubex[i] += 4 * dist;
					offset[i] = (int) ((Math.random() * (450 - 180)) + 180);
				} else
					tubex[i] -= 40;

				batch.draw(toptube, tubex[i], Gdx.graphics.getHeight() / 2 + gap + offset[i]);
				batch.draw(bottomtube, tubex[i], Gdx.graphics.getHeight() / 2 - gap - bottomtube.getHeight() + offset[i]);
				topTubeRectangles[i] = new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 + gap + offset[i], toptube.getWidth(), toptube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubex[i], Gdx.graphics.getHeight() / 2 - gap - bottomtube.getHeight() + offset[i], bottomtube.getWidth(), bottomtube.getHeight());
			}

			changestate();
			vmovement();
			checkhieght();

		} else if (gamestate == 0) {

			batch.draw(start,Gdx.graphics.getWidth()/2-start.getWidth()/4,Gdx.graphics.getHeight()/4,200,200);
			if (Gdx.input.justTouched())
				gamestate = 1;
		}
		font.draw(batch, String.valueOf(score), 100, 200);
		batch.draw(bird[flapstate], Gdx.graphics.getWidth() / 2 - bird[flapstate].getWidth() / 2, birdY);

		birdcircle.set(Gdx.graphics.getWidth() / 2, birdY + bird[flapstate].getHeight() / 2, bird[flapstate].getWidth() / 2);
		for (int i = 0; i < 4; i++) {
			if (Intersector.overlaps(birdcircle, topTubeRectangles[i]) || Intersector.overlaps(birdcircle, bottomTubeRectangles[i])) {
				gamestate = 2;

				Gdx.app.log("Collision", "DETECTED");
               birdY=0;
			}


		}
		batch.end();

          // ss
	}
}
