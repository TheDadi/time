package ch.lepeit.stundenabrechnung.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * JPA Model Journal
 * 
 * @author Generated
 * @author Sven Tschui C910511
 * 
 */
@Entity
public class Journal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer nr;
	
	private String bemerkung;
 
	@Temporal(TemporalType.DATE)
	private Date datum;

	private boolean plantaverbucht;

	private double stunden;

	// bi-directional many-to-one association to Benutzer
	@ManyToOne
	@JoinColumn(name = "benutzer_id")
	private Benutzer benutzer;

	// bi-directional many-to-one association to Task
	@ManyToOne
	@JoinColumn(name = "task")
	private Task task;

	public Journal() {
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		Journal j = (Journal) o;

		return this.nr == j.getNr();
	}

	public String getBemerkung() {
		return this.bemerkung;
	}

	public Date getDatum() {
		return this.datum;
	}

	public int getNr() {
		return this.nr;
	}
	


	public boolean getPlantaverbucht() {
		return this.plantaverbucht;
	}
	

	public Double getStunden() {
		return this.stunden;
	}

	public Task getTask() {
		return this.task;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public void setNr(int nr) {
		this.nr = nr;
	}


	public void setPlantaverbucht(boolean plantaverbucht) {

		this.plantaverbucht = plantaverbucht;
	

	}


	public void setStunden(Double stunden) {
		this.stunden = stunden;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Benutzer getBenutzer() {
		return this.benutzer;
	}

	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}

}