package ch.lepeit.stundenabrechnung.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Observable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import ch.lepeit.stundenabrechnung.datamodel.JournalModel;
import ch.lepeit.stundenabrechnung.model.Journal;
import ch.lepeit.stundenabrechnung.service.JournalService;

/**
 * ViewController für die Korrekturansicht des Journals (korrektur.xhtml)
 * 
 * Stellt alle Journaleinträge sowie alle Tasks zur verfügung. Es gibt noch die
 * möglichkeit einen Journaleintrag zu editieren. Die Journaleinträge werden in
 * Form eines JournalDataModel zur verfügung gestellt, um das Serverseitige
 * Paging von Richfaces zu ermöglichen.
 * 
 * @author Sven Tschui C910511
 * 
 */
@Named
@SessionScoped
public class KorrekturController extends Observable implements Serializable {
	private static final long serialVersionUID = 20120516L;


	@EJB
	private JournalService journalService;

	private Journal selectedItem;

	private List<Journal> selection;

	private JournalModel journalModel;

	public JournalModel getBuchungen() {

		this.selection = journalService.getJournals();
		this.journalModel = new JournalModel(selection);
		return journalModel;

	}

	public Journal getSelectedItem() {
		return selectedItem;
	}

	public Collection<Journal> getSelection() {
		return selection;
	}

	@PostConstruct
	public void init() {
		this.selectedItem = null;
		this.selection = null;
	}

	public void onRowSelect(SelectEvent event) {
		
		this.selectedItem = (Journal) event.getObject();
		this.setChanged();
		this.notifyObservers();
	}

	public void setSelectedItem(Journal selectedItem) {

		this.selectedItem = selectedItem;
		this.setChanged();
		this.notifyObservers();
	}

	public void setSelection(List<Journal> selection) {
		this.selection = selection;
	}
}
