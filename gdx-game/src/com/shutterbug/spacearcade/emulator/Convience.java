package com.shutterbug.spacearcade.emulator;

public class Convience {

	//TODO: HUGE: Make a collection of commonly used code.
	
	private Intel8080cpudebug cpu;

	public Convience(Intel8080cpu cpu) {
//		this.cpu = cpu;
	}
	
	public Convience(Intel8080cpudebug cpu) {
		this.cpu = cpu;
	}

	public void incRegPair(Register r1, Register r2){
		char[] regs = cpu.getRegs();
		int setPair = (regs[r1.index] << 8 | regs[r2.index]);
		setPair++;
		regs[r1.index] = (char) (setPair & 0xFF00);
		regs[r2.index] = (char) (setPair & 0x00FF);
		cpu.setRegs(regs);
	}
	
	public void decRegPair(Register r1, Register r2){
		char[] regs = cpu.getRegs();
		int setPair = (regs[r1.index] << 8 | regs[r2.index]);
		setPair--;
		regs[r1.index] = (char) (setPair & 0xFF00);
		regs[r2.index] = (char) (setPair & 0x00FF);
		cpu.setRegs(regs);
	}
	
	public void checkZ(Register r){
		char[] flags = cpu.getFlags();
		if((cpu.getRegs()[r.index] & 0x00FF) == 0){
			flags[Flag.Zero.index] = 1;
		}else{
			flags[Flag.Zero.index] = 0;
		}
		cpu.setFlags(flags);
	}
	
	public void checkS(Register r){
		char[] flags = cpu.getFlags();
		if((cpu.getRegs()[r.index] & 0x00FF) < 0){
			flags[Flag.Sign.index] = 1;
		}else{
			flags[Flag.Sign.index] = 0;
		}
		cpu.setFlags(flags);
	}
	
	public void checkP(Register r){
	
		char flags[] = cpu.getFlags();
		
	int s = (int)(cpu.getRegs()[r.index] & 0x00FF);
	
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
	}
	
	public void loadToReg(Register r, char addr){
		char[] regs = cpu.getRegs();
		regs[r.index] = (cpu.getMem()[addr]);
		cpu.setRegs(regs);
	}
	
	public void incReg(Register r1){
		char[] regs = cpu.getRegs();
		regs[r1.index]++;
		checkZ(r1);
		checkS(r1);
		checkP(r1);
		//TODO: MAJOR: Figure out how to emulate AC flag
		cpu.setRegs(regs);
	}
	
	public void decReg(Register r1){
		char[] regs = cpu.getRegs();
		regs[r1.index] = (char) ((regs[r1.index] - 1) & 0x00ff);
		checkZ(r1);
		checkS(r1);
		checkP(r1);
		//TODO: MAJOR: Figure out how to emulate AC flag
		cpu.setRegs(regs);
	}
	
	public void regToMem(Register r, char addr){
		char[] mem = cpu.getMem();
		mem[addr] = cpu.getRegs()[r.index];
		cpu.setMem(mem);
	}
	
	public boolean checkFlag(Flag f){
		if(cpu.getFlags()[f.index] == 1){
			return true;
		}else{
			return false;
		}
		
	}
	
	public boolean jumpIf(Flag f, boolean state, char addr, char addr2){
	if(checkFlag(f) == state){
		char set = (char) ((cpu.getMem()[addr] << 8) | cpu.getMem()[addr2]);
		cpu.setPc(set);
		return true;
	}else{
		return false;
	}
	}
	
	public void jump(char addr, char addr2){
		char set = (char) ((cpu.getMem()[addr] << 8) | cpu.getMem()[addr2]);
		cpu.setPc(set);
	}
	
	public void memToSP(char addr1, char addr2){
		int sp = cpu.getSp();
		char[] mem = cpu.getMem();
		sp = (mem[addr1] << 8) | (mem[addr2]);
		cpu.setSp(sp);
	}
	
	public void SPtoRegPair(Register r1, Register r2){
		int sp = cpu.getSp();
		char[] regs = cpu.getRegs();
		regs[r1.index] = (char)(sp);
		regs[r2.index] = (char)(sp + 1);
		cpu.setRegs(regs);
	}
	
	public void call(char addr1, char addr2){
		char[] mem = cpu.getMem();
		int sp = cpu.getSp();
		int pc = cpu.getPc();
		sp += 2;
		int set = (cpu.getPc() & 0xFF00 << 8) | (cpu.getPc() & 0x00FF);
		mem[sp] = (char) set;
		pc = ((mem[addr1] << 8) | (mem[addr2]));
		cpu.setMem(mem);
		cpu.setSp(sp);
		cpu.setPc(pc);
	}
	
	public void regPairToReg(Register rp1, Register rp2, Register r){
		int set = ((cpu.getRegs()[rp1.index] << 8) | (cpu.getRegs()[rp2.index]));
		char[] regs = cpu.getRegs();
		regs[r.index] = (char) set;
		cpu.setRegs(regs);
	}
	
	public void regToRegPair(Register rp1, Register rp2, Register r){
		char[] regs = cpu.getRegs();
		char set = regs[r.index];
		regs[rp1.index] = (char) (set & 0xFF00);
		regs[rp2.index] = (char) (set & 0x00FF);
		cpu.setRegs(regs);
	}
	
}
