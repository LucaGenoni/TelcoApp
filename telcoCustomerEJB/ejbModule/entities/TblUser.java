package entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * The persistent class for the tblUsers database table.
 * 
 */
@Entity
@Table(name = "tblUsers")
@NamedQuery(name = "TblUser.findAll", query = "SELECT t FROM TblUser t")
@NamedQuery(name = "TblUser.checkSignIn", query = "SELECT t FROM TblUser t WHERE t.username = ?1 and t.password = ?2")
@NamedQuery(name = "TblUser.findAllInsolvent", query = "SELECT t FROM TblUser t WHERE t.isInsolvent = 1")
@NamedQuery(name = "TblUser.findByUsername", query = "SELECT t FROM TblUser t WHERE t.username = ?1")
public class TblUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int PK_Users;

	@Column(nullable = false, length = 255)
	private String email;

	@Column(nullable = false)
	private byte isInsolvent;

	@Column(nullable = false, length = 255)
	private String password;

	@Column(nullable = false)
	private byte role;

	@Column(nullable = false, length = 255)
	private String username;

	@OneToMany(mappedBy = "tblUser", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH, CascadeType.DETACH })
	private List<TblAlert> tblAlerts;

	@OneToMany(mappedBy = "tblUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TblOrder> tblOrders;

	@OneToMany(mappedBy = "tblUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TblSchedule> tblSchedules;

	public TblUser() {
	}

	public int getPK_Users() {
		return this.PK_Users;
	}

	public void setPK_Users(int PK_Users) {
		this.PK_Users = PK_Users;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte getIsInsolvent() {
		return this.isInsolvent;
	}

	public void setIsInsolvent(byte isInsolvent) {
		this.isInsolvent = isInsolvent;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte getRole() {
		return this.role;
	}

	public void setRole(byte role) {
		this.role = role;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<TblAlert> getTblAlerts() {
		return this.tblAlerts;
	}

	public void setTblAlerts(List<TblAlert> tblAlerts) {
		this.tblAlerts = tblAlerts;
	}

	public TblAlert addTblAlert(TblAlert tblAlert) {
		getTblAlerts().add(tblAlert);
		tblAlert.setTblUser(this);

		return tblAlert;
	}

	public TblAlert removeTblAlert(TblAlert tblAlert) {
		getTblAlerts().remove(tblAlert);
		tblAlert.setTblUser(null);

		return tblAlert;
	}

	public List<TblOrder> getTblOrders() {
		return this.tblOrders;
	}

	public void setTblOrders(List<TblOrder> tblOrders) {
		this.tblOrders = tblOrders;
	}

	public TblOrder addTblOrder(TblOrder tblOrder) {
		getTblOrders().add(tblOrder);
		tblOrder.setTblUser(this);

		return tblOrder;
	}

	public TblOrder removeTblOrder(TblOrder tblOrder) {
		getTblOrders().remove(tblOrder);
		tblOrder.setTblUser(null);

		return tblOrder;
	}

	public List<TblSchedule> getTblSchedules() {
		return this.tblSchedules;
	}

	public void setTblSchedules(List<TblSchedule> tblSchedules) {
		this.tblSchedules = tblSchedules;
	}

	public TblSchedule addTblSchedule(TblSchedule tblSchedule) {
		getTblSchedules().add(tblSchedule);
		tblSchedule.setTblUser(this);

		return tblSchedule;
	}

	public TblSchedule removeTblSchedule(TblSchedule tblSchedule) {
		getTblSchedules().remove(tblSchedule);
		tblSchedule.setTblUser(null);

		return tblSchedule;
	}

}