package entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * The persistent class for the tblPackages database table.
 * 
 */
@Entity
@Table(name = "tblPackages")
@NamedQuery(name = "TblPackage.findAll", query = "SELECT t FROM TblPackage t")
@NamedQuery(name = "TblPackage.findByName", query = "SELECT t FROM TblPackage t WHERE t.name = ?1")
public class TblPackage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int PK_Packages;

	@Column(nullable = false, length = 255)
	private String name;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "tblPackageIncludesOptional", joinColumns = {
			@JoinColumn(name = "FK_Packages", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "FK_Optionals", nullable = false) })
	private List<TblOptional> tblOptionals;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "tblPackageLastsPeriod", joinColumns = {
			@JoinColumn(name = "FK_Packages", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "FK_Periods", nullable = false) })
	private List<TblPeriod> tblPeriods;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "tblPackageOffersService", joinColumns = {
			@JoinColumn(name = "FK_Packages", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "FK_Services", nullable = false) })
	private List<TblService> tblServices;

	public TblPackage() {
	}

	public int getPK_Packages() {
		return this.PK_Packages;
	}

	public void setPK_Packages(int PK_Packages) {
		this.PK_Packages = PK_Packages;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TblOptional> getTblOptionals() {
		return this.tblOptionals;
	}

	public void setTblOptionals(List<TblOptional> tblOptionals) {
		this.tblOptionals = tblOptionals;
	}

	public List<TblPeriod> getTblPeriods() {
		return this.tblPeriods;
	}

	public void setTblPeriods(List<TblPeriod> tblPeriods) {
		this.tblPeriods = tblPeriods;
	}

	public List<TblService> getTblServices() {
		return this.tblServices;
	}

	public void setTblServices(List<TblService> tblServices) {
		this.tblServices = tblServices;
	}
}