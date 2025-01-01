
document.addEventListener('DOMContentLoaded', function() {

	// Function to handle pagination
	function handlePagination(event) {
		event.preventDefault();
		var targetUrl = event.target.getAttribute('href');
		fetchData(targetUrl);
	}

	// Function to attach pagination event listeners
	function attachPaginationEventListeners() {
		var paginationLinks = document.querySelectorAll('.pagination a');
		paginationLinks.forEach(link => {
			link.addEventListener('click', handlePagination);
		});
	}

	// Function to fetch data
	function fetchData(url) {
		fetch(url)
			.then(response => response.text())
			.then(data => {
				var parser = new DOMParser();
				var htmlDoc = parser.parseFromString(data, 'text/html');

				var tableHtml = htmlDoc.getElementById('collegeTableBody');
				var paginationHtml = htmlDoc.getElementById('paginationContainer');

				if (tableHtml && paginationHtml) {
					document.getElementById('collegeTableBody').innerHTML = tableHtml.innerHTML;
					document.getElementById('paginationContainer').innerHTML = paginationHtml.innerHTML;
					attachPaginationEventListeners();
				} else {
					console.error("Error: Table or pagination elements not found in fetched content");
				}
			})
			.catch(error => {
				console.error("Error fetching content:", error);
			});
	}


	// Function to handle real-time search
	function handleSearchInput(event) {
		
		console.log("in handle search input")
		var searchTerm = event.target.value.trim();
		var selectedPageSize = document.getElementById('pageSize').value;

		if (searchTerm.length > 0) {
			fetch('/user/new/searchColleges?searchTerm=' + encodeURIComponent(searchTerm) + '&pageSize=' + selectedPageSize)
				.then(response => response.json())
				.then(data => {
					updateTable(data);
				})
				.catch(error => {
					console.error('Error: ', error);
				});
		} else {
			fetch('/user/new/colleges')
				.then(response => response.json())
				.then(data => {
					updateTable(data);
				})
				.catch(error => {
					console.error('Error: ', error);
				});
		}

		var previousPageSize = selectedPageSize;

		fetch(url)
			.then(response => response.json())
			.then(data => {
				updateTable(data);

				// After the fetch request completes, set the dropdown value back to the previous selected pageSize
				document.getElementById('pageSize').value = previousPageSize;
			})
			.catch(error => {
				console.error('Error: ', error);

				// If an error occurs, set the dropdown value back to the previous selected pageSize
				document.getElementById('pageSize').value = previousPageSize;
			});
	}

	// Event listener for real-time search
	document.getElementById('search-college-name').addEventListener('input', handleSearchInput);

	// Function to update table
	function updateTable(colleges) {
		var tableBody = document.querySelector('#collegeTableBody tbody');
		tableBody.innerHTML = ''; // Clear existing content in the table body
		colleges.forEach(function(college) {
			var row = document.createElement('tr');
			row.innerHTML = `
                <td>${college.id}</td>
                <td>${college.name}</td>
                <td>${college.email}</td>
                <td>${college.contactNumber}</td>
                <td>${college.address}</td>
                <td>${college.col6}</td>
                <td>${college.col7}</td>
                <td>
                    <a href="/user/editCollege/${college.id}" class="btn btn-primary btn-sm">Edit</a>
                    <a href="/user/deleteCollege/${college.id}" class="btn btn-danger btn-sm">Delete</a>
                </td>
            `;
			tableBody.appendChild(row); // Append the row to the table body
		});
	}


	// Function to handle page size change
	function handlePageSizeChange(event) {
		console.log("in handle pageSizeChange");
		var selectedPageSize = event.target.value;
		console.log("selectedPageSize: "+selectedPageSize);
	//	document.getElementById('pageSizeHidden').value = selectedPageSize; // Update the hidden input value
		
		
		var currentPageUrl = '/user/new/colleges';
		
		var searchTerm = document.getElementById('search-college-name').value.trim();
		var url = currentPageUrl + '?pageSize=' + selectedPageSize;
		
		if (searchTerm) {
			url += '&searchTerm=' + encodeURIComponent(searchTerm);
			console.log("url: "+url);
		}
		fetchData(url);
	}


	document.getElementById('pageSize').addEventListener('change', handlePageSizeChange);


	// Call attachPaginationEventListeners() initially
	attachPaginationEventListeners();

});
 


