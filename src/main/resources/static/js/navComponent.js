(function() {
	var navMenu = document.getElementsByTagName("nav")[0];
	navMenu.innerHTML = `
		<a style="font-size:30px; text-decoration:none;">KWIX LITE<a>
		<a href="/">Home</a>
		<a href="/employees">Employees</a>
		<a href="/payroll">Payroll</a>
		<a href="/jobsites">Jobsites</a>
		<a href="/search">Search</a>
		<a href="/logout" class="right">Logout</a>
		<a href="/help" class="right">Help</a>
	`;
})();