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
	private Convience conv;
	public void reset(){
		conv = new Convience(this);
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
		
		public char[] getRegs() {
		return regs;
	}

	public void setRegs(char[] regs) {
		this.regs = regs;
	}

	public char[] getMem() {
		return mem;
	}

	public void setMem(char[] mem) {
		this.mem = mem;
	}

	public char[] getFlags() {
		return flags;
	}

	public void setFlags(char[] flags) {
		this.flags = flags;
	}
	
	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}
	
	public int getSp() {
		return sp;
	}

	public void setSp(int sp) {
		this.sp = sp;
	}

		public void run(){
			MyGdxGame.str = Integer.toHexString(mem[pc]);
			MyGdxGame.pc = Integer.toHexString(pc);
			MyGdxGame.debug = Integer.toHexString(regs[Register.B.index]);
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
					conv.decReg(Register.B);
					pc++;
					break;
				}
				
				case 0x06:{
					conv.loadToReg(Register.B, (char) (pc + 1));
						cycles = 7;
						while(cycles > 0){
							cycles--;
						}
						pc++;
					break;
				}
				
				case 0x0D:{
					conv.incReg(Register.C);
					pc++;
					break;
				}
				
				case 0x11:{
					//endianness might be incorrect
					conv.loadToReg(Register.D, (char) (pc + 2));
					conv.loadToReg(Register.E, (char) (pc + 1));
					pc += 3;
					break;
				}
				
				case 0x13:{
						//endianness might be incorrect
						conv.incRegPair(Register.D, Register.E);
						pc++;
						break;
					}
				
				case 0x1a:{
					conv.regPairToReg(Register.D, Register.E, Register.A);
					pc++;
					break;
				}
				
				case 0x21:{
						//endianness might be incorrect
					conv.loadToReg(Register.H, (char) (pc + 2));
					conv.loadToReg(Register.L, (char) (pc + 1));
						pc += 3;
						break;
					}
				
				case 0x23:{
					conv.incRegPair(Register.H, Register.L);
					pc++;
					break;
				}
				
				case 0x31:{
					conv.memToSP((char)(pc + 2), (char)(pc + 1));
					pc += 3;
					break;
				}
				
				case 0x32:{
					conv.regToMem(Register.A, (char) ((mem[pc + 2] << 8) | (mem[pc + 1])));
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
					conv.incReg(Register.A);
					pc++;
					break;
				}
				
				case 0x3E:{
					conv.loadToReg(Register.A, (char) (pc + 1));
						cycles = 7;
						while(cycles > 0){
							cycles--;
						}
						pc++;
						break;
					}
				
				case 0x77:{
					conv.regToRegPair(Register.H, Register.L, Register.C);
					pc++;
					break;
				}
				
				case 0xc2:{
						//Gdx.app.log("Debug", Integer.toHexString((mem[pc + 2] << 8) | (mem[pc + 1])));
					boolean bool = conv.jumpIf(Flag.Zero, false, (char) (pc + 2), (char) (pc + 1));
						cycles = 10;
						while(cycles > 0){
							cycles--;
						}
						
						if(bool == false){
							pc += 3;
							run();
							MyGdxGame.halt = true;
						}
						break;
					}

					
					
				case 0xc3:{
//						Gdx.app.log("Debug", Integer.toHexString((mem[pc + 2] << 8) | (mem[pc + 1])));
					conv.jump((char)(pc + 2), (char)(pc + 1));	
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
					conv.jump((char)(sp + 1), (char)(sp));
					sp += 2;
					break;
					}
						
				case 0xcd:{
					//Fail emulation, FIX ASAP
					conv.call((char)(pc + 2), (char)(pc + 1));
				break;
				}
				
				case 0xd3:{
					conv.regToMem(Register.A, (char)(pc + 1));
					pc++;
					break;
				}
					
				case 0xe1:{
					//MyGdxGame.debug = Integer.toHexString((mem[sp] << 8) | (mem[sp + 1]));
					conv.SPtoRegPair(Register.H, Register.L);
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
