package it.betplus.inventory.controller.tables;


import it.betplus.inventory.controller.UserBean;
import it.betplus.inventory.db.dao.UserDAO;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.User;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.primefaces.event.RowEditEvent;

@ManagedBean(name = "tableBeanUser")
@SessionScoped
public class TableBeanUser extends BaseTableBean {

	private static final long serialVersionUID = -7127998751959747683L;

	private List<User> tableList;
	private User userGet;
	private SelectItem[] availableRole;

	public TableBeanUser() throws DataLayerException {
		tableList = new ArrayList<User>();
	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		try {

			List<User> m = UserDAO.getInstance().getUsers();
			setTableList(m);
			createAvailableRole();

		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		}

	}

	public List<User> getTableList() {
		return tableList;
	}

	public void setTableList(List<User> tableList) {
		this.tableList = tableList;
	}


	public void onRemove() {
		try {
			UserDAO.getInstance().deleteUser(userGet.getUsername());
			sendInfoMessageToUser("CANCELLAZIONE User",
					"User cancellato con successo");
			List<User> m = UserDAO.getInstance().getUsers();
			setTableList(m);
		} catch (DataAccessException e) {
			sendErrorMessageToUser("CANCELLAZIONE User",
					"Errore nella cancellatzione User");
		}

	}
/*
	public void onEdit() {
		try {
			UserDAO.getInstance().modifyUser(userGet,
					userGet.getUsername());
			sendInfoMessageToUser("MODIFICA User",
					"User modificato con successo");
		} catch (DataAccessException e) {
			sendErrorMessageToUser("MODIFICA User",
					"Errore nella modifica User");
		}

	}*/
	
	public void onEdit(RowEditEvent event) {
		User user = (User) event.getObject();

		try {
			UserDAO.getInstance().modifyUser(user);
			sendInfoMessageToUser("MODIFICA User",
					"User modificato con successo");
		} catch (DataAccessException e) {
			sendErrorMessageToUser("MODIFICA User",
					"Errore nella modifica User");
		}

	}

	public void onCreate() {
		UserBean userBean = findBean("userBean");
		User ut = new User(userBean.getUsername(),userBean.getPassword(),
				userBean.getName(), userBean.getSurname(), userBean.getEmail(),userBean.getRole());
		int exitCode = 0;
		try {
			exitCode = UserDAO.getInstance().insertNewUser(ut);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (exitCode != 0) {
			sendInfoMessageToUser("INSERIMENTO User",
					"User inserito con successo");
			List<User> m;
			try {
				m = UserDAO.getInstance().getUsers();
				setTableList(m);
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		} else
			sendErrorMessageToUser("INSERIMENTO User",
					"Errore User non inserito");
	}

	public User getUserGet() {
		return userGet;
	}

	public void setUserGet(User userGet) {
		this.userGet = userGet;
	}

	/**
	 * @return the availableRole
	 */
	public SelectItem[] getAvailableRole() {
		return availableRole;
	}

	/**
	 * @param availableRole the availableRole to set
	 */
	public void setAvailableRole(SelectItem[] availableRole) {
		this.availableRole = availableRole;
	}

	private void createAvailableRole() {
		availableRole = new SelectItem[3];

		availableRole[0] = new SelectItem("admin", "admin");
		availableRole[1] = new SelectItem("ict", "ict");
		availableRole[2] = new SelectItem("guest", "guest");
	}

}
