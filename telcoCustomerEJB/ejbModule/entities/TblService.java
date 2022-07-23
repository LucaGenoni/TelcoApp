package entities;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the tblServices database table.
 * 
 * 
 */
@Entity
@Table(name = "tblServices")
@NamedQuery(name = "TblService.findAll", query = "SELECT t FROM TblService t ORDER BY t.type")
public class TblService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int PK_Services;

	private int sms;
	
	private int min;	
	
	private int gb;
	
	private double feeSms;
	
	private double feeMin;
	
	private double feeGb;

	@Column(nullable = false, length = 40)
	private String type;

	public TblService() {
	}
	
	public TblService(String type, int sms, int min, int gb, double feeSms, double feeMin, double feeGb) {
		super();
		this.sms = sms;
		this.min = min;
		this.gb = gb;
		this.feeSms = feeSms;
		this.feeMin = feeMin;
		this.feeGb = feeGb;
		this.type = type;
	}
	
	public int getPK_Services() {
		return this.PK_Services;
	}

	public void setPK_Services(int PK_Services) {
		this.PK_Services = PK_Services;
	}

	public double getFeeGb() {
		return this.feeGb;
	}

	public void setFeeGb(double feeGb) {
		this.feeGb = feeGb;
	}

	public double getFeeMin() {
		return this.feeMin;
	}

	public void setFeeMin(double feeMin) {
		this.feeMin = feeMin;
	}

	public double getFeeSms() {
		return this.feeSms;
	}

	public void setFeeSms(double feeSms) {
		this.feeSms = feeSms;
	}

	public int getGb() {
		return this.gb;
	}

	public void setGb(int gb) {
		this.gb = gb;
	}

	public int getMin() {
		return this.min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getSms() {
		return this.sms;
	}

	public void setSms(int sms) {
		this.sms = sms;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}