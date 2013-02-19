package it.betplus.inventory.controller.tables;

import it.betplus.inventory.controller.ItemBean;
import it.betplus.inventory.db.dao.Barcode_itemDAO;
import it.betplus.inventory.db.dao.ComponentDAO;
import it.betplus.inventory.db.dao.ItemDAO;
import it.betplus.inventory.db.dao.KindDAO;
import it.betplus.inventory.db.dao.OfficeDAO;
import it.betplus.inventory.db.dao.OwnerDAO;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.primitive.Barcode_item;
import it.betplus.inventory.primitive.Component;
import it.betplus.inventory.primitive.Item;
import it.betplus.inventory.primitive.Kind;
import it.betplus.inventory.primitive.Network;
import it.betplus.inventory.primitive.Office;
import it.betplus.inventory.primitive.Owner;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.RowEditEvent;

@ManagedBean(name = "tableBeanItem")
@SessionScoped
public class TableBeanItem extends BaseTableBean {

	private static final long serialVersionUID = -7127998751959747683L;

	private List<Item> tableList;

	private Item itemGet;
	private List<Owner> avaliableOwner;
	private List<Office> avaliableOffice;
	private List<Item> avaliableItem;
	private List<Barcode_item> avaliableBarcode_item;
	private List<Barcode_item> filteredBarcode_item;
	private List<Kind> avaliableKind;

	private Item selectedObj;
	private int selectedOffice;
	private int selectedMap;

	public TableBeanItem() throws DataLayerException {
		tableList = new ArrayList<Item>();
		filteredBarcode_item = new ArrayList<Barcode_item>();
	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		try {

			List<Item> m;
			List<Item> items;
			if (params.isEmpty()) {
				m = new ArrayList<Item>(ItemDAO.getInstance().getItems()
						.values());
				items = m;
			} else {
				selectedOffice = (Integer) params.get(0);
				selectedMap = (Integer) params.get(1);
				m = new ArrayList<Item>(ItemDAO.getInstance()
						.getItems(selectedOffice, selectedMap).values());
				items = new ArrayList<Item>(ItemDAO.getInstance().getItems()
						.values());
			}
			setTableList(m);
			setAvaliableItem(items);
			List<Owner> owners = OwnerDAO.getInstance().getOwners();
			setAvaliableOwner(owners);
			List<Office> offices = OfficeDAO.getInstance().getOffices();
			setAvaliableOffice(offices);
			List<Barcode_item> barcode_items = Barcode_itemDAO.getInstance()
					.getBarcodes();
			setAvaliableBarcode_item(barcode_items);
			List<Kind> kinds = KindDAO.getInstance().getKinds();
			setAvaliableKind(kinds);

		} catch (DataAccessException e) {
			sendErrorMessageToUser(
					"Impossibile ricevere la lista degli Oggetti",
					e.getFaultType());

		} catch (DataLayerException e) {
			sendErrorMessageToUser("Impossibile elaborare la richiesta",
					e.getMessage());
		}

	}

	public List<Item> getTableList() {
		return tableList;
	}

	public void setTableList(List<Item> tableList) {
		this.tableList = tableList;
	}

	public List<Owner> getAvaliableOwner() {
		return avaliableOwner;
	}

	public void setAvaliableOwner(List<Owner> avaliableOwner) {
		this.avaliableOwner = avaliableOwner;
	}

	public List<Item> getAvaliableItem() {
		return avaliableItem;
	}

	public void setAvaliableItem(List<Item> avaliableItem) {
		this.avaliableItem = avaliableItem;
	}

	public List<Office> getAvaliableOffice() {
		return avaliableOffice;
	}

	public void setAvaliableOffice(List<Office> avaliableOffice) {
		this.avaliableOffice = avaliableOffice;
	}

	public List<Barcode_item> getAvaliableBarcode_item() {
		return avaliableBarcode_item;
	}

	public void setAvaliableBarcode_item(
			List<Barcode_item> avaliableBarcode_item) {
		this.avaliableBarcode_item = avaliableBarcode_item;
	}

	/**
	 * @return the filteredBarcode_item
	 */
	public List<Barcode_item> getFilteredBarcode_item() {
		return filteredBarcode_item;
	}

	/**
	 * @param filteredBarcode_item
	 *            the filteredBarcode_item to set
	 */
	public void setFilteredBarcode_item(List<Barcode_item> filteredBarcode_item) {
		this.filteredBarcode_item = filteredBarcode_item;
	}

	public Item getSelectedObj() {
		return selectedObj;
	}

	public void setSelectedObj(Item selectedObj) {
		this.selectedObj = selectedObj;
	}

	public int getSelectedOffice() {
		return selectedOffice;
	}

	public void setSelectedOffice(int selectedOffice) {
		this.selectedOffice = selectedOffice;
	}

	public int getSelectedMap() {
		return selectedMap;
	}

	public void setSelectedMap(int selectedMap) {
		this.selectedMap = selectedMap;
	}

	public void onRemove() {
		try {
			ItemDAO.getInstance().deleteItem(itemGet);
			sendInfoMessageToUser("CANCELLAZIONE Item",
					"Item cancellato con successo");
			List<Item> m = new ArrayList<Item>(ItemDAO.getInstance().getItems()
					.values());
			setTableList(m);
			List<Owner> owners = OwnerDAO.getInstance().getOwners();
			setAvaliableOwner(owners);
			List<Office> offices = OfficeDAO.getInstance().getOffices();
			setAvaliableOffice(offices);
			List<Barcode_item> barcode_items = Barcode_itemDAO.getInstance()
					.getBarcodes();
			setAvaliableBarcode_item(barcode_items);
			List<Kind> kinds = KindDAO.getInstance().getKinds();
			setAvaliableKind(kinds);
		} catch (DataAccessException e) {
			sendErrorMessageToUser("Impossibile Cancellare Oggetto",
					"Oggetto ancora in uso");
		} catch (DataLayerException e) {
			sendErrorMessageToUser(
					"Impossibile Elaborare la Cancellazione Oggetto",
					e.getMessage());
		}

	}

	public void onCreate() {
		ItemBean itemBean = findBean("itemBean");
		Item it = new Item();
		it.setDescription(itemBean.getDescription());
		it.setBrand(itemBean.getBrand());
		it.setQuantity(itemBean.getQuantity());

		// controllare se sono stati popolati...primefaces ritornera errore ???
		if (itemBean.getInternalItem() == null) {
			it.setBarcode(itemBean.getBarcode());
			it.setOffice(itemBean.getOffice());
			it.setOwner(itemBean.getOwner());
			it.setKind(itemBean.getKind());
		} else {
			it = itemBean.getInternalItem();

			Office off = new Office();
			off.setId(selectedOffice);

			it.setOffice(off);
		}

		int exitCode = 0;
		try {
			exitCode = ItemDAO.getInstance().insertNewItem(it);
		} catch (DataAccessException e) {
			sendErrorMessageToUser("Impossibile Inserire Oggetto",
					e.getMessage());
		} catch (DataLayerException e) {
			sendErrorMessageToUser(
					"Impossibile Elaborare l Inserimento Oggetto",
					e.getMessage());
		}

		if (exitCode != 0) {
			sendInfoMessageToUser("INSERIMENTO Item",
					"Item inserito con successo");
			try {

				List<Item> m = new ArrayList<Item>();

				if (selectedOffice == 0) {
					m = new ArrayList<Item>(ItemDAO.getInstance().getItems()
							.values());
				} else {
					m = new ArrayList<Item>(ItemDAO.getInstance()
							.getItems(selectedOffice, selectedMap).values());

				}
				setTableList(m);

				List<Item> items = new ArrayList<Item>(ItemDAO.getInstance()
						.getItems().values());
				setAvaliableItem(items);
				List<Owner> owners = OwnerDAO.getInstance().getOwners();
				setAvaliableOwner(owners);
				List<Office> offices = OfficeDAO.getInstance().getOffices();
				setAvaliableOffice(offices);
				List<Barcode_item> barcode_items = Barcode_itemDAO
						.getInstance().getBarcodes();
				setAvaliableBarcode_item(barcode_items);
				List<Kind> kinds = KindDAO.getInstance().getKinds();
				setAvaliableKind(kinds);
			} catch (DataAccessException e) {
				sendErrorMessageToUser(
						"Impossibile ricevere la lista degli Oggetti",
						e.getFaultType());
			} catch (DataLayerException e) {
				sendErrorMessageToUser(
						"Impossibile ricevere la lista degli Oggetti",
						"Errore Aggiornamento");
			}
		}
	}

	public void onMove(ActionEvent event) {
		ItemBean itemBean = findBean("itemBean");
		Item it = new Item();

		it.setOffice(itemBean.getOffice());
		it.setOwner(itemBean.getOwner());
		it.setQuantity(itemBean.getQuantity());

		int exitCode = 0;
		try {
			exitCode = ItemDAO.getInstance().moveItem(itemGet, it);
		} catch (DataAccessException e) {
			sendErrorMessageToUser("Impossibile Muovere Oggetto",
					e.getMessage());
		} catch (DataLayerException e) {
			sendErrorMessageToUser(
					"Impossibile Elaborare lo Spostamento Oggetto",
					e.getMessage());
		}

		if (exitCode != 0) {
			sendInfoMessageToUser("SPOSTAMENTO Item",
					"Item spostato con successo");
			try {
				List<Item> m = new ArrayList<Item>();

				if (selectedOffice == 0) {
					m = new ArrayList<Item>(ItemDAO.getInstance().getItems()
							.values());
				} else {
					m = new ArrayList<Item>(ItemDAO.getInstance()
							.getItems(selectedOffice, selectedMap).values());

				}
				setTableList(m);

				List<Item> items = new ArrayList<Item>(ItemDAO.getInstance()
						.getItems().values());
				setAvaliableItem(items);
				
				List<Owner> owners = OwnerDAO.getInstance().getOwners();
				setAvaliableOwner(owners);
				List<Office> offices = OfficeDAO.getInstance().getOffices();
				setAvaliableOffice(offices);
				List<Barcode_item> barcode_items = Barcode_itemDAO
						.getInstance().getBarcodes();
				setAvaliableBarcode_item(barcode_items);
				List<Kind> kinds = KindDAO.getInstance().getKinds();
				setAvaliableKind(kinds);
			} catch (DataAccessException e) {
				sendErrorMessageToUser(
						"Impossibile ricevere la lista degli Oggetti",
						e.getFaultType());
			} catch (DataLayerException e) {
				sendErrorMessageToUser(
						"Impossibile ricevere la lista degli Oggetti",
						"Errore Aggiornamento");
			}
		}
	}

	public void onCancel(RowEditEvent event) {
		// Item it = (Item) event.getObject();

		try {

			List<Item> m = new ArrayList<Item>();

			if (selectedOffice == 0) {
				m = new ArrayList<Item>(ItemDAO.getInstance().getItems()
						.values());
			} else {
				m = new ArrayList<Item>(ItemDAO.getInstance()
						.getItems(selectedOffice, selectedMap).values());

			}
			setTableList(m);

			sendInfoMessageToUser("MODIFICA Item", "Cancellata");
		} catch (DataAccessException e) {
			sendErrorMessageToUser("Impossibile Modificare Oggetto",
					e.getMessage());
		} catch (DataLayerException e) {
			sendErrorMessageToUser("Impossibile Ricevere la lista degli Oggetti",
					"Errore Aggiornamento");
		}

	}

	public void onEdit(RowEditEvent event) {
		Item it = (Item) event.getObject();

		try {

			ItemDAO.getInstance().modifyItem(it);

			it.setNet_discovered(ItemDAO.getInstance().getNetDiscoveredPcList()
					.contains(it.getId()));

			List<Barcode_item> barcode_items = Barcode_itemDAO.getInstance()
					.getBarcodes();
			setAvaliableBarcode_item(barcode_items);

			sendInfoMessageToUser("MODIFICA Item",
					"Item modificato con successo");
		} catch (DataAccessException e) {
			sendErrorMessageToUser("Impossibile Modificare Oggetto",
					e.getMessage());
		} catch (DataLayerException e) {
			sendErrorMessageToUser("Impossibile Elaborare la Modifica Oggetto",
					e.getMessage());
		}

	}

	public void onStartEdit(ActionEvent event) {
		filteredBarcode_item.clear();
		itemGet = (Item) event.getComponent().getAttributes().get("parameter");
		for (Barcode_item b : avaliableBarcode_item) {
			if (b.getgKind().getId() == itemGet.getKind().getgKind().getId())
				filteredBarcode_item.add(b);
		}

		if (!filteredBarcode_item.contains(itemGet.getBarcode()))
			itemGet.setBarcode(new Barcode_item());
	}

	public Item getItemGet() {
		return itemGet;
	}

	public void setItemGet(Item itemGet) {
		this.itemGet = itemGet;
	}

	public void populateComponent() {

		List<Network> internalNetworkList = new ArrayList<Network>();
		List<Component> internalComponentList = new ArrayList<Component>();
		internalNetworkList = itemGet.getNetList();

		int compId;
		try {
			compId = ComponentDAO.getInstance().findComponentId(
					internalNetworkList.get(0).getMac_address());
			internalComponentList = ComponentDAO.getInstance().getComponents(
					compId);
			itemGet.setComponentList(internalComponentList);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

	public List<Kind> getAvaliableKind() {
		return avaliableKind;
	}

	public void setAvaliableKind(List<Kind> avaliableKind) {
		this.avaliableKind = avaliableKind;
	}

	public void onSetItem(ActionEvent event) {
		itemGet = (Item) event.getComponent().getAttributes().get("parameter");
	}

	/*
	 * public void onModifyBarcode(AjaxBehaviorEvent event) { SelectOneMenu s =
	 * (SelectOneMenu) event.getSource(); System.out.println((Barcode_item)
	 * s.getValue()); }
	 */

	public void onChangeKind(AjaxBehaviorEvent event) {
		filteredBarcode_item.clear();

		SelectOneMenu s = (SelectOneMenu) event.getSource();
		Kind k = (Kind) s.getValue();

		for (Barcode_item b : avaliableBarcode_item) {
			if (b.getgKind().getId() == k.getgKind().getId())
				filteredBarcode_item.add(b);
		}
	}

}
