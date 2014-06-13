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

	private Double stunden;

	private Task task;

	public GroupedJournal() {
	}


	public GroupedJournal(Date datum, Double stunden, Task task) {
		super();
		this.datum = datum;
		this.stunden = stunden;
		this.task = task;
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

	public void setDatum(Date datum) {
		this.datum = datum;
	}



	public void setStunden(Double stunden) {
		this.stunden = stunden;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}