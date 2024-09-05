jQuery(document).ready(function () {
    var upload = $(".upload-file");
    var uploadDefault = $(".upload-default");

    upload.each(function (index) {
        jQuery(this).click(function () {
            jQuery(uploadDefault[index]).click();
        })

        jQuery(this).on("dragover", function (e) {
            e.preventDefault();
            jQuery(this).addClass("dragover");
        })

        jQuery(this).on("dragleave", function () {
            jQuery(this).removeClass("dragover");
        })
    })
})
