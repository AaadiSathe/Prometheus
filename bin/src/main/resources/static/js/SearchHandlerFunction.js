
function handleSubmit(event, tableId){
    event.preventDefault();

    var formData = new FormData(this);

    var queryString = new URLSearchParams(formData).toString();

    fetch(this.getAttribute('action') + '?' + queryString, {
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
        console.error("Error :", error);
    })
}


