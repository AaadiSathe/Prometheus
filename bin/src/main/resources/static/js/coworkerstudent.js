function handleFormSubmit(event, formId, tableId) {
    event.preventDefault();


  




    var form = document.getElementById(formId);
    var formData = new FormData(form);
    var queryString = new URLSearchParams(formData).toString();

    fetch(form.getAttribute('action') + '?' + queryString, {
        method: 'GET',
    })
    .then(response => response.text())
    .then(data => {
        var parser = new DOMParser();
        var htmlDoc = parser.parseFromString(data, 'text/html');
        var tableHtml = htmlDoc.getElementById(tableId).innerHTML;
        document.getElementById(tableId).innerHTML = tableHtml;
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
		  function handleDropdown(selectElement,tableId) {
        var form = selectElement.form;
        var formData = new FormData(form);
        var form1 = document.getElementById('dropdownvalue');
        var form2 = document.getElementById('selectedDropdownValue');
       // var form3=document.getElementById('hiddennamevalue');

	    var dropdownValue = form1.querySelector('[name="dropdownvalue"]').value;
	    var pagelimitValue = form2.querySelector('[name="pagelimit"]').value;
	   // var nameValue= form3.querySelector('[name="name"]').value;
    
    // Update hidden input values based on selected values
    document.getElementById('hiddenPagelimit').value = pagelimitValue;
    document.getElementById('hiddenDropdownValue').value = dropdownValue;
   // document.getElementById('hiddennamevalue').value=nameValue;


        fetch(form.getAttribute('action') + '?' + new URLSearchParams(formData), {
            method: 'GET',
        })
        .then(response => response.text())
        .then(data => {
		
            var parser = new DOMParser();
            var htmlDoc = parser.parseFromString(data, 'text/html');
            var tableHtml = htmlDoc.getElementById(tableId).innerHTML;
            document.getElementById(tableId).innerHTML = tableHtml;
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
 
    
		
    
    
    
    

		 