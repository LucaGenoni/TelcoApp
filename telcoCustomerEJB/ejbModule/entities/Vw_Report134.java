package entities;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the vw_Report134 database table.
 * 
 */
@Entity
@Cacheable(false)
@Table(name="vw_Report134")
@NamedQuery(name="Vw_Report134.findAll", query="SELECT v FROM Vw_Report134 v")
public class Vw_Report134 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int FK_Packages;

	private double nAvgOpt;

	private int nSold;

	private double priceNoOpt;

	private double priceWithOpt;

	//bi-directional one-to-one association to TblPackage
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_Packages", nullable=false, insertable=false, updatable=false)
	private TblPackage tblPackage;

	public Vw_Report134() {
	}

	public int getFK_Packages() {
		return this.FK_Packages;
	}

	public void setFK_Packages(int FK_Packages) {
		this.FK_Packages = FK_Packages;
	}

	public double getNAvgOpt() {
		return this.nAvgOpt;
	}

	public void setNAvgOpt(double nAvgOpt) {
		this.nAvgOpt = nAvgOpt;
	}

	public int getNSold() {
		return this.nSold;
	}

	public void setNSold(int nSold) {
		this.nSold = nSold;
	}

	public double getPriceNoOpt() {
		return this.priceNoOpt;
	}

	public void setPriceNoOpt(double priceNoOpt) {
		this.priceNoOpt = priceNoOpt;
	}

	public double getPriceWithOpt() {
		return this.priceWithOpt;
	}

	public void setPriceWithOpt(double priceWithOpt) {
		this.priceWithOpt = priceWithOpt;
	}

	public TblPackage getTblPackage() {
		return this.tblPackage;
	}

	public void setTblPackage(TblPackage tblPackage) {
		this.tblPackage = tblPackage;
	}

}