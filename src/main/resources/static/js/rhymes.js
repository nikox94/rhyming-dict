window.onload = function() { 
	document.getElementById("word-form").onsubmit = function() { 
		showRhymes();
		return false;
	};
};

function showRhymes() {
	query = $("#word-query").val();
	if (query == null || query =="") {
		alert("Моля въведете дума за римуване.");
	}
	$.get( "/word/" + query, function( data ) {
		$("#two-letters").html(data["twoLetterRhymes"]);
		$("#three-letters").html(data["threeLetterRhymes"]);
		});
}