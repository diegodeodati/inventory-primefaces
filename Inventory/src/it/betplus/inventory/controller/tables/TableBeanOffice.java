package it.betplus.inventory.controller.tables;

import it.betplus.inventory.controller.OfficeBean;
import it.betplus.inventory.db.dao.MapDAO;
import it.betplus.inventory.db.dao.OfficeDAO;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Map;
import it.betplus.inventory.primitive.Office;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.RowEditEvent;

@ManagedBean(name = "tableBeanOffice")
@SessionScoped
public class TableBeanOffice extends BaseTableBean {

	private static final long serialVersionUID = -7127998751959747683L;

	private List<Office> tableList;
	private Office officeGet;
	private List<Map> avaliableMap;

	public TableBeanOffice() throws DataLayerException {
		tableList = new ArrayList<Office>();
	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		try {
			List<Office> m = OfficeDAO.getInstance().getOffices();
			setTableList(m);
			setAvaliableMap(MapDAO.getInstance().getMaps());

		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		}

	}

	public List<Office> getTableList() {
		return tableList;
	}

	public void setTableList(List<Office> tableList) {
		this.tableList = tableList;
	}

	public void onRemove() {
		try {
			int risultato = OfficeDAO.getInstance().deleteOffice(officeGet);
			if (risultato < 0) {
				sendErrorMessageToUser("CANCELLAZIONE Ufficio",
						"Impossibile cancellare l'Ufficio è uso");
			} else {
				sendInfoMessageToUser("CANCELLAZIONE Ufficio",
						"Ufficio cancellato con successo");
				List<Office> m = OfficeDAO.getInstance().getOffices();
				setTableList(m);
			}

		} catch (DataAccessException e) {
			sendErrorMessageToUser("CANCELLAZIONE Ufficio",
					"Errore nella cancellatzione Ufficio");
		}

	}

	/*
	 * public void onEdit() { try { UserDAO.getInstance().modifyUser(userGet,
	 * userGet.getUsername()); sendInfoMessageToUser("MODIFICA User",
	 * "User modificato con successo"); } catch (DataAccessException e) {
	 * sendErrorMessageToUser("MODIFICA User", "Errore nella modifica User"); }
	 * 
	 * }
	 */

	public void onEdit(RowEditEvent event) {
		Office off = (Office) event.getObject();

		try {
			OfficeDAO.getInstance().modifyOffice(off);
			sendInfoMessageToUser("MODIFICA Ufficio",
					"Ufficio modificato con successo");
		} catch (DataAccessException e) {
			sendErrorMessageToUser("MODIFICA Ufficio",
					"Errore nella modifica Ufficio");
		}

	}

	public void onCreate() {
		OfficeBean officeBean = findBean("officeBean");
		Office off = new Office(officeBean.getName());
		int exitCode = 0;
		try {
			exitCode = OfficeDAO.getInstance().insertNewOffice(off);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (exitCode != 0) {
			sendInfoMessageToUser("INSERIMENTO Ufficio",
					"Ufficio inserito con successo");
			List<Office> m;
			try {
				m = OfficeDAO.getInstance().getOffices();
				setTableList(m);
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		} else
			sendErrorMessageToUser("INSERIMENTO Ufficio",
					"Errore Ufficio non inserito");
	}

	public Office getOfficeGet() {
		return officeGet;
	}

	public void setOfficeGet(Office officeGet) {
		this.officeGet = officeGet;
	}

	/**
	 * @return the avaliableMap
	 */
	public List<Map> getAvaliableMap() {
		return avaliableMap;
	}

	/**
	 * @param avaliableMap the avaliableMap to set
	 */
	public void setAvaliableMap(List<Map> avaliableMap) {
		this.avaliableMap = avaliableMap;
	}

}
