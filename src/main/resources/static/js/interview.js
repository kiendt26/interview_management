$(document).ready(function() {

});
function confirmCancel(postUrl) {
    swal({
        title: "Are you sure?",
        text: "Do you really want to cancel this action?",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, cancel it!",
        cancelButtonText: "No, stay here!",
        closeOnConfirm: false
    }, function(isConfirm) {
        if (isConfirm) {
            const formData = new FormData(document.getElementById('myForm'));
            fetch(postUrl, {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        window.location.href = '@{/list-interview}'; // Chuyển hướng đến trang chính
                    } else {
                        swal("Error!", "There was an error cancelling the action.", "error");
                    }
                })
                .catch(error => console.error('Error:', error));
        }
    });
}