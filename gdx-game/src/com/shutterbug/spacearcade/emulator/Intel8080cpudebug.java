package com.shutterbug.spacearcade.emulator;
import java.io.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.Bits;
import com.shutterbug.spacearcade.*;

public class Intel8080cpudebug
{
	private char regs[];
	private char[] mem;
	private char[] flags;
	private int sp;
	private int pc;
	private int i;
	private int cycles;
	public void reset(){
		regs = new char[7];
		flags = new char[7];
		mem = new char[0x4000];
		pc = 0;
		sp = 0;
		DataInputStream input = null;
		try {
			input = new DataInputStream(new FileInputStream(new File("C:/sdcard/inv.h")));

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
			input = new DataInputStream(new FileInputStream(new File("C:/sdcard/inv.g")));

			int offset = 0;
			for(i = 0x800; i < 0xfff; i++){
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
			input = new DataInputStream(new FileInputStream(new File("C:/sdcard/inv.f")));

			int offset = 0;
			for(i = 0x1000; i < 0x17FF; i++){
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
			input = new DataInputStream(new FileInputStream(new File("C:/sdcard/inv.e")));

			int offset = 0;
			for(i = 0x1800; i < 0x1fff; i++){
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
			MyGdxGame.str = Integer.toHexString(mem[pc]);
			MyGdxGame.pc = Integer.toHexString(pc);
			switch(mem[pc]){
				case 0x00:{
					//do nothing at all
					pc++;
					cycles = 4;
					while(cycles > 0){
						cycles--;
					}
					break;
				}
				
				case 0x05:{
					regs[Register.B.index] = (char) ((regs[Register.B.index] & 0xFF) - 1);
					
					//Z flag check
					if((regs[Register.B.index] & 0xFF) == 0){
						flags[Flag.Zero.index] = 1;
					}else{
						flags[Flag.Zero.index] = 0;
					}
					
					//S flag check
					if((regs[Register.B.index] & 0xFF) < 0){
						flags[Flag.Sign.index] = 1;
					}else{
						flags[Flag.Sign.index] = 0;
					}
					
					//P flag check
					int s = (int)(regs[Register.B.index] & 0xFF);
					
					int bitCount = 0;
					for ( int i = 0; i < 8; i++, s >>>= 1 )
		            {
		            if ( ( s & 1 ) != 0 )
		                {
		                bitCount++;
		                }
		            }
					
					if(bitCount % 2 == 0){
						flags[Flag.Parity.index] = 1;
					}else{
						flags[Flag.Parity.index] = 0;
					}
					
					//TODO: MAJOR: Figure out how to emulator AC flag
					pc++;
					break;
				}
				
				case 0x06:{
					regs[Register.B.index] = (mem[pc + 1]);
						cycles = 7;
						while(cycles > 0){
							cycles--;
						}
						pc++;
					break;
				}
				
				case 0x0D:{
					regs[Register.C.index]--;
					pc++;
					break;
				}
				
				case 0x11:{
					//endianness might be incorrect
					regs[Register.D.index] = mem[pc + 2];
					regs[Register.E.index] = mem[pc + 1];
					pc += 3;
					break;
				}
				
				case 0x13:{
						//endianness might be incorrect
						regs[Register.D.index]++;
						regs[Register.E.index]++;
						pc++;
						break;
					}
				
				case 0x21:{
						//endianness might be incorrect
						regs[Register.H.index] = mem[pc + 2];
						regs[Register.L.index] = mem[pc + 1];
						pc += 3;
						break;
					}
				
				case 0x32:{
						int word = (mem[pc + 2] << 8) | (mem[pc + 1]);
						mem[word] = regs[Register.A.index];
						//pc++;
						//start debug for pc check
						//MyGdxGame.halt = true;
						//MyGdxGame.debug = Integer.toHexString(pc);
						//MyGdxGame.debug2 = Integer.toHexString(word);
						//end
						
						//Increment over ALL of opcode
						pc += 3;
						break;
					}
				
				case 0x3C:{
					regs[Register.A.index]++;
					pc++;
					break;
				}
				
				case 0x3E:{
						regs[Register.A.index] = (mem[pc + 1]);
						cycles = 7;
						while(cycles > 0){
							cycles--;
						}
						pc++;
						break;
					}
				
				case 0xc2:{
					if(flags[Flag.Zero.index] == 1){
						Gdx.app.log("Debug", Integer.toHexString((mem[pc + 2] << 8) | (mem[pc + 1])));
						pc = ((mem[pc + 2] << 8) | (mem[pc +1]));
						cycles = 10;
						while(cycles > 0){
							cycles--;
						}
					}
					pc += 3;
						break;
					}
					
					
				case 0xc3:{
//						Gdx.app.log("Debug", Integer.toHexString((mem[pc + 2] << 8) | (mem[pc + 1])));
					pc = ((mem[pc + 2] << 8) | (mem[pc +1]));
						cycles = 10;
						while(cycles > 0){
							cycles--;
						}
						break;
						}
				
				case 0xc9:{
					//Possible erronous emulation. *should be fixed*
					/*debug
						MyGdxGame.str = Integer.toHexString(mem[pc]);
						MyGdxGame.pc = Integer.toHexString(pc);
						MyGdxGame.halt = true;
						MyGdxGame.debug = "Halted.";
						MyGdxGame.debug2 = Integer.toHexString((sp + 1 << 8) | (sp));
						end debug */
					//MyGdxGame.debug = Integer.toHexString((mem[sp] << 8) | (mem[sp + 1]));
					pc = (mem[sp + 1] << 8) | (mem[sp]);
					sp += 2;
					break;
					}
						
				case 0xcd:{
					//Fail emulation, FIX ASAP
							mem[sp] = (char) pc;
							sp += 2;
							pc = ((mem[pc + 2] << 8) | (mem[pc + 1]));
				break;
				}
				
				case 0xd3:{
					mem[pc + 1] = regs[Register.A.index];
					pc++;
					break;
				}
					
				case 0xe1:{
					//MyGdxGame.debug = Integer.toHexString((mem[sp] << 8) | (mem[sp + 1]));
					regs[Register.L.index] = mem[sp];
					regs[Register.H.index] = mem[sp + 1];
					pc++;
					break;
				}
				
				default:
				{
				//	Gdx.app.log("Unknown opcode:", Integer.toHexString(mem[pc]));
				MyGdxGame.str = Integer.toHexString(mem[pc]);
				MyGdxGame.pc = Integer.toHexString(pc);
				MyGdxGame.halt = true;
				MyGdxGame.debug = "Halted.";
				//System.exit(0);
					}
			}
			}
			
}
