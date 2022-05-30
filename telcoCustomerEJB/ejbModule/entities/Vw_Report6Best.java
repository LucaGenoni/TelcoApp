package entities;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the vw_Report6Best database table.
 * 
 */
@Entity
@Cacheable(false)
@Table(name="vw_Report6Best")
@NamedQuery(name="Vw_Report6Best.findAll", query="SELECT v FROM Vw_Report6Best v")
public class Vw_Report6Best implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int FK_Optionals;

	private int score;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_Optionals", nullable=false, insertable=false, updatable=false)
	private TblOptional tblOptional;

	public Vw_Report6Best() {
	}

	public int getFK_Optionals() {
		return this.FK_Optionals;
	}

	public void setFK_Optionals(int FK_Optionals) {
		this.FK_Optionals = FK_Optionals;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public TblOptional getTblOptional() {
		return this.tblOptional;
	}

	public void setTblOptional(TblOptional tblOptional) {
		this.tblOptional = tblOptional;
	}

}