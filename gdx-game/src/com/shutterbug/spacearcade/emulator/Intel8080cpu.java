package com.shutterbug.spacearcade.emulator;
import java.io.*;
import com.badlogic.gdx.*;
import com.shutterbug.spacearcade.*;

public class Intel8080cpu
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
					pc++;
					cycles = 4;
					while(cycles > 0){
						cycles--;
					}
					break;
				}
				
				case 0x05:{
					regs[Register.B.index]--;
					if(regs[Register.B.index] == 0){
						flags[Flag.Zero.index] = 1;
					}
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
						mem[word] = regs[Register.B.index];
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
					pc++;
						break;
					}
					
					
				case 0xc3:{
						Gdx.app.log("Debug", Integer.toHexString((mem[pc + 2] << 8) | (mem[pc + 1])));
					pc = ((mem[pc + 2] << 8) | (mem[pc +1]));
						cycles = 10;
						while(cycles > 0){
							cycles--;
						}
						break;
						}
				
				case 0xc9:{
					//Possible erronous emulation.
					//debug
						MyGdxGame.str = Integer.toHexString(mem[pc]);
						MyGdxGame.pc = Integer.toHexString(pc);
						MyGdxGame.halt = true;
						MyGdxGame.debug = "Halted.";
						MyGdxGame.debug2 = Integer.toHexString((sp + 1 << 8) | (sp));
						//end debug
					pc = ((sp << 8) | (sp + 1));
					sp += 2;
					break;
					}
						
				case 0xcd:{
					//Fail emulation, FIX ASAP
							sp = pc;
							sp += 2;
							pc = ((mem[pc + 2] << 8) | (mem[pc + 1]));
				break;
				}
				
				case 0xd3:{
					mem[pc + 1] = regs[Register.A.index];
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
