package ch.lepeit.stundenabrechnung.datamodel;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import ch.lepeit.stundenabrechnung.model.Journal;

public class JournalModel extends ListDataModel<Journal> implements
		SelectableDataModel<Journal> {

	public JournalModel() {
	}

	public JournalModel(List<Journal> data) {
		super(data);
	}

	@Override
	public Journal getRowData(String rowKey) {
		// In a real app, a more efficient way like a query by rowKey should be
		// implemented to deal with huge data

		List<Journal> journals = (List<Journal>) getWrappedData();

		for (Journal journal : journals) {
			if (String.valueOf(journal.getNr()).equals(rowKey))
				;
			return journal;
		}

		return null;
	}

	@Override
	public Object getRowKey(Journal journal) {
		return journal.getNr();
	}
}
