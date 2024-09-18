jQuery(document).ready(function () {
    document.getElementById('user-menu-toggle').onclick = function () {
        var menu = document.getElementById('mega-menu')
        menu.classList.toggle('hidden');
    }

    window.onclick = function (event) {
        if (!event.target.matches('#user-menu-toggle, #user-menu-toggle *')) {
            var menu = document.getElementById('mega-menu');
            if (!menu.classList.contains('hidden')) {
                menu.classList.add('hidden');
            }
        }
    }
})

