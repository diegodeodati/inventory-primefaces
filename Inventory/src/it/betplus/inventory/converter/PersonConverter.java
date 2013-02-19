package it.betplus.inventory.converter;

import it.betplus.inventory.controller.tables.TableBeanItem;
import it.betplus.inventory.primitive.Owner;

import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class PersonConverter implements Converter{

	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                int number = Integer.parseInt(submittedValue);
                TableBeanItem tabBean = TableBeanItem.findBean("tableBeanItem");
                ArrayList<Owner> listOwner = (ArrayList<Owner>) tabBean.getAvaliableOwner();
                
                for (Owner o : listOwner) {
                    if (o.getId() == number) {
                        return o;
                    }
                }
                
            } catch(NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Person"));
            }
        }

        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((Owner) value).getId());
        }
    }
}
