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
@NamedQuery(name = "TblService.findAll", query = "SELECT t FROM TblService t")
public class TblService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int PK_Services;

	private double feeGb;

	private double feeMin;

	private double feeSms;

	private int gb;

	private int min;

	@Column(nullable = false, length = 255)
	private String name;

	private int sms;

	@Column(nullable = false, length = 40)
	private String type;

	public TblService() {
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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