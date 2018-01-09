package main.model;

import main.utils.WEAPONTYPE;

//represents a weapon. contains information about weapon damage and speed
public class Weapon {
	private int mhWeaponDamageMin;
	private int mhWeaponDamageMax;
	private int mainhandSpeed;
	//offhandspeed can be set to 0 if using 2h
	private int offhandSpeed;
	private int ohWeaponDamageMin;
	private int ohWeaponDamageMax;
	private WEAPONTYPE mhWeaponType;
	private WEAPONTYPE ohWeaponType;
	private double normalizedMhSpeed;
	private double normalizedOhSpeed;
	private Proc mhProc;
	private Proc ohProc;

	public Proc getMhProc() {
		return mhProc;
	}
	public void setMhProc(Proc mhProc) {
		this.mhProc = mhProc;
	}
	public Proc getOhProc() {
		return ohProc;
	}
	public void setOhProc(Proc ohProc) {
		this.ohProc = ohProc;
	}
	
	//normalized weapon speeds
	public static final double TWOHAND = 3.3, ONEHAND = 2.4, DAGGER = 1.7;
	
	public int getMhWeaponDamageMin() {
		return mhWeaponDamageMin;
	}
	public int getMhWeaponDamageMax() {
		return mhWeaponDamageMax;
	}
	public int getMainhandSpeed() {
		return mainhandSpeed;
	}
	public int getOffhandSpeed() {
		return offhandSpeed;
	}
	public int getOhWeaponDamageMin() {
		return ohWeaponDamageMin;
	}
	public int getOhWeaponDamageMax() {
		return ohWeaponDamageMax;
	}
	public double getNormalizedMhSpeed() {
		return normalizedMhSpeed;
	}
	public double getNormalizedOhSpeed() {
		return normalizedOhSpeed;
	}
	public WEAPONTYPE getMhWeaponType() {
		return mhWeaponType;
	}
	public WEAPONTYPE getohWeaponType() {
		return ohWeaponType;
	}
	
	private Weapon(WeaponBuilder builder) {
        this.mhWeaponDamageMin = builder.mhWeaponDamageMin;
        this.mhWeaponDamageMax = builder.mhWeaponDamageMax;
        this.mainhandSpeed = builder.mhSpeed;
        this.offhandSpeed = builder.ohSpeed;
        this.ohWeaponDamageMin = builder.ohWeaponDamageMin;
        this.ohWeaponDamageMax = builder.ohWeaponDamageMax;
        this.mhWeaponType = builder.mhWeaponType;
        this.ohWeaponType = builder.ohWeaponType;
        this.normalizedMhSpeed = builder.normalizedMhSpeed;
        this.normalizedOhSpeed = builder.normalizedOhSpeed;
    }
	
	public static MhWeaponDamageMin builder() {
	    return new WeaponBuilder();
	}
	
	public static class WeaponBuilder implements MhWeaponDamageMin, MhWeaponDamageMax, MhSpeed, NormalizedMhSpeed, MhWeaponType, Build{
        //mandatory
        private int mhWeaponDamageMin;
        private int mhWeaponDamageMax;
        private int mhSpeed;
        private double normalizedMhSpeed;
        private WEAPONTYPE mhWeaponType;
        
        @Override
        public Weapon build() {
            return new Weapon(this);
        }
        
        @Override
        public Build mhWeaponType(WEAPONTYPE mhWeaponType) {
            this.mhWeaponType = mhWeaponType;
            return this;
        }
        @Override
        public MhWeaponType normalizedMhSpeed(double normalizedMhSpeed) {
           this.normalizedMhSpeed = normalizedMhSpeed;
            return this;
        }
        @Override
        public NormalizedMhSpeed mhSpeed(int mhSpeed) {
            this.mhSpeed = mhSpeed;
            return this;
        }
        @Override
        public MhSpeed mhWeaponDamageMax(int mhWeaponDamageMax) {
            this.mhWeaponDamageMax = mhWeaponDamageMax;
            return this;
        }
        @Override
        public MhWeaponDamageMax mhWeaponDamageMin(int mhWeaponDamageMin) {
            this.mhWeaponDamageMin = mhWeaponDamageMin;
            return this;
        }
        
        //non-mandatory
        private int ohWeaponDamageMin = 0;
        private int ohWeaponDamageMax = 0;
        private int ohSpeed = 0;
        private double normalizedOhSpeed = 0;
        private WEAPONTYPE ohWeaponType = null;
        
        @Override
        public Build ohWeaponType(WEAPONTYPE ohWeaponType) {
            this.ohWeaponType = ohWeaponType;
            return this;
        }
        @Override
        public Build normalizedOhSpeed(double normalizedOhSpeed) {
            this.normalizedOhSpeed = normalizedOhSpeed;
            return this;
        }
        @Override
        public Build ohSpeed(int ohSpeed) {
            this.ohSpeed = ohSpeed;
            return this;
        }
        @Override
        public Build ohWeaponDamageMax(int ohWeaponDamageMax) {
            this.ohWeaponDamageMax = ohWeaponDamageMax;
            return this;
        }
        @Override
        public Build ohWeaponDamageMin(int ohWeaponDamageMin) {
            this.ohWeaponDamageMin = ohWeaponDamageMin;
            return this;
        }
    }
    
    public interface MhWeaponDamageMin{
        public MhWeaponDamageMax mhWeaponDamageMin(int mhWeaponDamageMin);
    }
    public interface MhWeaponDamageMax{
        public MhSpeed mhWeaponDamageMax(int mhWeaponDamageMax);
    }
    public interface MhSpeed{
        public NormalizedMhSpeed mhSpeed(int mhSpeed);
    }
    public interface NormalizedMhSpeed{
        public MhWeaponType normalizedMhSpeed(double normalizedMhSpeed);
    }
    public interface MhWeaponType{
        public Build mhWeaponType(WEAPONTYPE mhWeaponType);
    }

    public interface Build{
        public Weapon build();
        public Build ohWeaponDamageMin(int ohWeaponDamageMin);
        public Build ohWeaponDamageMax(int ohWeaponDamageMax);
        public Build ohSpeed(int ohSpeed);
        public Build normalizedOhSpeed(double normalizedOhSpeed);
        public Build ohWeaponType(WEAPONTYPE ohWeaponType);
    }
}
