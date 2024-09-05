jQuery(document).ready(function () {
    var infoButton = jQuery(".info-title");
    var frag = jQuery(".frag");

    showInfo(infoButton, frag);

    function showInfo(button, fragment) {
        jQuery.each(button,function (index) {
            jQuery(button[index]).on("click", function () {
                if (index == 0) {
                    jQuery(button[0]).css("border-bottom", "none");
                    jQuery(button[1]).css("border-bottom", "1px solid black");
                    jQuery(button[2]).css("border-bottom", "1px solid black");

                    jQuery(fragment[0]).css("display", "block");
                    jQuery(fragment[1]).css("display", "none");
                    jQuery(fragment[2]).css("display", "none");
                }
                if (index == 1) {
                    jQuery(button[1]).css("border-bottom", "none");
                    jQuery(button[0]).css("border-bottom", "1px solid black");
                    jQuery(button[2]).css("border-bottom", "1px solid black");

                    jQuery(fragment[1]).css("display", "block");
                    jQuery(fragment[0]).css("display", "none");
                    jQuery(fragment[2]).css("display", "none");
                }
                if (index == 2) {
                    jQuery(button[2]).css("border-bottom", "none");
                    jQuery(button[1]).css("border-bottom", "1px solid black");
                    jQuery(button[0]).css("border-bottom", "1px solid black");

                    jQuery(fragment[2]).css("display", "block");
                    jQuery(fragment[1]).css("display", "none");
                    jQuery(fragment[0]).css("display", "none");
                }
            })


        })
    }


})