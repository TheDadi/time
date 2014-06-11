package ch.lepeit.stundenabrechnung.controller;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.PieChartModel;

import ch.lepeit.stundenabrechnung.model.BuchartStunden;
import ch.lepeit.stundenabrechnung.model.GroupedJournal;
import ch.lepeit.stundenabrechnung.model.Verbuchbar;
import ch.lepeit.stundenabrechnung.service.AuswertungService;
import ch.lepeit.stundenabrechnung.service.JournalService;

/**
 * ViewController für den Auswertungstab (auswertung.xhtml)
 * 
 * Stellt die Daten für das Kuchendiagramm sowie für die Liste der nicht
 * verbuchbaren Journaleinträge bereit. Für diese Daten wird auch eine
 * "paging"-Funktion bereit gestellt, um den Monat, von welchem die Daten sind
 * zu ändern.
 * 
 * @author Sven Tschui C910511
 * 
 */
@Named
@RequestScoped
public class AuswertungController implements Serializable, Observer {
	private static final long serialVersionUID = 20120524L;

	@EJB
	private AuswertungService auswertungService;

	@EJB
	private JournalService journalService;

	private List<GroupedJournal> nichtVerbuchbar;

	@Inject
	private AuswertungPagingController pagingController;

	private PieChartModel verbuchbarChart;
	private PieChartModel buchartChart;

	private PieChartModel createBuchartChart(Date date) {
		List<BuchartStunden> list = auswertungService.getBuchartProMonat(date);

		buchartChart = new PieChartModel();
		buchartChart.setMouseoverHighlight(true);
		buchartChart.setLegendPosition("e");
		buchartChart.setFill(true);
		buchartChart.setShowDataLabels(true);
		buchartChart.setDiameter(150);

		for (BuchartStunden v : list) {
			buchartChart.set(createLabel(v.getStunden(), v.getBuchart()),
					v.getStunden());
		}
		if (list.isEmpty()) {
			buchartChart.set("No Data found", 0);
		}

		return buchartChart;
	}

	public String createLabel(Double stunden, boolean verbuchbar) {
		NumberFormat stundenFormat = NumberFormat.getNumberInstance();
		stundenFormat.setMaximumFractionDigits(0);

		NumberFormat tagFormat = NumberFormat.getNumberInstance();
		tagFormat.setMaximumFractionDigits(1);

		return stundenFormat.format(stunden) + " Stunden / "
				+ tagFormat.format(stunden / 8.4) + " Tage "
				+ (verbuchbar ? "Verbuchbar" : "Nicht verbuchbar");
	}

	public String createLabel(Double stunden, String tool) {
		NumberFormat stundenFormat = NumberFormat.getNumberInstance();
		stundenFormat.setMaximumFractionDigits(0);

		NumberFormat tagFormat = NumberFormat.getNumberInstance();
		tagFormat.setMaximumFractionDigits(1);

		return stundenFormat.format(stunden) + " Stunden / "
				+ tagFormat.format(stunden / 8.4) + " Tage " + tool;
	}

	private PieChartModel createVerbuchbarChart(Date date) {
		List<Verbuchbar> list = auswertungService.getVerbuchbar(date);

		verbuchbarChart = new PieChartModel();

		int i = 0;
		for (Verbuchbar v : list) {

			verbuchbarChart.set(createLabel(v.getZeit(), v.isVerbuchbar()),
					v.getZeit());
			if (i == 0) {
				if (v.isVerbuchbar() == true) {
					verbuchbarChart.setSeriesColors("04B404, DF0101");
				} else {
					verbuchbarChart.setSeriesColors("DF0101, 04B404");
				}
				i++;
			}
		}

		verbuchbarChart.setLegendPosition("e");
		verbuchbarChart.setFill(true);
		verbuchbarChart.setShowDataLabels(true);
		verbuchbarChart.setDiameter(150);
		verbuchbarChart.setMouseoverHighlight(true);

		if (list.isEmpty()) {
			verbuchbarChart.set("No Data found", 0);
		}

		return verbuchbarChart;
	}

	@PreDestroy
	public void destruct() {
		this.pagingController.deleteObserver(this);
	}

	public PieChartModel getBuchartChart() {
		return buchartChart;
	}

	public List<GroupedJournal> getNichtVerbuchbar() {
		return this.nichtVerbuchbar;
	}

	public PieChartModel getVerbuchbarChart() {
		return verbuchbarChart;
	}

	@PostConstruct
	public void init() {
		this.pagingController.addObserver(this);
		this.loadMonat(this.pagingController.getMonat());
	}

	private void loadMonat(Date monat) {
		nichtVerbuchbar = journalService
				.getNichtVerbuchbarGroupedJournals(monat);
		verbuchbarChart = createVerbuchbarChart(monat);
		buchartChart = createBuchartChart(monat);
	}

	@Override
	public void update(Observable o, Object arg) {
		this.loadMonat(this.pagingController.getMonat());
	}
}
