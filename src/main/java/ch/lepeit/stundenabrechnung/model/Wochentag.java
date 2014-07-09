package ch.lepeit.stundenabrechnung.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Model Wochentag
 * 
 * @author Sven Tschui C910511
 * 
 */
public class Wochentag {
    private final List<GroupedJournal> buchungen;

    private final Date datum;

    private double total = 0;

    public Wochentag(Date datum, List<GroupedJournal> buchungen) {
        this.datum = datum;
        this.buchungen = buchungen;

        for (GroupedJournal b : buchungen) {
            total += b.getStunden();
        }
    }

    public List<GroupedJournal> getBuchungen() {
        return buchungen;
    }

    public String getDatum() {
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        return simpleDateFormat.format(datum);
    }

    public double getTotal() {
        return total;
    }
}
