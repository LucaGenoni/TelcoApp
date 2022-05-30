package entities;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the tblPeriods database table.
 * 
 * 
 */
@Entity
@Table(name = "tblPeriods")
@NamedQuery(name = "TblPeriod.findAll", query = "SELECT t FROM TblPeriod t")
public class TblPeriod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int PK_Periods;

	@Column(nullable = false)
	private double fee;

	@Column(nullable = false)
	private int months;

	public TblPeriod() {
	}

	public int getPK_Periods() {
		return this.PK_Periods;
	}

	public void setPK_Periods(int PK_Periods) {
		this.PK_Periods = PK_Periods;
	}

	public double getFee() {
		return this.fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public int getMonths() {
		return this.months;
	}

	public void setMonths(int months) {
		this.months = months;
	}
}