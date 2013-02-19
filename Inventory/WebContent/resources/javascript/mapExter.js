
$(document).ready(function() {
	$(PrimeFaces.escapeClientId('form-table:itemReportTable')).hide();
	 });

function gotoOffice(office){
	
	$(PrimeFaces.escapeClientId('form-control:officetext')).val(office);
	
	$('.scribblar').animate({'width': '15%'}, 200);
	$('.scribblar').animate({'height': '20%'}, 200);
	
	//$(PrimeFaces.escapeClientId('form-control:goToOfficeButton')).click();
	//document.getElementById("officetext").value = office;
	//alert(office);
	
	$(PrimeFaces.escapeClientId('form-control:goToOfficeButton')).click();
	
	$(PrimeFaces.escapeClientId('form-table:itemReportTable')).css('height','80%');
	$(PrimeFaces.escapeClientId('form-table:itemReportTable')).show();
	
	
}
