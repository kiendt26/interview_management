$(document).ready(function() {
    $('#assignLink').on('click', function(event) {
        event.preventDefault(); // Ngăn chặn hành động mặc định

        const selectElement = $('select[name="recruiter.userId"]'); // Lấy dropdown
        const userId = $(this).data('user-id'); // Lấy ID người dùng từ thuộc tính data-user-id

        // Tìm option theo giá trị
        // const option = selectElement.find(`option[value="${userId}"]`);


        selectElement.val(userId);

    });

    $('#cancelSchedule').on('click', function(event) {
        event.preventDefault(); // Ngăn chặn hành động mặc định

        // Lấy giá trị từ input ẩn
        const scheduleId = $('#scheduleId').val();


        const confirmation = confirm("Are you sure you want to cancel this interview?");
        if (confirmation) { // Kiểm tra nếu scheduleId hợp lệ
            window.location.href = `/interview/cancel?id=${scheduleId}`; // Chuyển hướng nếu xác nhận
        }
    });

    // xử lys chon interviewer
    // $("form").on("submit", function() {
    //     const interviewers = $("#skillSelectTag");
    //     const errorDiv = $("#interviewer-error");
    //     errorDiv.text(""); // Reset lỗi
    //
    //     // Kiểm tra xem có ít nhất một interviewer được chọn
    //     if (interviewers[0].selectedOptions.length === 0) {
    //         errorDiv.text("At least one interviewer must be selected.");
    //         return false; // Ngăn không cho gửi form
    //     }
    //
    //     return true; // Cho phép gửi form
    // });

});
