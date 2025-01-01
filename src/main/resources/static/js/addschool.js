



document.addEventListener("DOMContentLoaded", function() {


	console.log("in addschool js file");
	let num = 1;

	const customColumns = /*[[${school.customColumns}]]*/ {};
	let customColumns1 = [];


	//	 console.log(customColumns1);

	function addColumn(columnName = '') {

		const form = document.getElementById('customColumnsForm');
		const div = document.createElement('div');
		div.className = 'form-check mt-3';
		div.id = `col${num}Div`;
		div.dataset.column = `col${num}`;

		div.innerHTML = `
		 <input class="form-check-input ccolumn-checkbox" type="checkbox" value="" checked>
		 <label class="form-check-label">Add Custom Column</label>
		 <input type="text" class="form-control mt-2" placeholder="specify custom column name" value="${columnName}" >
		 `;

		form.appendChild(div);
		num++;
	}

	/*
	function saveCustomColumns(){
		console.log("in save custom columns");
		document.querySelectorAll('.form-check').forEach(function(div){
			let checkbox = div.querySelector('input[type="checkbox"]');
			let input = div.querySelector('input[type="text"]');
			console.log("in save custom loop");
			if(checkbox.checked && input.value.trim() !== ''){
				console.log("in save custom loop");
				customColumns1.push({
					customColumnName: input.value.trim()
				});
			}
		});
	    
		console.log(customColumns1);
	    
		appendCustomColumns(customColumns1);
	}  */

	function saveCustomColumns() {
		let columnsToSave = [];
		let columnsToDelete = [];

		document.querySelectorAll('.form-check').forEach(function(div) {
			let checkbox = div.querySelector('input[type="checkbox"]');
			let input = div.querySelector('input[type="text"]');
			if (checkbox && input) {
				if (checkbox.checked && input.value.trim() !== '') {
					columnsToSave.push({
						customColumnName: input.value.trim()
					});
				} else if (!checkbox.checked && input.value.trim() !== '') {
					columnsToDelete.push({
						customColumnName: input.value.trim()
					});
					div.classList.add('to-delete');
				}else{
					div.classList.remove('to-delete');
				}
			}
		});

		fetch('/user/school/updatecustomcolumns', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({ columnsToSave, columnsToDelete })
		})
			.then(response => response.json())
			.then(data => {
				appendCustomColumns(columnsToSave);
				removeDeletedColumns();
			})
			.catch(error => {
				console.error('Error saving custom columns:', error);
			});
	}
	
	function removeDeletedColumns(){
		document.querySelectorAll('.to-delete').forEach(function(div){
			div.remove();
		});
	}

	function appendCustomColumns(customColumns) {
		
		const customFieldsContainer = document.getElementById('customFieldsContainer');
		customFieldsContainer.innerHTML = '';
		
		customColumns.forEach(column => {
			const formGroup = document.createElement('div');
			formGroup.className = 'col-md-6';
			formGroup.innerHTML = `
			 <div class="form-group">
			 <label for="${column.customColumnName}" class="form-label">${column.customColumnName}</label>
			 <div class="input-group">
			 	<input type="text" name="${column.customColumnName}" class="form-control" id="${column.customColumnName}" required>
			 </div>
			 <div class="invalid-feedback">Please enter a value for ${column.customColumnName}.</div>
			 `
			customFieldsContainer.appendChild(formGroup);
		});


		

	}

	document.getElementById('addCustomColumnBtn').addEventListener('click', function() {
		addColumn();
	});

	document.getElementById('saveCustomColumnsBtn').addEventListener('click', function() {
		saveCustomColumns();
	});

	function fethCustomColumns() {
		fetch('/user/school/getAllCustomColumns')
			.then(response => response.json())
			.then(data => {
				console.log('parsed columns:', data);

				customColumns1 = Object.keys(data).map(key => ({
					customColumnName: key,
					customColumnValue: data[key]
				}));
				customColumns1.forEach(column => addColumn(column.customColumnName.toString()));
				console.log(customColumns1);
				appendCustomColumns(customColumns1);
			})
			.catch(error => {
				console.error('Error fetching custom columns:', error);
			});
	}

	fethCustomColumns();


	document.getElementById('schoolForm').addEventListener('submit', function(event) {
		// event.preventDefault();

		let form = event.target;
		let formData = new FormData(form);
		let jsonData = {};

		formData.forEach((value, key) => {
			jsonData[key] = value;
		});

		// Collect custom fields
		let customColumns = {};
		document.querySelectorAll('#customFieldsContainer input').forEach(input => {
			let columnName = input.getAttribute('name');
			customColumns[columnName] = input.value;
		});

		jsonData['customColumns'] = customColumns;

		fetch('/user/addSchool', {
			method: 'POST', // Specify the method as POST
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(jsonData)
		})
			.then(response => {
				if (!response.ok) {
					throw new Error('Network response was not ok');
				}
				return response.json();
			})
			.then(data => {
				console.log('Success:', data);
			})
			.catch(error => {
				console.error('Error:', error);
			})
			.finally(() => {
				// Reset the form regardless of the response
				//form.reset();
			});
	});





});

$(document).ready(function() {
	$('#saveCustomColumnsBtn').click(function() {

		$('#customColumnsModal').on('hidden.bs.modal', function() {
			$('.modal-backdrop').remove();
		});
		$('#customColumnsModal').modal('hide');
	});
});


$(document).ready(function() {
	// Handle the change event of the checkbox
	$('#customColumnsModal').on('change', '.form-check-input', function() {
		// Check if checkbox is unchecked
		if (!$(this).prop('checked')) {
			// Remove the parent form-check div
			//$(this).closest('.form-check').remove();
		}
	});
});