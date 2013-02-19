
function simulateClick(strId) {
	
	$(PrimeFaces.escapeClientId('form-dialog:barcode_printReviewTable:downloadLink')).click();
	


}


function clearAllFilters() {
	$('.ui-datatable .ui-column-filter').val('');
}
