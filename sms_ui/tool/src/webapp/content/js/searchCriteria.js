function clearSearchCriteria(){
	document.getElementById('searchCriteria::id').value = '';
	document.getElementById('searchCriteria::tool-name').value = '';
	document.getElementById('searchCriteria::sender').value = '';
	document.getElementById('searchCriteria::task-status-selection').value = 'All';
	resetDates();
}

function resetDates(){
	document.getElementById('searchCriteria::date-from').value = "";
	document.getElementById('searchCriteria::date-to').value = "";
}

function getToday(){
	var now = new Date();
	var y = now.getFullYear();
	var m = now.getMonth() + 1;
	var d = now.getDate();
	var dateString = m + '/' + d + '/' + y; 
	return dateString;
}