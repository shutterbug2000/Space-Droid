package com.shutterbug.spacearcade;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.*;
import com.shutterbug.spacearcade.emulator.*;

import java.util.BitSet;

public class MyGdxGame implements ApplicationListener
{
	
	public OrthographicCamera camera;
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
	
	int i = 0x2400;
	int b = 1;
	private Texture b2;
	private Texture w2;
	private int bit = 0;
	private Texture w3;
	private byte[] dispArray = new byte[0x3fff - 0x2400];
	private BitSet bits;
	
	@Override
	public void create()
	{
//		texture = new Texture(Gdx.files.internal("android.jpg"));
		batch = new SpriteBatch();
		space8080.reset();
		font = new BitmapFont();
		font.scale(5);
		halt = false;
		
		Pixmap pixmapb = new Pixmap( 10,10, Format.RGBA8888);
		pixmapb.setColor( 0, 0, 0, .0000000000000000001f );
		pixmapb.fillRectangle(1,1,10,10);
		b2 = new Texture( pixmapb );
		pixmapb.dispose();
		Pixmap pixmapw = new Pixmap( 10,10, Format.RGBA8888);
		pixmapw.setColor( 1, 0, 1, 1f );
		pixmapw.fillRectangle(1,1,10,10);
		w2= new Texture( pixmapw );
		pixmapw.dispose();
/*		Pixmap pixmapw2 = new Pixmap( 10,10, Format.RGBA8888);
		pixmapw2.setColor( 1, 0, 1, 1f );
		pixmapw2.fillRectangle(1,1,256,224);
		w3= new Texture( pixmapw2 );
		pixmapw2.dispose();
		batch.begin();
		batch.draw(w3, (1 * 256), (1 * 224), 256, 224);
		batch.end();*/
		
		for(b = 0; b <= 0x3f00 - 0x2400 - 1; b++)
		{
		    dispArray[b] = (byte) 0xff;
		}
		
		bits = BitSet.valueOf(dispArray); 
		
		camera = new OrthographicCamera(256, 224);
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
//	    Gdx.gl.glClearColor(1, 1, 1, 1);
//	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void render()
	{        
		//batch.begin();
		//batch.draw(texture, Gdx.graphics.getWidth() / 4, 0, 
		//		   Gdx.graphics.getWidth() / 2, Gdx.graphics.getWidth() / 2);
		//batch.end();
		if(!halt || Gdx.input.isKeyPressed(Keys.SPACE)){
		MyGdxGame.halt = false;
//		space8080.run();
		}
		//batch.begin();
		/*font.setColor(0.0f, 0.0f, 1.0f, 1.0f);
//		font.scale(2);
		font.draw(batch, "Op: " + str, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		font.draw(batch, "PC: " + pc, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/8);
		font.draw(batch, debug, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/4);
		font.draw(batch, debug2, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/6);
		font.draw(batch, debug3, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
		font.draw(batch, "SP" + Integer.toHexString(space8080.getMem()[space8080.getSp()]), 0, Gdx.graphics.getHeight()/2);*/
				int j = i & b;
		
				
				
		/*		System.out.println("Address: " + i + " Bit: " + b + "=" + (space8080.getMem()[j] >= 1 ));
			
			
			
		if(i == 0x3fff & b > 0x80){
			i = 0x2400;
			b = 1;
		}else if(b > 0x80){
			i++;
			b = 1;
		}else{
			b = b << 1;
		}*/
				
			/*boolean[] disparray = new boolean[0xF400];
			
			disparray[b + bit] = ((space8080.getMem()[b] & (1 << bit)) != 0);
			if(b == 0x3fff & bit > 0x80){
				b = 0x2400;
				bit = 1;
			}else if(bit < 0x80){
				b++;
				bit = 1;
			}else{
				bit = bit << 1;
			}*/
				
			
			byte[] disparray = new byte[0xE001];
			
			int currentPos = 0;
			
			for(int vramPos = 0; vramPos < 0x4000 - 0x2400; vramPos++){
				for (int b = 0; b<8;b++){
					if((((space8080.getMem()[0x2400 + vramPos] >> b)&1)) == 0){
						disparray[currentPos] = (byte) 0x00;
						currentPos++;
					} else {
						disparray[currentPos] = (byte) 0xff;
						currentPos++;
					}
				}
			}
			
			for(int k = 0; k < 0xE000; k++){
				if(disparray[k] == 0){
					int x = (k % 256);
					int y = (int)Math.floor(k / 224);
					
					//System.out.println("x: " + x);
					//System.out.println("y: " + y);
					
					batch.begin();
					batch.draw(b2, (x), (y), 10, 10);
					batch.end();
				
				} else{
					int x = (k % 256);
					int y = (int)Math.floor(k / 224);
					batch.begin();
					batch.draw(w2, (x), (y), 10, 10);
					batch.end();
				}
			}
				
/*		for(int k = 0x0; k < 0xdff0; k++) {
			System.out.println(bits.get(k));
		if(bits.get(k)){
			int x = (k % 256);
			int y = (int)Math.floor(k / 224);
			
			System.out.println(x);
			System.out.println(y);
			
			batch.begin();
			batch.draw(b2, (x), (y), 10, 10);
			batch.end();
		
		} else{
			int x = (k % 256);
			int y = (int)Math.floor(k / 224);
			batch.begin();
			batch.draw(w2, (x), (y), 10, 10);
			batch.end();
		}
		}*/
				
		
	
		//font.draw(batch, opsran, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
		//batch.end();
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
