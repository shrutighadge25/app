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
