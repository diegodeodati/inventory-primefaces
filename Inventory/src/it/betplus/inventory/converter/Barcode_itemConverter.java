package it.betplus.inventory.converter;

import it.betplus.inventory.controller.tables.TableBeanItem;
import it.betplus.inventory.primitive.Barcode_item;

import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class Barcode_itemConverter implements Converter {

	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String submittedValue) {
		if (submittedValue.trim().equals("")) {
			return null;
		} else {
			try {
				// int number = Integer.parseInt(submittedValue);
				TableBeanItem tabBean = TableBeanItem
						.findBean("tableBeanItem");
				ArrayList<Barcode_item> listBarcode_item = (ArrayList<Barcode_item>) tabBean
						.getAvaliableBarcode_item();

				for (Barcode_item b : listBarcode_item) {
					if (b.getBarcode().equals(submittedValue)) {
						return b;
					}
				}

			} catch (NumberFormatException exception) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid Barcode"));
			}
		}
		return null;
	}

	public String getAsString(FacesContext facesContext, UIComponent component,
			Object value) {
		if (value == null || value.equals("")) {
			return "";
		} else {
			return ((Barcode_item) value).getBarcode();
		}
	}
}