$(document).ready(function () {
    $('#skillsDTO').select2({
        placeholder: "Select skills",
        tags: true,
    });
});

$(document).ready(function () {
    $('#benefitDTO').select2({
        placeholder: "Select benefit",
        tags: true,
    });
});

$(document).ready(function () {
    $('#levelDTO').select2({
        placeholder: "Select level",
        tags: true,
    });
});
    function validateDates() {
    var startDate = document.getElementById("startDate").value;
    var endDate = document.getElementById("endDate").value;

    if (startDate && endDate) {
    var start = new Date(startDate);
    var end = new Date(endDate);
    if (start >= end) {
    alert("End date must be greater than start date");
    return false; // Ngăn không cho submit form
}
}
    return true; // Cho phép submit form
}


