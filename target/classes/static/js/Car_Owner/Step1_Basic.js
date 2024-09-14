jQuery(document).ready(function(){

    var automatic = jQuery(".automatic");
    var manual = jQuery(".manual");
    var gasoline = jQuery(".gasoline");
    var diesel = jQuery(".diesel");

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

    // Model và Brand
    let modelsAndBrands = {
        Audi:["Audi1", "Audi2", "Audi3", "Audi4"],
        Mercedes:["Mercedes1", "Mercedes2", "Mercedes3", "Mercedes4"],
        Lamborghini:["Lamborghini1", "Lamborghini2", "Lamborghini3", "Lamborghini4"],
        Ford:["Ford1", "Ford2", "Ford3", "Ford4"]
    }
    var brandName = jQuery("#brand");
    var modelName = jQuery("#model");
    brandName.on("change", function () {
        var selectedBrand = jQuery(this).val();
        var selectModels = modelsAndBrands[selectedBrand];
        modelName.empty();
        modelName.append('<option value="">Choose Model</option>');
        jQuery(selectModels).each(function () {
            modelName.append(`<option value="${this}">${this}</option>`)
        })
    })

    // Color
    let colors = ["Black", "Purple", "Orange", "Blue", "White", "Yellow", "Blue", "Pink"]
    var color = jQuery("#color");
    jQuery(colors).each(function () {
        color.append(`<option value="${this}">${this}</option>`);
    })

    //Year
    let years = [];
    for(var i = 1990; i <= 2024; i++){
        years.push(i);
    }
    var productYear = jQuery("#product-year");
    jQuery(years).each(function () {
        productYear.append(`<option value="${this}">${this}</option>`);
    })

    // No. of Seat
    let NoOfSeat = [4, 7, 9, 16, 30, 50]
    var seatNumber = jQuery("#seat-number");
    jQuery(NoOfSeat).each(function () {
        seatNumber.append(`<option value="${this}">${this}</option>`);
    })

    // Transmission and Fuel
    var automatic = jQuery(".automatic");
    var manual = jQuery(".manual")
    var gasoline = jQuery(".gasoline");
    var diesel = jQuery(".diesel");
    var transmission = jQuery("#transmission");
    var fuel = jQuery("#fuel");
    automatic.click(function () {
        transmission.val("automatic");
    });
    manual.click(function () {
        transmission.val("manual");
    });
    gasoline.click(function () {
        fuel.val("gasoline");
    });
    diesel.click(function () {
        fuel.val("diesel");
    });

    // Hàm xử lý tải lên tệp
    // function uploadFiles(files, index) {
    //     var formData = new FormData();
    //
    //     // Duyệt qua các tệp và thêm vào FormData
    //
    //     formData.append("file", files[0]);
    //     console.log(files[0])
    //     jQuery(uploadedFile[index]).html(files[0].name);
    //
    //     // Gửi yêu cầu Ajax để tải lên
    //     jQuery.ajax({
    //         url: "http://localhost:8080/upload/files",
    //         type: "POST",
    //         data: formData,
    //         processData: false,
    //         contentType: false,
    //         success: function (response) {
    //             console.log("Tải lên thành công:", response);
    //
    //             // Xử lý phản hồi từ server
    //             // var fileUrl = response;  // URL của ảnh nhận được từ server
    //         },
    //         error: function (jqXHR, textStatus, errorThrown) {
    //             console.error("Lỗi khi tải lên:", textStatus, errorThrown);
    //         }
    //     })
    // }

})