package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the tblAlerts database table.
 * 
 * 
 */
@Entity
@Table(name = "tblAlerts")
@NamedQuery(name = "TblAlert.findAll", query = "SELECT t FROM TblAlert t")
public class TblAlert implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int PK_Alerts;

	@Column(nullable = false)
	private double amount;

	@Column(nullable = false, length = 255)
	private String email;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date lastReject;

	@Column(nullable = false, length = 255)
	private String username;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_Users", nullable = false)
	private TblUser tblUser;

	public TblAlert() {
	}

	public TblAlert(double amount, TblUser tblUser, String username, String email, Date lastReject) {
		super();
		this.amount = amount;
		this.email = email;
		this.lastReject = lastReject;
		this.username = username;
		this.tblUser = tblUser;
	}

	public int getPK_Alerts() {
		return this.PK_Alerts;
	}

	public void setPK_Alerts(int PK_Alerts) {
		this.PK_Alerts = PK_Alerts;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastReject() {
		return this.lastReject;
	}

	public void setLastReject(Date lastReject) {
		this.lastReject = lastReject;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public TblUser getTblUser() {
		return this.tblUser;
	}

	public void setTblUser(TblUser tblUser) {
		this.tblUser = tblUser;
	}

}