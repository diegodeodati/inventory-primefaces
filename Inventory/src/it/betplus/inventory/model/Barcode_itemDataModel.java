package it.betplus.inventory.model;

import it.betplus.inventory.primitive.Barcode_item;

import java.util.List;

import javax.faces.model.ListDataModel;

public class Barcode_itemDataModel  extends ListDataModel<Barcode_item> implements java.io.Serializable  {  



	private static final long serialVersionUID = -55232085478984773L;



	public Barcode_itemDataModel() {
    }

    public Barcode_itemDataModel(List<Barcode_item> data) {
        super(data);
    }
    
     
    public List<Barcode_item> getData(){
    	@SuppressWarnings("unchecked")
		List<Barcode_item> modelData = (List<Barcode_item>) getWrappedData();
    	return modelData;
    }
    
     
}
                    
