function updateSuggestion(query) {
    if (query.length === 0)
        document.getElementById('suggestList').innerHTML = "";
    else {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (this.readyState === 4 && this.status === 200) {
                var response = this.responseText.split("\n");
                document.getElementById('suggestList').innerHTML = "";
                console.log(response);
                for (var i in response) {
                    document.getElementById('suggestList').innerHTML += "<option value=\"" + response[i] + "\"></option>";
                }
            }
        };
        xhr.open("GET", "searchSuggestion?q=" + query, true);
        xhr.send();
    }
}

function updateCityState(query) {
    if (query.length >= 3) {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (this.readyState === 4 && this.status === 200) {
                console.log(this.responseText);
                if (this.responseText.length > 0) {
                    var response = this.responseText.split(",");
                    document.getElementById('city').value = response[0];
                    document.getElementById('state').value = response[1];
                }
            }
        };
        xhr.open("GET", "addressUpdate?q=" + query, true);
        xhr.send();
    }
}
