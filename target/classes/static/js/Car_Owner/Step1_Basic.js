jQuery(document).ready(function(){
    var upload = jQuery(".upload-file");
    var uploadDefault = jQuery(".upload-default");
    var automatic = jQuery(".automatic");
    var manual = jQuery(".manual");
    var gasoline = jQuery(".gasoline");
    var diesel = jQuery(".diesel");

    upload.each(function (index){
        jQuery(this).click(function (){
            jQuery(uploadDefault[index]).click();
        })

        jQuery(this).on("dragover", function () {
            jQuery(this).addClass("dragover");
            jQuery(this).html("<i class='fa-solid fa-upload'></i>");
        })

        jQuery(this).on("dragleave", function() {
            jQuery(this).removeClass("dragover");
            jQuery(this).html(
                `
                        <i class= "fa-solid fa-upload"></i>
                        <p>Drag and drop</p>
                        <p>OR</p>
                        <p>Select file</p>
                    `
        );
        })
    });
    chosen(automatic, manual);
    chosen(gasoline, diesel);

    function chosen(type1, type2) {
        type1.on("click",function (){
            type1.addClass("chosen");
            type2.removeClass("chosen");
        });
        type2.on("click",function (){
            type2.addClass("chosen");
            type1.removeClass("chosen");
        });
    }


})