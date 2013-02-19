package it.betplus.inventory.controller.sheets;


import it.betplus.inventory.controller.tables.TableBeanBarcode_item;
import it.betplus.inventory.controller.tables.TableBeanItem;
import it.betplus.inventory.controller.tables.TableBeanOffice;
import it.betplus.inventory.controller.tables.TableBeanOwner;
import it.betplus.inventory.controller.tables.TableBeanUser;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Barcode_item;
import it.betplus.inventory.primitive.Item;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.GeneralBean;
import it.betplus.web.framework.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "reportBean")
@SessionScoped
public class ReportBean extends GeneralBean {

	private boolean tooManyRecords;
	private boolean errorDate = false;

	private Date dataS;
	private Date dataE;
	private Date dataM;

	private String manageInventory = "";
	private String manageInternalUse = "";
	private String manageMaps = "";
	private String manageUtility = "";

	private int selectedOffice = 0;
	private int selectedMap =0;
	
	private SelectItem[] barcodeOptions = {};

	private boolean dateChanged = true;

	public boolean isDateChanged() {
		return dateChanged;
	}

	public void setDateChanged(boolean dateChanged) {
		this.dateChanged = dateChanged;
	}

	// ************** SELECT FOR CUSTOM FILTER

	
	public boolean isErrorDate() {
		return errorDate;
	}

	public void setErrorDate(boolean errorDate) {
		this.errorDate = errorDate;
	}

	public boolean isTooManyRecords() {
		return tooManyRecords;
	}

	public void setTooManyRecords(int size) {
		if (size > 60000) {
			tooManyRecords = true;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_FATAL,
									"EXPORT DISABILITATO",
									"Limite Record per Export raggiunto. Si consiglia di effettuare il Totale Giorni"));
		} else
			tooManyRecords = false;
	}

	public ReportBean() throws DataLayerException {

	}

	public void setDataS(Date ds) {
		dataS = ds;
	}

	public void setDataE(Date de) {
		dataE = de;
	}

	public Date getDataS() {
		return dataS;
	}

	public Date getDataE() {
		return dataE;
	}

	public Date getDataM() {
		return dataM;
	}

	public void setDataM(Date dataM) {
		this.dataM = dataM;
	}

	public String getDataStoString() {
		return DateUtils.dateToString(dataS, "dd/MM/yyyy");
	}

	public String getDataEtoString() {
		return DateUtils.dateToString(dataE, "dd/MM/yyyy");
	}

	public String getDataMtoString() {
		return DateUtils.dateToString(dataM, "dd/MM/yyyy");
	}

	public String filterUser() throws DataAccessException {

		TableBeanUser tabBeanUser = findBean("tableBeanUser");
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		tabBeanUser.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/Inventory/pages/report/user.xhtml"))
			return "";
		else
			return "user";

	}

	public String filterInventory() throws DataAccessException {

		

		TableBeanItem tabBeanItem = findBean("tableBeanItem");
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		tabBeanItem.populateTable(arrayParams);
		tabBeanItem.setSelectedOffice(0);
		tabBeanItem.setSelectedMap(0);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/Inventory/pages/report/inventory.xhtml"))
			return "";
		else
			return "inventory";

	}

	public void filterItemByMap() throws DataAccessException {

		TableBeanItem tabBeanItem_pc = findBean("tableBeanItem");
		List<Item> m = new ArrayList<Item>(); 
		tabBeanItem_pc.setTableList(m);
		
		TableBeanItem tabBeanItem = findBean("tableBeanItem");
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(selectedOffice);
		arrayParams.add(selectedMap);
		tabBeanItem.populateTable(arrayParams);
		
		

	}

	public String filterOffice() throws DataAccessException {

		TableBeanOffice tabBeanOffice = findBean("tableBeanOffice");
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		tabBeanOffice.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/Inventory/pages/report/office.xhtml"))
			return "";
		else
			return "office";

	}

	public String filterBarcode() throws DataAccessException {

		TableBeanBarcode_item tabBeanBarcode_item = findBean("tableBeanBarcode_item");
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add("all");
		tabBeanBarcode_item.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/Inventory/pages/report/barcode.xhtml"))
			return "";
		else
			return "barcode";

	}
	
	public String generatorBarcode() throws DataAccessException {

		TableBeanBarcode_item tabBeanBarcode_item = findBean("tableBeanBarcode_item");
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add("all");
		tabBeanBarcode_item.populateTable(arrayParams);
		
		
		for(Barcode_item b:tabBeanBarcode_item.getTableList()){
			b.barcodeToImage();
		}

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/Inventory/pages/util/barcode.xhtml"))
			return "";
		else
			return "generator-barcode";

	}
	

	public String filterOwner() throws DataAccessException {

		TableBeanOwner tabBeanOwner = findBean("tableBeanOwner");
		ArrayList<Object> arrayParams = new ArrayList<Object>();
		tabBeanOwner.populateTable(arrayParams);

		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/Inventory/pages/report/owner.xhtml"))
			return "";
		else
			return "owner";

	}

	public String filterMap21() {

		selectedMap = 1;
		selectedOffice = 0;
		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/Inventory/pages/map/map21.xhtml"))
			return "";
		else
			return "map21";

	}
	
	public String filterMap11() {

		selectedMap = 2;
		selectedOffice = 0;
		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/Inventory/pages/map/map11.xhtml"))
			return "";
		else
			return "map11";

	}
	
	public String filterMap12() {

		selectedMap = 3;
		selectedOffice = 0;
		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/Inventory/pages/map/map12.xhtml"))
			return "";
		else
			return "map12";

	}
	
	public String filterMap01() {

		selectedMap = 4;
		selectedOffice = 0;
		String redirect = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getRequestURI();

		log.info(redirect);
		if (redirect.contains("/Inventory/pages/map/map01.xhtml"))
			return "";
		else
			return "map01";

	}


	/**
	 * @return the selectedOffice
	 */
	public int getSelectedOffice() {
		return selectedOffice;
	}

	public SelectItem[] getBarcodeOptions() {
		
		barcodeOptions = new SelectItem[2];  
		
		barcodeOptions[0] = new SelectItem("none", "Da Associare");
		barcodeOptions[1] = new SelectItem("", "Tutti");
		
		return barcodeOptions;
	}

	public void setBarcodeOptions(SelectItem[] barcodeOptions) {
		this.barcodeOptions = barcodeOptions;
	}

	/**
	 * @param selectedOffice
	 *            the selectedOffice to set
	 */
	public void setSelectedOffice(int selectedOffice) {
		this.selectedOffice = selectedOffice;
	}

	/**
	 * @return the selectedMaps
	 */
	public int getSelectedMap() {
		return selectedMap;
	}

	/**
	 * @param selectedMap the selectedMap to set
	 */
	public void setSelectedMap(int selectedMap) {
		this.selectedMap = selectedMap;
	}
	
	public boolean isMapSelected(){		
		return selectedMap!=0 && selectedOffice !=0;
	}

	public String getManageInventory() {
		return manageInventory;
	}

	public void setManageInventory(String manageInventory) {
		this.manageInventory = manageInventory;
	}

	public String getManageInternalUse() {
		return manageInternalUse;
	}

	public void setManageInternalUse(String manageInternalUse) {
		this.manageInternalUse = manageInternalUse;
	}

	public String getManageMaps() {
		return manageMaps;
	}

	public void setManageMaps(String manageMaps) {
		this.manageMaps = manageMaps;
	}

	/**
	 * @return the manageUtility
	 */
	public String getManageUtility() {
		return manageUtility;
	}

	/**
	 * @param manageUtility the manageUtility to set
	 */
	public void setManageUtility(String manageUtility) {
		this.manageUtility = manageUtility;
	}

	
}
