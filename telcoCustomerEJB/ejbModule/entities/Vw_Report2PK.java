package entities;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The primary key class for the vw_Report2 database table.
 * 
 */
@Embeddable
public class Vw_Report2PK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false, unique=true, nullable=false)
	private int FK_Packages;

	@Column(insertable=false, updatable=false, unique=true, nullable=false)
	private int FK_Periods;

	public Vw_Report2PK() {
	}
	
	public Vw_Report2PK(int FK_Packages,int FK_Periods) {
		this.FK_Packages = FK_Packages;
		this.FK_Periods = FK_Periods;
	}
	
	public int getFK_Packages() {
		return this.FK_Packages;
	}
	public void setFK_Packages(int FK_Packages) {
		this.FK_Packages = FK_Packages;
	}
	public int getFK_Periods() {
		return this.FK_Periods;
	}
	public void setFK_Periods(int FK_Periods) {
		this.FK_Periods = FK_Periods;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Vw_Report2PK)) {
			return false;
		}
		Vw_Report2PK castOther = (Vw_Report2PK)other;
		return 
			(this.FK_Packages == castOther.FK_Packages)
			&& (this.FK_Periods == castOther.FK_Periods);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.FK_Packages;
		hash = hash * prime + this.FK_Periods;
		
		return hash;
	}
}