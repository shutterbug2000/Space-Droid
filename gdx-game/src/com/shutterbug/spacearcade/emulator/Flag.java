package com.shutterbug.spacearcade.emulator;

public enum Flag
{
	Sign(0),
	Zero(1),
	Parity(2),
	Carry(3),
	Aux(4);

	public final int index;

	private Flag(int index) {
		this.index = index;
	}
}
