const navItems = document.querySelectorAll('.nav-item');

// Lặp qua từng mục và gán sự kiện click
navItems.forEach(item => {
    item.addEventListener('click', function() {
        // Xóa class 'active' khỏi tất cả các mục
        navItems.forEach(nav => nav.classList.remove('active'));

        // Thêm class 'active' vào mục vừa click
        this.classList.add('active');
    });
});