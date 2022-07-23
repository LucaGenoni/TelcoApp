package entities;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The persistent class for the tblOptionals database table.
 * 
 * 
 */
@Entity
@Table(name = "tblOptionals")
@NamedQuery(name = "TblOptional.findAll", query = "SELECT t FROM TblOptional t ORDER BY t.name")
@NamedQuery(name = "TblOptional.findByName", query = "SELECT t FROM TblOptional t WHERE t.name = ?1")
public class TblOptional implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int PK_Optionals;

	@Column(nullable = false)
	private double fee;

	@Column(nullable = false, length = 255)
	private String name;

	public TblOptional() {
	}

	public int getPK_Optionals() {
		return this.PK_Optionals;
	}

	public void setPK_Optionals(int PK_Optionals) {
		this.PK_Optionals = PK_Optionals;
	}

	public double getFee() {
		return this.fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}