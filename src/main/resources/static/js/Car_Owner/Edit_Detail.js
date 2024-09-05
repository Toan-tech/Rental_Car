jQuery(window).on("load", function () {
    var upload = jQuery(".upload-file");
    var uploadDefault = jQuery(".upload-default");
    var img = jQuery(".images");

    img.each(function (index) {
        if(jQuery(this).attr("src") == null || jQuery(this).attr("src") == "") {
            jQuery(this).css("display", "none");
            jQuery(upload[index]).css("display", "flex");
        } else {
            jQuery(this).css("display", "block");
            jQuery(upload[index]).css("display", "none");
        }
    })

    upload.each(function (index) {
        jQuery(this).on("click", function () {
            jQuery(uploadDefault[index]).click();
        })

        jQuery(this).on("dragover", function () {
            jQuery(this).addClass("dragover");
        })
        jQuery(this).on("dragleave", function () {
            jQuery(this).removeClass("dragover");
        })
    })
})