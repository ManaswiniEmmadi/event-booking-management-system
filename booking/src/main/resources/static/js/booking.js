document.getElementById('bookingForm').addEventListener('submit', function (e) {
  e.preventDefault();

  const booking = {
    eventId: document.getElementById('eventId').value,
    userId: document.getElementById('userId').value,
    numberOfSeats: document.getElementById('seats').value,
    status: 'confirmed'
  };

  fetch('/api/bookings', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(booking)
  })
    .then(response => response.json())
    .then(data => {
      alert('Booking successful!');
      console.log(data);
    })
    .catch(error => {
      alert('Booking failed.');
      console.error('Error:', error);
    });
});
