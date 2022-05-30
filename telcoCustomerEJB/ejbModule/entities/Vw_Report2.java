package entities;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the vw_Report2 database table.
 * 
 */
@Entity
@Cacheable(false)
@Table(name="vw_Report2")
@NamedQuery(name="Vw_Report2.findAll", query="SELECT v FROM Vw_Report2 v")
public class Vw_Report2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private Vw_Report2PK id;

	private int nSold;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_Packages", nullable=false) 
	private TblPackage tblPackage;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_Periods", nullable=false)
	private TblPeriod tblPeriod;

	public Vw_Report2() {
	}

	public Vw_Report2PK getId() {
		return this.id;
	}

	public void setId(Vw_Report2PK id) {
		this.id = id;
	}

	public int getNSold() {
		return this.nSold;
	}

	public void setNSold(int nSold) {
		this.nSold = nSold;
	}

	public TblPackage getTblPackage() {
		return this.tblPackage;
	}

	public void setTblPackage(TblPackage tblPackage) {
		this.tblPackage = tblPackage;
	}

	public TblPeriod getTblPeriod() {
		return this.tblPeriod;
	}

	public void setTblPeriod(TblPeriod tblPeriod) {
		this.tblPeriod = tblPeriod;
	}

}