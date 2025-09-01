function showPage(pageId) {
  // Hide all pages
  const pages = document.querySelectorAll('.page');
  pages.forEach(page => page.style.display = 'none');

  // Show the selected page
  document.getElementById(pageId).style.display = 'block';
}

function savePage(pageId) {
  // Function to handle saving of data
  alert('Data saved for ' + pageId);
  // Here you can add code to save data, e.g., send it to a server
}

function discardChanges() {
  // Function to handle discarding changes
  alert('Changes discarded');
  // Here you can add code to revert changes if needed
}

// Optionally, show the first page by default

document.getElementById('cpTrialDropdown').addEventListener('change', function() {
  var modal = document.getElementById('cpModal');
  if (this.value === 'yes') {
      modal.style.display = 'block';
  } else {
      modal.style.display = 'none';
  }
});

document.getElementById('addRowButton').addEventListener('click', function() {
  var tableBody = document.getElementById('cpTableBody');
  var newRow = document.createElement('tr');
  newRow.innerHTML = `
      <td><input type="text" name="cpLayout[]" /></td>
      <td><input type="text" name="coCustomerName[]" /></td>
  `;
  tableBody.appendChild(newRow);
});

// Function to save form data
function saveForm() {
  const form = document.getElementById('kickoff-form');
  const formData = new FormData(form);

  // Create an object to store form data
  let data = {};
  formData.forEach((value, key) => {
      data[key] = value;
  });

  // Log the data to the console (you might want to send it to a server or save it somewhere)
  console.log('Form Data Saved:', data);

  // Optional: Show a message to the user
  alert('Form data has been saved!');
}

// Function to discard form data
function discardForm() {
  const form = document.getElementById('kickoff-form');
  
  // Clear the form
  form.reset();

  // Optional: Show a message to the user
  alert('Form data has been discarded!');
}
