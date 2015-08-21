package com.shutterbug.spacearcade;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.shutterbug.spacearcade.emulator.*;

public class MyGdxGame implements ApplicationListener
{
	
	Texture texture;
	SpriteBatch batch;
	Intel8080cpudebug space8080 = new Intel8080cpudebug();
	public static CharSequence str = "";
	public static CharSequence debug = "";

	private BitmapFont font;
	public static CharSequence opsran = "";
	public static CharSequence debug3 = "";
	public static boolean halt;

	public static CharSequence debug2 = "";

	public static CharSequence pc = "";
	
	@Override
	public void create()
	{
//		texture = new Texture(Gdx.files.internal("android.jpg"));
		batch = new SpriteBatch();
		space8080.reset();
		font = new BitmapFont();
		font.scale(5);
		halt = false;
	}

	@Override
	public void render()
	{        
	    Gdx.gl.glClearColor(1, 1, 1, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.begin();
		//batch.draw(texture, Gdx.graphics.getWidth() / 4, 0, 
		//		   Gdx.graphics.getWidth() / 2, Gdx.graphics.getWidth() / 2);
		//batch.end();
		if(!halt || Gdx.input.isKeyPressed(Keys.SPACE)){
		MyGdxGame.halt = false;
		space8080.run();
		}
		batch.begin();
		font.setColor(0.0f, 0.0f, 1.0f, 1.0f);
//		font.scale(2);
		font.draw(batch, "Op: " + str, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		font.draw(batch, "PC: " + pc, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/8);
		font.draw(batch, debug, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/4);
		font.draw(batch, debug2, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/6);
		font.draw(batch, debug3, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
		font.draw(batch, opsran, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
		batch.end();
		}

	@Override
	public void dispose()
	{
	}

	@Override
	public void resize(int width, int height)
	{
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}
}
