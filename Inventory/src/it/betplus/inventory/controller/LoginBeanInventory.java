package it.betplus.inventory.controller;

import it.betplus.inventory.controller.sheets.ReportBean;
import it.betplus.inventory.db.dao.UserDAO;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.User;
import it.betplus.inventory.utils.XmlGenerator;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.LoginBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBeanInventory extends LoginBean {

	private static final long serialVersionUID = -7859104635092743231L;

	protected UserBean loggedUser;

	public LoginBeanInventory() {
		super();
		configureOutcomes("inventory", "index");
	}

	// *** Business methods ***//
	@Override
	public Object getUserAuthenticationDTO(String username, String password)
			throws DataLayerException, Exception {

		User user = null;

		try {

			user = UserDAO.getInstance().getUserByUsernamePassword(username,
					password);
			
			
			XmlGenerator.getInstance().doXmlMaps();

		} catch (DataAccessException dle) {
			// handle exception from Mysqlfacade
			throw new DataLayerException(dle.getMessage());
		}

		return user;

	}

	@Override
	public void resetLoginSession() {

		// remove user from session bean
		this.loggedUser = null;
		this.username = "";
		this.password = "";

	}

	@Override
	public String forwardToLoginOutcome() {

		try {

			ReportBean reportBean = findBean("reportBean");
			return reportBean.filterInventory();

		} catch (DataAccessException e) {

			sendErrorMessageToUser("ERRORE LOGIN", "UTENTE NON TROVATO");
			// log.error(e.getMessage());
		}

		return null;

	}

	@Override
	public void setLoggedUserFromDTO(Object user) {

		loggedUser = new UserBean((User) user);

	}

	// *** get & set methods ***//
	public UserBean getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(UserBean loggedUser) {
		this.loggedUser = loggedUser;
	}

}
