package ch.lepeit.stundenabrechnung.datamodel;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import ch.lepeit.stundenabrechnung.model.GroupedJournal;
import ch.lepeit.stundenabrechnung.model.Journal;
import ch.lepeit.stundenabrechnung.model.Wochentag;

public class GroupedJournalModel extends ListDataModel<Wochentag> implements
		SelectableDataModel<Wochentag> {

	public GroupedJournalModel() {
	}

	public GroupedJournalModel(List<Wochentag> data) {
		super(data);
	}

	@Override
	public Wochentag getRowData(String rowKey) {
		// In a real app, a more efficient way like a query by rowKey should be
		// implemented to deal with huge data

		List<Wochentag> journals = (List<Wochentag>) getWrappedData();

		for (Wochentag journal : journals) {
			String i = journal.getDatum().toString();
			if (i.equals(rowKey))
				;
			return journal;
		}

		return null;
	}

	@Override
	public Object getRowKey(Wochentag journal) {
		String i = journal.getDatum().toString();
		return i;
	}
}
