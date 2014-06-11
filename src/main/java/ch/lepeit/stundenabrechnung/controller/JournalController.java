package ch.lepeit.stundenabrechnung.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.event.DragDropEvent;

import ch.lepeit.stundenabrechnung.datamodel.GroupedJournalModel;
import ch.lepeit.stundenabrechnung.model.GroupedJournal;
import ch.lepeit.stundenabrechnung.model.Wochentag;
import ch.lepeit.stundenabrechnung.service.JournalService;

/**
 * ViewController für die Anzeige des Wochenjournals (journal.xhtml)
 * 
 * Es wird eine Woche in Form einer Liste von Wochentagen (nur Arbeitstage)
 * bereitgestellt. Für jeden Wochentag stehen somit alle Buchungen zur
 * verfügung. Es gibt eine "paging"-Funktion, mit welcher die Woche, zu welcher
 * die Daten bereitgestellt werden geändert werden kann.
 * 
 * @author Sven Tschui C910511
 * 
 */
@Named
@SessionScoped
public class JournalController implements Serializable {
	private static final long serialVersionUID = 20120516L;

	@EJB
	private JournalService journalService;

	private Date woche;

	private List<Wochentag> wochentage;

	private List<Wochentag> droppedJournals;

	private GroupedJournalModel groupedJournalModel;
	public Date getWoche() {
		return woche;
	}

	public List<Wochentag> getWochentage() {
		Calendar c = new GregorianCalendar();
		c.setTime(this.getWoche());
		this.loadWoche(c.getTime());
		return wochentage;
	}
	
	public GroupedJournalModel getWochentage2() {
		Calendar c = new GregorianCalendar();
		c.setTime(this.getWoche());
		this.loadWoche(c.getTime());
		groupedJournalModel = new GroupedJournalModel(wochentage);
		return groupedJournalModel;
	}

	@PostConstruct
	public void init() {
		// Startdatum auf Tagesdatum setzen, Wochentage berechnen und Buchungen
		// laden
		this.loadWoche(new Date());
		droppedJournals = new ArrayList<Wochentag>();
	}

	public String letzteWoche() {
		Calendar c = new GregorianCalendar();

		c.setTime(this.getWoche());

		c.add(Calendar.WEEK_OF_MONTH, -1);

		this.loadWoche(c.getTime());

		return null;
	}

	private void loadWoche(Date woche) {
		this.woche = woche;

		this.wochentage = new Vector<Wochentag>();

		Calendar c = new GregorianCalendar();

		c.setTime(woche);

		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		for (int i = 0; i < 5; i++) {
			this.wochentage.add(new Wochentag(c.getTime(), journalService
					.getGroupedJournals(c.getTime())));
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	public String naechsteWoche() {
		Calendar c = new GregorianCalendar();

		c.setTime(this.getWoche());

		c.add(Calendar.WEEK_OF_MONTH, 1);

		this.loadWoche(c.getTime());

		return null;
	}

	public void reload() {
		this.loadWoche(this.getWoche());
	}

	public List<Wochentag> getDroppedJournals() {
		return droppedJournals;
	}

	public void setDroppedJournals(List<Wochentag> droppedJournals) {
		this.droppedJournals = droppedJournals;
	}

	public void onJournalDrop(DragDropEvent ddEvent) {
		System.out.println("gnitös");
		Wochentag journal = ((Wochentag) ddEvent.getData());

		droppedJournals.add(journal);
	}

	public GroupedJournalModel getGroupedJournalModel() {
		return groupedJournalModel;
	}

	public void setGroupedJournalModel(GroupedJournalModel groupedJournalModel) {
		this.groupedJournalModel = groupedJournalModel;
	}

}
