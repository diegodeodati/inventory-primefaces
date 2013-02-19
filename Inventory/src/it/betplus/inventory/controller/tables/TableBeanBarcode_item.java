package it.betplus.inventory.controller.tables;

import it.betplus.inventory.controller.Barcode_itemBean;
import it.betplus.inventory.db.dao.Barcode_itemDAO;
import it.betplus.inventory.db.dao.GeneralKindDAO;
import it.betplus.inventory.exception.DataAccessException;
import it.betplus.inventory.model.Barcode_itemDataModel;
import it.betplus.inventory.primitive.Barcode_item;
import it.betplus.inventory.primitive.Barcode_print;
import it.betplus.inventory.primitive.GeneralKind;
import it.betplus.inventory.utils.Printer;
import it.betplus.web.framework.exceptions.DataLayerException;
import it.betplus.web.framework.managedbeans.BaseTableBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.print.PrintException;

import org.primefaces.model.StreamedContent;

import com.lowagie.text.DocumentException;

@ManagedBean(name = "tableBeanBarcode_item")
@SessionScoped
public class TableBeanBarcode_item extends BaseTableBean {

	private static final long serialVersionUID = -7127998751959747683L;

	private List<Barcode_item> tableList;
	private List<Barcode_print> tableReviewList;
	private Barcode_itemDataModel dataModel;

	private Barcode_item barcodeGet;
	private Barcode_print barcodePrintGet;

	private List<GeneralKind> optionGeneralKind;
	private GeneralKind filterGeneralKind;

	private StreamedContent file;

	public TableBeanBarcode_item() throws DataLayerException {
		tableList = new ArrayList<Barcode_item>();
		tableReviewList = new ArrayList<Barcode_print>();
		optionGeneralKind = new ArrayList<GeneralKind>();
	}

	@Override
	public void setListDataFromSource(ArrayList<Object> params)
			throws DataLayerException {

		try {
			List<Barcode_item> m;
			if (params.equals("used"))
				m = Barcode_itemDAO.getInstance().getUsedBarcodes();
			else
				m = Barcode_itemDAO.getInstance().getBarcodes();

			setTableList(m);
			setDataModel(new Barcode_itemDataModel(m));

			setOptionGeneralKind(GeneralKindDAO.getInstance().getGeneralKinds());

		} catch (DataAccessException e) {

			throw new DataLayerException(e.getMessage());

		}

	}

	public List<Barcode_item> getTableList() {
		return tableList;
	}

	public void setTableList(List<Barcode_item> tableList) {
		this.tableList = tableList;
	}


	public void onCreate() {
		Barcode_itemBean bcBean = findBean("barcode_itemBean");
		Barcode_item bc;

		int resultOk = 0;
		int resultKo = 0;

		for (int i = 0; i < bcBean.getQuantity(); i++) {

			bc = new Barcode_item();
			bc.setBarcode(bcBean.getgKind().getDerivedBarcode());
			bc.setDescription(bcBean.getgKind().getDerivedSignature());
			bc.setgKind(bcBean.getgKind());

			int exitCode = 0;
			try {

				exitCode = Barcode_itemDAO.getInstance().insertNewBarcode(bc);

				if (exitCode != 0) {

					bcBean.getgKind().setCounter(
							bcBean.getgKind().getCounter() + 1);

					bc.barcodeToImage();
					resultOk++;

				} else
					resultKo++;

			} catch (DataAccessException e) {
				sendErrorMessageToUser("INSERIMENTO Barcode",
						"Errore Barcode non inserito");
			} catch (DataLayerException e) {
				sendErrorMessageToUser("INSERIMENTO Barcode", e.getMessage());
			}
		}

		List<Barcode_item> m;
		try {
			m = Barcode_itemDAO.getInstance().getBarcodes();
			setTableList(m);
		} catch (DataAccessException e) {
			sendErrorMessageToUser("Errore Dati Tabella",
					"Non è stato possibile aggiornare dati tabella Barcode");
		}

		if (resultOk > 0)
			sendInfoMessageToUser("INSERIMENTO Barcode", resultOk
					+ " Barcode inserito/i con successo");

		if (resultKo > 0)
			sendErrorMessageToUser("INSERIMENTO Barcode", "Errore n°"
					+ resultKo + " Barcode non inserito/i");

	}

	public Barcode_item getBarcodeGet() {
		return barcodeGet;
	}

	public void setBarcodeGet(Barcode_item barcodeGet) {
		this.barcodeGet = barcodeGet;
	}

	public Barcode_print getBarcodePrintGet() {
		return barcodePrintGet;
	}

	public void setBarcodePrintGet(Barcode_print barcodePrintGet) {
		this.barcodePrintGet = barcodePrintGet;
	}

	public List<Barcode_print> getTableReviewList() {
		return tableReviewList;
	}

	public void setTableReviewList(List<Barcode_print> tableReviewList) {
		this.tableReviewList = tableReviewList;
	}

	public void addToTableReviewList() {
		Barcode_print bp = new Barcode_print(barcodeGet, 1);
		tableReviewList.add(bp);
	}

	public void removeFromTableReviewList() {
		tableReviewList.remove(barcodePrintGet);
	}

	public void cleanTableReviewList() {
		tableReviewList.clear();
	}

	public StreamedContent getFile() {

		try {
			if (!tableReviewList.isEmpty())
				file = Printer.getInstance().doPrint(tableReviewList);
			else
				sendErrorMessageToUser("STAMPA PDF BARCODE",
						"Non è possibile stampare lista vuota");

		} catch (PrintException e) {
			sendErrorMessageToUser("STAMPA PDF BARCODE", "Errore Stampa");
		} catch (IOException e) {
			sendErrorMessageToUser("STAMPA PDF BARCODE", "Errore Input Output");
		} catch (DocumentException e) {
			sendErrorMessageToUser("STAMPA PDF BARCODE",
					"Errore Interno del Documento");
		}

		return file;
	}

	/**
	 * @return the dataModel
	 */
	public Barcode_itemDataModel getDataModel() {
		return dataModel;
	}

	/**
	 * @param dataModel
	 *            the dataModel to set
	 */
	public void setDataModel(Barcode_itemDataModel dataModel) {
		this.dataModel = dataModel;
	}

	/**
	 * @return the optionGeneralKind
	 */
	public List<GeneralKind> getOptionGeneralKind() {
		return optionGeneralKind;
	}

	/**
	 * @param optionGeneralKind
	 *            the optionGeneralKind to set
	 */
	public void setOptionGeneralKind(List<GeneralKind> optionGeneralKind) {
		this.optionGeneralKind = optionGeneralKind;
	}

	/**
	 * @return the filterGeneralKind
	 */
	public GeneralKind getFilterGeneralKind() {
		return filterGeneralKind;
	}

	/**
	 * @param filterGeneralKind
	 *            the filterGeneralKind to set
	 */
	public void setFilterGeneralKind(GeneralKind filterGeneralKind) {
		this.filterGeneralKind = filterGeneralKind;
	}

	public void changeFilterGeneralKind() {

		List<Barcode_item> internalList = new ArrayList<Barcode_item>();

		if (filterGeneralKind != null) {
			for (Barcode_item br : dataModel.getData()) {
				if (br.getgKind().getId() == filterGeneralKind.getId())
					internalList.add(br);
			}
		} else {
			internalList = dataModel.getData();
		}

		tableList = internalList;
		// System.out.println(filterGeneralKind);
	}

}
