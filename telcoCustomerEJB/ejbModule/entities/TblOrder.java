package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the tblOrders database table.
 * 
 */
@Entity
@Table(name = "tblOrders")
@NamedQuery(name = "TblOrder.findAll", query = "SELECT t FROM TblOrder t")
@NamedQuery(name = "TblOrder.findAllRejected", query = "SELECT t FROM TblOrder t WHERE t.isValid=0")
@NamedQuery(name = "TblOrder.findAllRejectedOfUser", query = "SELECT t FROM TblOrder t WHERE t.isValid=0 and t.tblUser = ?1")
public class TblOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int PK_Orders;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creationDate;

	private byte isValid;

	@Column(nullable = false)
	private double price;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date startDate;

	@OneToMany(mappedBy = "tblOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TblBill> tblBills;

	@ManyToMany
	@JoinTable(name = "tblOrderIncludesOptional", joinColumns = {
			@JoinColumn(name = "FK_Orders", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "FK_Optionals", nullable = false) })
	private List<TblOptional> tblOptionals;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_Packages", nullable = false)
	private TblPackage tblPackage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_Periods", nullable = false)
	private TblPeriod tblPeriod;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_Users", nullable = false)
	private TblUser tblUser;

	public TblOrder() {
	}

	public TblOrder(double price, Date startDate, List<TblOptional> tblOptionals, TblPackage tblPackage,
			TblPeriod tblPeriod) {
		super();
		this.price = price;
		this.startDate = startDate;
		this.tblOptionals = tblOptionals;
		this.tblPackage = tblPackage;
		this.tblPeriod = tblPeriod;
	}

	public int getPK_Orders() {
		return this.PK_Orders;
	}

	public void setPK_Orders(int PK_Orders) {
		this.PK_Orders = PK_Orders;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public byte getIsValid() {
		return this.isValid;
	}

	public void setIsValid(byte isValid) {
		this.isValid = isValid;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public List<TblBill> getTblBills() {
		return this.tblBills;
	}

	public void setTblBills(List<TblBill> tblBills) {
		this.tblBills = tblBills;
	}

	public TblBill addTblBill(TblBill tblBill) {
		getTblBills().add(tblBill);
		tblBill.setTblOrder(this);

		return tblBill;
	}

	public TblBill removeTblBill(TblBill tblBill) {
		getTblBills().remove(tblBill);
		tblBill.setTblOrder(null);

		return tblBill;
	}

	public List<TblOptional> getTblOptionals() {
		return this.tblOptionals;
	}

	public void setTblOptionals(List<TblOptional> tblOptionals) {
		this.tblOptionals = tblOptionals;
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

	public TblUser getTblUser() {
		return this.tblUser;
	}

	public void setTblUser(TblUser tblUser) {
		this.tblUser = tblUser;
	}

//	public List<TblSchedule> getTblSchedules() {
//		return this.tblSchedules;
//	}
//
//	public void setTblSchedules(List<TblSchedule> tblSchedules) {
//		this.tblSchedules = tblSchedules;
//	}
//
//	public TblSchedule addTblSchedule(TblSchedule tblSchedule) {
//		getTblSchedules().add(tblSchedule);
//		tblSchedule.setTblOrder(this);
//
//		return tblSchedule;
//	}
//
//	public TblSchedule removeTblSchedule(TblSchedule tblSchedule) {
//		getTblSchedules().remove(tblSchedule);
//		tblSchedule.setTblOrder(null);
//
//		return tblSchedule;
//	}

}