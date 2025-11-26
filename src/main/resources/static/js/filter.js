let empList = [];
const inputs = document.querySelectorAll("input.filter-input");
const empTable = document.getElementById("filter-tb-emp") || null;
const payrollTable = document.getElementById("filter-tb-payroll") || null;
const stubTable = document.getElementById("filter-tb-stub") || null;
let initRowCount = payrollTable? getRowCount(payrollTable) : 0;

//config for each table
const tableConfig = {
	payroll: {
		table: payrollTable,
		inputLimit: 7,
		columnOffset: 0,
		updateEmpList: true,
		empListColumn: 0,
		empTable: empTable
	},
	emp: {
		table:empTable,
		inputLimit: 6,
		columnOffset: 1,
		updateEmpList: false
	},
	stub: {
		table: stubTable,
		inputLimit: 6,
		columnOffset: 0,
		updateEmpList: false
	}
};

function filterTable(inputs, config) {
	const table = config.table;
	const inputLimit = config.inputLimit;
	const columnOffset = config.columnOffset;
	const updateEmpList = config.updateEmpList;
	const empListColumn = config.empListColumn || 0;
	const empTable = config.empTable || null;


	if(!table){
		console.error("Table could not be found...");
		return;
	}

	//clear empList if updating
	if(updateEmpList){
		empList.length = 0;
	}

	const rows = table.getElementsByTagName("tr");
	for(let i = 1; i < rows.length; i++){ //start at 1 to skip header
		const row = rows[i];
		let shouldDisplay = true;

		for (let j = 0; j < inputs.length; j++){
			if (j === inputLimit){ //break at limit, ignore last column
				break;
			}
			const input = inputs[j];
			const filter = input.value.toUpperCase();
			const cell = row.getElementsByTagName("td")[j + columnOffset];

			if(cell){
				const cellTxt = cell.innerText || cell.textContent;
				if(filter && cellTxt.toUpperCase().indexOf(filter) < 0){
					shouldDisplay = false;
				}
			}
		}
		row.style.display = shouldDisplay ? "" : "none";
	}

	//filter empTable after payrollTable is filtered
	if(updateEmpList && empTable){
		for(let i = 1; i <rows.length; i++){//start at 1 to ignore header
			const stubRow = rows[i];
			const empCell = stubRow.getElementsByTagName("td")[empListColumn]; //emp name is at index 0
			const empName = empCell.innerText || empCell.textContent;
			if(stubRow.style.display !== "none" && !empList.includes(empName)){
				console.log(empName);
				empList.push(empName); //add to empList only when updating
			}
		}
		
		console.log(empList);

		const empRows = empTable.getElementsByTagName("tr");
		for (let i = 1; i < empRows.length; i++){
			let displayEmp = true;
			const empRow = empRows[i];
			const cell = empRow.getElementsByTagName("td")[1];
			const nameCell = cell.textContent || cell.innerText;
			if(!empList.includes(nameCell)){
				displayEmp = false;
			}
			empRow.style.display = displayEmp ? "" : "none";
		}

		const currentRowCount = getRowCount(table);
		if (currentRowCount === initRowCount){
			empList.length = 0;
			for (let i = 1; i < empRows.length; i++){
				empRows[i].style.display = "";
			}
		}
	}
}

function getRowCount(table){
	if(!table){
		return 0;
	}
	
	let count = 0;
	const rows = table.getElementsByTagName("tr");
	for (var i = 1; i < rows.length; i++){
		if(rows[i].style.display !== "none"){
			count++;
		}
	}
	return count;
}

function handleFilter(){
	if(payrollTable){
		filterTable(inputs, tableConfig.payroll);
	}
	else if(empTable){
		filterTable(inputs, tableConfig.emp);
	}
	else if(stubTable){
		filterTable(inputs, tableConfig.stub);
	}
	else {
		console.error("No table found...");
	}
}

document.querySelectorAll("input.filter-input").forEach(input => {
	input.addEventListener("input", handleFilter);
})