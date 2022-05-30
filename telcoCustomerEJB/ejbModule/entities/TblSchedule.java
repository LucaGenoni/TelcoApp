package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the tblSchedules database table.
 * 
 * 
 */
@Entity
@Table(name = "tblSchedules")
@NamedQuery(name = "TblSchedule.findAll", query = "SELECT t FROM TblSchedule t")
public class TblSchedule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int PK_Schedules;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date activeDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date deactiveDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_Orders", nullable = false)
	private TblOrder tblOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_Users", nullable = false)
	private TblUser tblUser;

	public TblSchedule() {
	}

	public TblSchedule(Date activeDate, Date deactiveDate, TblOrder tblOrder, TblUser tblUser) {
		super();
		this.activeDate = activeDate;
		this.deactiveDate = deactiveDate;
		this.tblOrder = tblOrder;
		this.tblUser = tblUser;
	}

	public int getPK_Schedules() {
		return this.PK_Schedules;
	}

	public void setPK_Schedules(int PK_Schedules) {
		this.PK_Schedules = PK_Schedules;
	}

	public Date getActiveDate() {
		return this.activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	public Date getDeactiveDate() {
		return this.deactiveDate;
	}

	public void setDeactiveDate(Date deactiveDate) {
		this.deactiveDate = deactiveDate;
	}

	public TblOrder getTblOrder() {
		return this.tblOrder;
	}

	public void setTblOrder(TblOrder tblOrder) {
		this.tblOrder = tblOrder;
	}

	public TblUser getTblUser() {
		return this.tblUser;
	}

	public void setTblUser(TblUser tblUser) {
		this.tblUser = tblUser;
	}

}