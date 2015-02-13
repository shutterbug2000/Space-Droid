package com.shutterbug.spacearcade.emulator;
import java.io.*;
import com.badlogic.gdx.*;

public class Intel8080cpu
{
	private char[] mem;
	private int pc;
	private int i;
	public void reset(){
		mem = new char[0x4000];
		pc = 0;
		DataInputStream input = null;
		try {
			input = new DataInputStream(new FileInputStream(new File("/sdcard/inv.h")));

			int offset = 0;
			for(i = 0; i < 0x7ff; i++){
				mem[i] = (char)(input.readByte() & 0xFF);
				offset++;
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} finally {
			if(input != null) {
				try { input.close(); } catch (IOException ex) {}
			}
		}
		
		try {
			input = new DataInputStream(new FileInputStream(new File("/sdcard/inv.g")));

			int offset = 0;
			for(i = 0x7FF; i < (0x7ff * 2); i++){
				mem[i] = (char)(input.readByte() & 0xFF);
				offset++;
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} finally {
			if(input != null) {
				try { input.close(); } catch (IOException ex) {}
			}
		}
		
		try {
			input = new DataInputStream(new FileInputStream(new File("/sdcard/inv.f")));

			int offset = 0;
			for(i = (0x7ff * 2); i < (0x7ff * 3); i++){
				mem[i] = (char)(input.readByte() & 0xFF);
				offset++;
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} finally {
			if(input != null) {
				try { input.close(); } catch (IOException ex) {}
			}
		}
		
		try {
			input = new DataInputStream(new FileInputStream(new File("/sdcard/inv.e")));

			int offset = 0;
			for(i = (0x7ff * 3); i < (0x7ff * 4); i++){
				mem[i] = (char)(input.readByte() & 0xFF);
				offset++;
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} finally {
			if(input != null) {
				try { input.close(); } catch (IOException ex) {}
			}
		}
		}
		
		public void run(){
			switch(mem[pc]){
				case 0x00:{
					//do nothing at all
					break;
				}
				
				default:
				{
					Gdx.app.log("Unknown opcode:", Integer.toHexString(mem[pc]));
				System.exit(0);
					}
			}
		}
		
}
