package ch.lepeit.stundenabrechnung.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model GroupedJournal
 * 
 * Journaleinträge gruppiert nach Tag und Task
 * 
 * @author Sven Tschui C910511
 * 
 */
public class GroupedJournal implements Serializable {
	private static final long serialVersionUID = 1L;

	private Date datum;

	private int plantaverbucht;

	private Double stunden;

	private Task task;

	public GroupedJournal() {
	}

	public GroupedJournal(Date datum, Double stunden, Task task,
			boolean plantaverbucht) {
		super();
		this.datum = datum;
		this.stunden = stunden;
		this.task = task;
		this.setPlantaverbucht(plantaverbucht);
	}

	public GroupedJournal(Date datum, Double stunden, Task task,
			int plantaverbucht) {
		super();
		this.datum = datum;
		this.stunden = stunden;
		this.task = task;
		this.plantaverbucht = plantaverbucht;
	}

	public Date getDatum() {
		return datum;
	}

	public Double getStunden() {
		return stunden;
	}

	public Task getTask() {
		return task;
	}

	public boolean isPlantaverbucht() {
		if (plantaverbucht == 0)
			return false;
		else
			return true;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public void setPlantaverbucht(boolean plantaverbucht) {

		if (plantaverbucht)
			this.plantaverbucht = 1;
		else
			this.plantaverbucht = 0;

	}

	public void setPlantaverbucht(int plantaverbucht) {

		this.plantaverbucht = plantaverbucht;

	}

	public void setStunden(Double stunden) {
		this.stunden = stunden;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}