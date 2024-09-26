$('document').ready(function(){

    $('#assignLink').on('click', function(event) {
        event.preventDefault(); // Ngăn chặn hành động mặc định

        const selectElement = $('select[name="recruiter.userId"]'); // Lấy dropdown
        const userId = $(this).data('user-id'); // Lấy ID người dùng từ thuộc tính data-user-id

        // Tìm option theo giá trị
        // const option = selectElement.find(`option[value="${userId}"]`);


        selectElement.val(userId);

    });

    var $file = $('#file-input'),
        $label = $file.next('label'),
        $labelText = $label.find('span'),
        $labelRemove = $('i.remove'),
        labelDefault = $labelText.text();

    // on file change
    $file.on('change', function(event){
        var fileName = $file.val().split( '\\' ).pop();
        if( fileName ){
            console.log($file)
            $labelText.text(fileName);
            $labelRemove.show();
        }else{
            $labelText.text(labelDefault);
            $labelRemove.hide();
        }
    });

    // Remove file
    $labelRemove.on('click', function(event){
        $file.val("");
        $labelText.text(labelDefault);
        $labelRemove.hide();
        console.log($file)
    });
})

function confirmDelete(url) {
    swal({
        title: "Are you sure?",
        text: "Once deleted, you will not be able to recover this candidate!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "No, cancel!",
        closeOnConfirm: false
    }, function(isConfirm) {
        if (isConfirm) {
            window.location.href = url;
        }
    });
    }

function showFileName() {
    const fileInput = document.getElementById('attachment');
    const fileNameSpan = document.getElementById('fileName');

    if (fileInput.files.length > 0) {
        fileNameSpan.textContent = fileInput.files[0].name;
    } else {
        fileNameSpan.textContent = '';
    }
}


function removeFile() {
    const fileInput = document.getElementById('attachment');
    const fileNameSpan = document.getElementById('fileName');

    // Xóa file đã chọn
    fileInput.value = '';
    fileNameSpan.textContent = '';
}