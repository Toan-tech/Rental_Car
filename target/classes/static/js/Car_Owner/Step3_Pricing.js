jQuery(document).ready(function() {
    var specifyCheck = jQuery(".specify-check");
    var specify = jQuery(".specify");
    specifyCheck.change(function () {
        if(specifyCheck.is(":checked")) {
            specify.css("display", "block");
        } else {
            specify.css("display", "none");
        }
    })
})