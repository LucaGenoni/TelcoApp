package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the tblBills database table.
 * 
 * 
 */
@Entity
@Table(name = "tblBills")
@NamedQuery(name = "TblBill.findAll", query = "SELECT t FROM TblBill t")
@NamedQuery(name = "TblBill.findAllOfRejectedOrderOfUser", query = "SELECT t FROM TblBill t WHERE t.result=0 and t.tblOrder.isValid = 0 and t.tblOrder.tblUser = ?1")
public class TblBill implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int PK_Bills;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date billDate;

	@Column(nullable = false)
	private byte result;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_Orders", nullable = false)
	private TblOrder tblOrder;

	public TblBill() {
	}

	public TblBill(Date billDate, byte result, TblOrder tblOrder) {
		super();
		this.billDate = billDate;
		this.result = result;
		this.tblOrder = tblOrder;
	}

	public int getPK_Bills() {
		return this.PK_Bills;
	}

	public void setPK_Bills(int PK_Bills) {
		this.PK_Bills = PK_Bills;
	}

	public Date getBillDate() {
		return this.billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public byte getResult() {
		return this.result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public TblOrder getTblOrder() {
		return this.tblOrder;
	}

	public void setTblOrder(TblOrder tblOrder) {
		this.tblOrder = tblOrder;
	}

}