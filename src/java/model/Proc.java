package model;

public class Proc {
	private String name;
	private int procChance;
	private int damage;
	
	public Proc(String name, int procChance, int damage) {
		this.procChance = procChance;
		this.damage = damage;
		this.name = name;
	}
	//return 0 if the proc doesnt happen, returns damage if it happens
	public int proc() {
		if(Math.random() * 1000 < procChance) {
			return damage;
		}
		return 0;
	}
	public String getName() {
		return name;
	}
}