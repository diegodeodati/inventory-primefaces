package it.betplus.inventory.controller.tables;

import it.betplus.inventory.controller.OwnerBean;
import it.betplus.inventory.db.dao.OwnerDAO;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Owner;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.event.RowEditEvent;

@ManagedBean(name = "tableBeanOwner")
@SessionScoped
public class TableBeanOwner extends BaseTableBean {

	private static final long serialVersionUID = -7127998751959747683L;

	private List<Owner> tableList;
	private Owner ownerGet;

	public TableBeanOwner() throws DataLayerException {
		tableList = new ArrayList<Owner>();
	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		try {

			List<Owner> m = OwnerDAO.getInstance().getOwners();
			setTableList(m);

		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		}

	}

	public List<Owner> getTableList() {
		return tableList;
	}

	public void setTableList(List<Owner> tableList) {
		this.tableList = tableList;
	}

	public void onRemove(ActionEvent event) {
		try {
			OwnerDAO.getInstance().deleteOwner(ownerGet);
			sendInfoMessageToUser("CANCELLAZIONE Associazione Utente",
					"Utente cancellato con successo");
			List<Owner> m = OwnerDAO.getInstance().getOwners();
			setTableList(m);
		} catch (DataAccessException e) {
			sendErrorMessageToUser("CANCELLAZIONE Associazione Utente",
					"Errore nella cancellatzione Utente");
		}

	}

	public void onEdit(RowEditEvent event) {
		Owner own = (Owner) event.getObject();

		try {
			OwnerDAO.getInstance().modifyOwner(own);
			sendInfoMessageToUser("MODIFICA Associazione Utente",
					"Utente modificato con successo");
		} catch (DataAccessException e) {
			sendErrorMessageToUser("MODIFICA Associazione Utente",
					"Errore nella modifica Utente");
		}

	}

	public void onCreate() {
		OwnerBean ownerBean = findBean("ownerBean");
		Owner own = new Owner(ownerBean.getName(), ownerBean.getSurname(),
				ownerBean.getEmail());
		int exitCode = 0;
		try {
			exitCode = OwnerDAO.getInstance().insertNewOwner(own);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (exitCode != 0) {
			sendInfoMessageToUser("INSERIMENTO Associazione Utente",
					"Utente inserito con successo");
			List<Owner> m;
			try {
				m = OwnerDAO.getInstance().getOwners();
				setTableList(m);
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		} else
			sendErrorMessageToUser("INSERIMENTO Associazione Utente",
					"Errore Utente non inserito");
	}

	public Owner getOwnerGet() {
		return ownerGet;
	}

	public void setOwnerGet(Owner ownerGet) {
		this.ownerGet = ownerGet;
	}
	
	public void onSetOwner(ActionEvent event){
		ownerGet  = (Owner) event.getComponent().getAttributes().get("parameter");
	}

}
