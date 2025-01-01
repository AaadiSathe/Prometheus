

/*
document.addEventListener('DOMContentLoaded', function() {

	(() => {
		'use strict';

		// Fetch all the forms we want to apply custom Bootstrap validation styles to
		const forms = document.querySelectorAll('.needs-validation');

		// Loop over them and prevent submission
		Array.from(forms).forEach(form => {
			form.addEventListener('submit', event => {
				if (!form.checkValidity()) {
					event.preventDefault();
					event.stopPropagation();
				}

				form.classList.add('was-validated');
			}, false);
		});

		// Custom JavaScript validation for name, contact number, email, and address
		document.getElementById('validationCustom01').addEventListener('input', function() {
			const inputValue = this.value.trim();
			const isValid = /^[A-Za-z]+$/.test(inputValue);
			toggleValidation(this, isValid);
		});

		document.getElementById('validationCustom02').addEventListener('input', function() {
			const inputValue = this.value.trim();
			const isValid = /^[0-9]{10}$/.test(inputValue);
			toggleValidation(this, isValid);
		});

		function toggleValidation(inputElement, isValid) {
			if (isValid) {
				inputElement.classList.remove('is-invalid');
				inputElement.classList.add('is-valid');
			} else {
				inputElement.classList.remove('is-valid');
				inputElement.classList.add('is-invalid');
			}
		}
	})();

	let renamedColumns = {}; // Initialize renamedColumns as an empty object
	

	function initializeModal() {
		const existingColumns = new Set();

		function addColumnField(columnNumber,originalValue, renamedValue) {
			console.log(columnNumber);
		 // Generate unique ID for the checkbox
    	    const checkboxId = `selectcol${columnNumber}`;
    	    console.log('In add column Field function');
    	    console.log(checkboxId);

    	    // Check if checkbox with the same ID already exists
    	    if (existingColumns.has(checkboxId)) {
    	        console.log(`Skipping column with ID ${checkboxId}, it already exists.`);
    	        return;
    	    }

    	    // Add checkbox ID to the set to track it
    	    existingColumns.add(checkboxId);
    	    console.log(existingColumns);
    	    
    	 // Reuse deleted column IDs if available
    	   

    	    const div = document.createElement('div');
    	    div.className = 'form-check mt-3';
    	    div.id = `col${columnNumber}Div`;
    	    div.dataset.column = `col${columnNumber}`;

    	   // const displayName = renamedValue || `Column ${columnNumber}`;
    	   const displayName = `Column ${columnNumber}`;
    	    const renamedName = renamedColumns[`col${columnNumber}`] || '';
    	    const placeholderText = renamedName || `Rename Column ${columnNumber}`;
    	    const originalName = originalValue || `col${columnNumber}`;

    	    div.innerHTML = `
    	        <input class="form-check-input column-checkbox" type="checkbox" value="${originalName}" id="${checkboxId}" 
    	            checked onchange="toggleColumn(this)">
    	        <label class="form-check-label" for="${checkboxId}">
    	            <span>${displayName}</span>
    	        </label>
    	        <input type="text" class="form-control mt-2 colRename" id="col${columnNumber}Rename" 
    	            placeholder="${placeholderText}" value="${renamedValue}">
    	    `;

    	    document.getElementById('selectColumnsForm').appendChild(div);

			
		}

		function toggleColumn(checkbox) {
			const columnName = checkbox.value;
			console.log(columnName);
            const divId = `col${columnName}Div`;

            if (!checkbox.checked) {
              //  removeColumn(columnName);
                document.getElementById(divId).remove();
           
            } else {
                renamedColumns[columnName] = document.getElementById(`col${columnName}Rename`).value;
            }

		}

		document.getElementById('addColumnBtn').addEventListener('click', function() {
			 const columnsCount = document.querySelectorAll('.form-check').length;
			 console.log(columnsCount);
         
			if (columnsCount < 6) {
				addColumnField(columnsCount,'','');
			} else {
				document.getElementById('error-message').style.display = 'block';
			}
		});

		document.getElementById('saveColumnsBtn').addEventListener('click', function() {
			saveRenamedColumns();
		});

		function saveRenamedColumns() {
			let renamedColumnsToSend = [];

			document.querySelectorAll('.form-check').forEach(function(div) {
				let checkbox = div.querySelector('input[type="checkbox"]');
				let input = div.querySelector('input[type="text"]');

				if (checkbox.checked && input.value.trim() !== '') {
					renamedColumnsToSend.push({
						originalName: checkbox.value,
						renamedName: input.value.trim()
					});
				}
			});

			fetch('/user/new/saverenamedcolumns', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(renamedColumnsToSend)
			})
				.then(response => {
					if (!response.ok) {
						throw new Error('Failed to save renamed columns');
					}
					console.log('Renamed columns saved successfully');
					appendRenamedColumns(renamedColumnsToSend);
					$('#selectColumnsModal').modal('hide');
				})
				.catch(error => {
					console.error('Error:', error);
				});
		}

		function appendRenamedColumns(renamedColumns) {
			const dynamicFieldsContainer = document.getElementById('dynamicFieldsContainer');
			dynamicFieldsContainer.innerHTML = '';

			renamedColumns.forEach(column => {
				const formGroup = document.createElement('div');
				formGroup.className = 'col-md-6';
				formGroup.innerHTML = `
                <div class="form-group">                                     
                    <label for="${column.originalName}" class="form-label">${column.renamedName}</label>
                    <div class="input-group">
                        <input type="text" name="${column.originalName}" class="form-control" id="${column.originalName}" required>
                    </div>
                    <div class="invalid-feedback">Please enter a value for ${column.renamedName}.</div>
                </div>
            `;
				dynamicFieldsContainer.appendChild(formGroup);
			});
		}

		function fetchRenamedColumns() {
			fetch('/user/new/getallcolumns')
				.then(response => response.json())
				.then(data => {
					console.log('Parsed columns:', data);

					const columns = Object.keys(data).map(key => ({
						originalName: key,
						renamedName: data[key]
					}));

					console.log('Transformed columns:', columns);

					renamedColumns = columns;

					columns.forEach((column, index) => {
						//console.log("orname: "+column.originalName.toString());
						addColumnField(index+1,column.originalName.toString(), column.renamedName.toString());
					});
					appendRenamedColumns(columns);
				})
				.catch(error => {
					console.error('Error fetching renamed columns:', error);
				});
		}

		$('#selectColumnsModal').on('hidden.bs.modal', function() {
			$('.modal-backdrop').remove();
		});

		document.getElementById('fetchDataBtn').addEventListener('click', function() {
			
				fetchRenamedColumns();
			
		});
	}

	// Function to fetch renamed columns from the backend
	function fetchRenamedColumnsAndPopulateModal() {
		fetch('/user/new/getallcolumns')

			.then(response => response.json())
			.then(data => {
				console.log('Parsed columns:', data);

				// Transform the map into an array of objects
				const columns = Object.keys(data).map(key => ({
					originalName: key,
					renamedName: data[key]
				}));

				console.log('Transformed columns:', columns);

				renamedColumns = columns;

				appendRenamedColumns(columns);
			})
			.catch(error => {
				console.error('Error fetching renamed columns:', error);
			});
	}

	function appendRenamedColumns(renamedColumns) {
		const dynamicFieldsContainer = document.getElementById('dynamicFieldsContainer');
		dynamicFieldsContainer.innerHTML = '';

		renamedColumns.forEach(column => {
			const formGroup = document.createElement('div');
			formGroup.className = 'col-md-6';
			formGroup.innerHTML = `
            <div class="form-group">
                <label for="${column.originalName}" class="form-label">${column.renamedName}</label>
                <div class="input-group">
                    <input type="text" name="${column.originalName}" class="form-control" id="${column.originalName}" required>
                </div>
                <div class="invalid-feedback">Please enter a value for ${column.renamedName}.</div>
            </div>
        `;
			dynamicFieldsContainer.appendChild(formGroup);
		});
	}


	initializeModal();
	fetchRenamedColumnsAndPopulateModal();
});  */


/*
document.addEventListener('DOMContentLoaded', function() {

	(() => {
		'use strict';

		// Fetch all the forms we want to apply custom Bootstrap validation styles to
		const forms = document.querySelectorAll('.needs-validation');

		// Loop over them and prevent submission
		Array.from(forms).forEach(form => {
			form.addEventListener('submit', event => {
				if (!form.checkValidity()) {
					event.preventDefault();
					event.stopPropagation();
				}

				form.classList.add('was-validated');
			}, false);
		});

		// Custom JavaScript validation for name, contact number, email, and address
		document.getElementById('validationCustom01').addEventListener('input', function() {
			const inputValue = this.value.trim();
			const isValid = /^[A-Za-z]+$/.test(inputValue);
			toggleValidation(this, isValid);
		});

		document.getElementById('validationCustom02').addEventListener('input', function() {
			const inputValue = this.value.trim();
			const isValid = /^[0-9]{10}$/.test(inputValue);
			toggleValidation(this, isValid);
		});

		function toggleValidation(inputElement, isValid) {
			if (isValid) {
				inputElement.classList.remove('is-invalid');
				inputElement.classList.add('is-valid');
			} else {
				inputElement.classList.remove('is-valid');
				inputElement.classList.add('is-invalid');
			}
		}
	})();

	let renamedColumns = {}; // Initialize renamedColumns as an empty object
	

	function initializeModal() {
		const existingColumns = new Set();

		function addColumnField(columnNumber,originalValue, renamedValue) {
			console.log(columnNumber);
		 // Generate unique ID for the checkbox
    	    const checkboxId = `selectcol${columnNumber}`;
    	    console.log('In add column Field function');
    	    console.log(checkboxId);

    	    // Check if checkbox with the same ID already exists
    	    if (existingColumns.has(checkboxId)) {
    	        console.log(`Skipping column with ID ${checkboxId}, it already exists.`);
    	        return;
    	    }

    	    // Add checkbox ID to the set to track it
    	    existingColumns.add(checkboxId);
    	    console.log(existingColumns);
    	    
    	 // Reuse deleted column IDs if available
    	   

    	    const div = document.createElement('div');
    	    div.className = 'form-check mt-3';
    	    div.id = `col${columnNumber}Div`;
    	    div.dataset.column = `col${columnNumber}`;

    	   // const displayName = renamedValue || `Column ${columnNumber}`;
    	   const displayName = `Column ${columnNumber}`;
    	    const renamedName = renamedColumns[`col${columnNumber}`] || '';
    	    const placeholderText = renamedName || `Rename Column ${columnNumber}`;
    	    const originalName = originalValue || `col${columnNumber}`;

    	    div.innerHTML = `
    	        <input class="form-check-input column-checkbox" type="checkbox" value="${originalName}" id="${checkboxId}" 
    	            checked onchange="toggleColumn(this)">
    	        <label class="form-check-label" for="${checkboxId}">
    	            <span>${displayName}</span>
    	        </label>
    	        <input type="text" class="form-control mt-2 colRename" id="col${columnNumber}Rename" 
    	            placeholder="${placeholderText}" value="${renamedValue}">
    	    `;

    	    document.getElementById('selectColumnsForm').appendChild(div);

			
		}

		

		document.getElementById('addColumnBtn').addEventListener('click', function() {
			 const columnsCount = document.querySelectorAll('.form-check').length;
			 console.log(columnsCount);
			 
			 
         
			if (columnsCount < 6) {
				addColumnField(columnsCount,'','');
			} else {
				document.getElementById('error-message').style.display = 'block';
			}
		});

		document.getElementById('saveColumnsBtn').addEventListener('click', function() {
			saveRenamedColumns();
		});

		function saveRenamedColumns() {
			let renamedColumnsToSend = [];

			document.querySelectorAll('.form-check').forEach(function(div) {
				let checkbox = div.querySelector('input[type="checkbox"]');
				let input = div.querySelector('input[type="text"]');

				if (checkbox.checked && input.value.trim() !== '') {
					renamedColumnsToSend.push({
						originalName: checkbox.value,
						renamedName: input.value.trim()
					});
				}
			});

			fetch('/user/new/saverenamedcolumns', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(renamedColumnsToSend)
			})
				.then(response => {
					if (!response.ok) {
						throw new Error('Failed to save renamed columns');
					}
					console.log('Renamed columns saved successfully');
					appendRenamedColumns(renamedColumnsToSend);
					$('#selectColumnsModal').modal('hide');
				})
				.catch(error => {
					console.error('Error:', error);
				});
		}

		function appendRenamedColumns(renamedColumns) {
			const dynamicFieldsContainer = document.getElementById('dynamicFieldsContainer');
			dynamicFieldsContainer.innerHTML = '';

			renamedColumns.forEach(column => {
				const formGroup = document.createElement('div');
				formGroup.className = 'col-md-6';
				formGroup.innerHTML = `
                <div class="form-group">                                     
                    <label for="${column.originalName}" class="form-label">${column.renamedName}</label>
                    <div class="input-group">
                        <input type="text" name="${column.originalName}" class="form-control" id="${column.originalName}" required>
                    </div>
                    <div class="invalid-feedback">Please enter a value for ${column.renamedName}.</div>
                </div>
            `;
				dynamicFieldsContainer.appendChild(formGroup);
			});
		}

		function fetchRenamedColumns() {
			fetch('/user/new/getallcolumns')
				.then(response => response.json())
				.then(data => {
					console.log('Parsed columns:', data);

					const columns = Object.keys(data).map(key => ({
						originalName: key,
						renamedName: data[key]
					}));

					console.log('Transformed columns:', columns);

					renamedColumns = columns;

					columns.forEach((column, index) => {
						//console.log("orname: "+column.originalName.toString());
						const originalName = column.originalName.toString();
						const renamedName = column.renamedName.toString();
						
						let match = originalName.match(/\d+/);
						let number;
						if(match){
							 number = Number(match[0]);
							console.log(number);
						}else{
							console.log("No number found in the string.");
						}
						
						addColumnField(number,originalName, renamedName);
					});
					appendRenamedColumns(columns);
				})
				.catch(error => {
					console.error('Error fetching renamed columns:', error);
				});
		}

		$('#selectColumnsModal').on('hidden.bs.modal', function() {
			$('.modal-backdrop').remove();
		});

		document.getElementById('fetchDataBtn').addEventListener('click', function() {
			
				fetchRenamedColumns();
			
		});
	}
	
	function toggleColumn(checkbox) {
			const columnName = checkbox.value;
			console.log(columnName);
            const divId = `col${columnName}Div`;

            if (!checkbox.checked) {
              //  removeColumn(columnName);
                document.getElementById(divId).remove();
           
            } else {
                renamedColumns[columnName] = document.getElementById(`col${columnName}Rename`).value;
            }

		}

	// Function to fetch renamed columns from the backend
	function fetchRenamedColumnsAndPopulateModal() {
		fetch('/user/new/getallcolumns')

			.then(response => response.json())
			.then(data => {
				console.log('Parsed columns:', data);

				// Transform the map into an array of objects
				const columns = Object.keys(data).map(key => ({
					originalName: key,
					renamedName: data[key]
				}));

				console.log('Transformed columns:', columns);

				renamedColumns = columns;

				appendRenamedColumns(columns);
			})
			.catch(error => {
				console.error('Error fetching renamed columns:', error);
			});
	}

	function appendRenamedColumns(renamedColumns) {
		const dynamicFieldsContainer = document.getElementById('dynamicFieldsContainer');
		dynamicFieldsContainer.innerHTML = '';

		renamedColumns.forEach(column => {
			const formGroup = document.createElement('div');
			formGroup.className = 'col-md-6';
			formGroup.innerHTML = `
            <div class="form-group">
                <label for="${column.originalName}" class="form-label">${column.renamedName}</label>
                <div class="input-group">
                    <input type="text" name="${column.originalName}" class="form-control" id="${column.originalName}" required>
                </div>
                <div class="invalid-feedback">Please enter a value for ${column.renamedName}.</div>
            </div>
        `;
			dynamicFieldsContainer.appendChild(formGroup);
		});
	}


	initializeModal();
	fetchRenamedColumnsAndPopulateModal();
});  */



document.addEventListener('DOMContentLoaded', function() {

	(() => {
		'use strict';

		// Fetch all the forms we want to apply custom Bootstrap validation styles to
		const forms = document.querySelectorAll('.needs-validation');

		// Loop over them and prevent submission
		Array.from(forms).forEach(form => {
			form.addEventListener('submit', event => {
				if (!form.checkValidity()) {
					event.preventDefault();
					event.stopPropagation();
				}

				form.classList.add('was-validated');
			}, false);
		});

		// Custom JavaScript validation for name, contact number, email, and address
		document.getElementById('validationCustom01').addEventListener('input', function() {
			const inputValue = this.value.trim();
			const isValid = /^[A-Za-z]+$/.test(inputValue);
			toggleValidation(this, isValid);
		});

		document.getElementById('validationCustom02').addEventListener('input', function() {
			const inputValue = this.value.trim();
			const isValid = /^[0-9]{10}$/.test(inputValue);
			toggleValidation(this, isValid);
		});

		function toggleValidation(inputElement, isValid) {
			if (isValid) {
				inputElement.classList.remove('is-invalid');
				inputElement.classList.add('is-valid');
			} else {
				inputElement.classList.remove('is-valid');
				inputElement.classList.add('is-invalid');
			}
		}
	})();



	function initializeModal() {
		const existingColumns = new Set();
		// const deletedColumns = new Set();

		const deletedColumns = new Set([1, 2, 3, 4, 5]);
		console.log(deletedColumns);
		console.log(existingColumns);
		let renamedColumns = {};

		function updateDeletedColumns(columnNumber) {
			deletedColumns.delete(columnNumber);
		}

		function addColumnField(columnNumber, originalValue = '', renamedValue = '') {
			console.log(columnNumber);

			const checkboxId = `selectcol${columnNumber}`;
			console.log('In add column Field function');
			console.log(checkboxId);

			if (existingColumns.has(checkboxId)) {
				console.log(`Skipping column with ID ${checkboxId}, it already exists.`);
				return;
			}

			existingColumns.add(checkboxId);
			console.log(existingColumns);

			updateDeletedColumns(columnNumber);

			const div = document.createElement('div');
			div.className = 'form-check mt-3';
			div.id = `col${columnNumber}Div`;
			div.dataset.column = `col${columnNumber}`;

			const displayName = `Column ${columnNumber}`;
			const renamedName = renamedColumns[`col${columnNumber}`] || '';
			const placeholderText = renamedName || `Rename Column ${columnNumber}`;
			const originalName = originalValue || `col${columnNumber}`;

			div.innerHTML = `
            <input class="form-check-input column-checkbox" type="checkbox" value="${originalName}" id="${checkboxId}" 
                checked>
            <label class="form-check-label" for="${checkboxId}">
                <span>${displayName}</span>
            </label>
            <input type="text" class="form-control mt-2 colRename" id="col${columnNumber}Rename" 
                placeholder="${placeholderText}" value="${renamedValue}">
        `;

			document.getElementById('selectColumnsForm').appendChild(div);

			// Add event listener for the checkbox
			div.querySelector('.column-checkbox').addEventListener('change', function() {
				toggleColumn(this);
			});
		}

		function toggleColumn(checkbox) {
			const columnName = checkbox.value;
			console.log('in toggle column method');
			console.log(columnName);
			const divId = `${columnName}Div`;


			if (!checkbox.checked) {
				const columnNumber = parseInt(columnName.match(/\d+/)[0], 10);
				console.log("in toggleColumn columnNumber: " + columnNumber);
				console.log(columnNumber);
				deletedColumns.add(columnNumber);
				console.log(deletedColumns);
				existingColumns.delete(`selectcol${columnNumber}`);
				document.getElementById(divId).remove();
			} else {
				renamedColumns[columnName] = document.getElementById(`col${columnName}Rename`).value;
			}
		}

		document.getElementById('addColumnBtn').addEventListener('click', function() {
			let columnNumber;
			console.log("in addColumn event listener");
			if (deletedColumns.size > 0) {
				columnNumber = Math.min(...deletedColumns);
				console.log("in if loop columNumber: " + columnNumber);
				deletedColumns.delete(columnNumber);
			} else {
				columnNumber = existingColumns.size + 1;
				console.log("in else columnNumber: " + columnNumber);
			}

			const columnsCount = document.querySelectorAll('.form-check').length;

			if (columnsCount < 6) {
				addColumnField(columnNumber, '', '');
			} else {
				document.getElementById('error-message').style.display = 'block';
			}
		});

		document.getElementById('saveColumnsBtn').addEventListener('click', function() {
			saveRenamedColumns();
		});

		function saveRenamedColumns() {
			let renamedColumnsToSend = [];

			document.querySelectorAll('.form-check').forEach(function(div) {
				let checkbox = div.querySelector('input[type="checkbox"]');
				let input = div.querySelector('input[type="text"]');

				if (checkbox.checked && input.value.trim() !== '') {
					renamedColumnsToSend.push({
						originalName: checkbox.value,
						renamedName: input.value.trim()
					});
				}
			});

			fetch('/user/new/saverenamedcolumns', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(renamedColumnsToSend)
			})
				.then(response => {
					if (!response.ok) {
						throw new Error('Failed to save renamed columns');
					}
					console.log('Renamed columns saved successfully');
					appendRenamedColumns(renamedColumnsToSend);
					$('#selectColumnsModal').modal('hide');
				})
				.catch(error => {
					console.error('Error:', error);
				});
		}
		
		//table

		function appendRenamedColumns(renamedColumns) {
			const dynamicFieldsContainer = document.getElementById('dynamicFieldsContainer');
			dynamicFieldsContainer.innerHTML = '';

			renamedColumns.forEach(column => {
				const formGroup = document.createElement('div');
				formGroup.className = 'col-md-6';
				formGroup.innerHTML = `
                <div class="form-group">                                     
                    <label for="${column.originalName}" class="form-label">${column.renamedName}</label>
                    <div class="input-group">
                        <input type="text" name="${column.originalName}" class="form-control" id="${column.originalName}" required>
                    </div>
                    <div class="invalid-feedback">Please enter a value for ${column.renamedName}.</div>
                </div>
            `;
				dynamicFieldsContainer.appendChild(formGroup);
			});
		}

		function fetchRenamedColumns() {
			fetch('/user/new/getallcolumns')
				.then(response => response.json())
				.then(data => {
					console.log('Parsed columns:', data);

					const columns = Object.keys(data).map(key => ({
						originalName: key,
						renamedName: data[key]
					}));

					console.log('Transformed columns:', columns);

					renamedColumns = columns;


					columns.forEach((column, index) => {

						const originalName = column.originalName.toString();
						const renamedName = column.renamedName.toString();

						let match = originalName.match(/\d+/);
						let number;
						if (match) {
							number = Number(match[0]);
							console.log(number);
						} else {
							console.log("No number found in the string.");
						}

						addColumnField(number, originalName, renamedName);
						console.log("appended the already present columns");
					});
					appendRenamedColumns(columns);
					console.log("appended the already renamed columns");
				})
				.catch(error => {
					console.error('Error fetching renamed columns:', error);
				});
		}

		$('#selectColumnsModal').on('hidden.bs.modal', function() {
			$('.modal-backdrop').remove();
		});

		document.getElementById('fetchDataBtn').addEventListener('click', function() {
			fetchRenamedColumns();
		});
	}

	// Function to fetch renamed columns from the backend
	function fetchRenamedColumnsAndPopulateModal() {
		fetch('/user/new/getallcolumns')
			.then(response => response.json())
			.then(data => {
				console.log('Parsed columns:', data);

				const columns = Object.keys(data).map(key => ({
					originalName: key,
					renamedName: data[key]
				}));

				console.log('Transformed columns:', columns);

				renamedColumns = columns;

				appendRenamedColumns(columns);
			})
			.catch(error => {
				console.error('Error fetching renamed columns:', error);
			});
	}

	function appendRenamedColumns(renamedColumns) {
		const dynamicFieldsContainer = document.getElementById('dynamicFieldsContainer');
		dynamicFieldsContainer.innerHTML = '';

		renamedColumns.forEach(column => {
			const formGroup = document.createElement('div');
			formGroup.className = 'col-md-6';
			formGroup.innerHTML = `
            <div class="form-group">
                <label for="${column.originalName}" class="form-label">${column.renamedName}</label>
                <div class="input-group">
                    <input type="text" name="${column.originalName}" class="form-control" id="${column.originalName}" required>
                </div>
                <div class="invalid-feedback">Please enter a value for ${column.renamedName}.</div>
            </div>
        `;
			dynamicFieldsContainer.appendChild(formGroup);
		});
	}

	initializeModal();
	fetchRenamedColumnsAndPopulateModal();


});



