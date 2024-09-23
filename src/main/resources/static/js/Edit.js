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

    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    var images = jQuery(".images");
    var carousel = jQuery(".carousel-inner");
    var showBasePrice = jQuery(".base-price");
    var priceShow = jQuery(".show-price");
    var imgs = jQuery(".uploaded-image img");

    let imageArray = images.val().split(", ");
    carousel.html(
        `
            <div class="carousel-item active">
                <img src="${imageArray[3]}" alt="Car1">
            </div>
            <div class="carousel-item">
                <img src="${imageArray[4]}" alt="Car2">
            </div>
            <div class="carousel-item">
                <img src="${imageArray[5]}" alt="Car3">
            </div>
            <div class="carousel-item">
                <img src="${imageArray[6]}" alt="Car4">
            </div>
            `);

    imgs.each(function (index) {
        if (index == 0) {
            jQuery(this).attr("src", imageArray[3]);
        } else if (index == 1) {
            jQuery(this).attr("src", imageArray[4]);
        } else if (index == 2) {
            jQuery(this).attr("src", imageArray[5]);
        } else if (index == 3){
            jQuery(this).attr("src", imageArray[6]);
        }
    })

    let carBasePrice = showBasePrice.val();
    let numberValue = Number(carBasePrice)/1000;
    priceShow.text(numberValue + " k/day");

    //     Payment and deposit button
    var btnPayment = jQuery(".btn-payment");
    var btnDeposit = jQuery(".btn-deposit");
    var bground = jQuery(".background");
    var popUp = jQuery(".pop-up");
    var depositVal = jQuery(".deposit-value");
    var paymentVal = jQuery(".payment-value");
    var text = jQuery(".pop-up p");
    var noBtn = jQuery(".no-btn");

    btnDeposit.on("click", function () {

        depositVal.val(jQuery(this).data("id"));
        console.log(depositVal.val());
        text.eq(0).text("Confirm deposit");
        text.eq(1).text("Please confirm that you have\n" +
            "receive the deposit this booking.\n" +
            "This will allow the customer to\n" +
            "pick-up the car at the agreed date\n" +
            "and time");
        bground.css("display", "block");
        popUp.css("display", "block");
    })

    btnPayment.on("click", function () {
        paymentVal.val(jQuery(this).data("id"));
        console.log(paymentVal.val());
        text.eq(0).text("Confirm payment");
        text.eq(1).text("Please confirm that you have\n" +
            "receive the payment for this\n" +
            "booking.")
        bground.css("display", "block");
        popUp.css("display", "block");
    })

    noBtn.on("click", function () {
        depositVal.val("");
        paymentVal.val("");
        bground.css("display", "none");
        popUp.css("display", "none");
    })

    // Edit-detail
    // Upload image
    var uploadImageContainer = jQuery(".upload-image-container");
    var imageUpload = jQuery(".image-upload-file");
    var imageUploadDefault = jQuery(".image-upload-default");
    var imageUploadedFile = jQuery(".image-uploaded-file");
    var uploadedImage = jQuery(".uploaded-image");
    var removeImageButton = jQuery(".remove-image-button");
    var previewImage = jQuery(".preview-image");
    var data = new FormData();

// ImageUpload
    uploadImageContainer.each(function (index) {
        jQuery(this).click(function () {
            jQuery(imageUploadDefault[index]).click();
        })

        jQuery(this).on("dragover", function (event) {
            event.preventDefault();
            jQuery(this).addClass("dragover");
        })

        jQuery(this).on("dragleave", function (event) {
            event.preventDefault();
            jQuery(this).removeClass("dragover");
        })

        jQuery(this).on("drop", function (event) {
            jQuery(this).removeClass("dragover");
            event.preventDefault();

            // Lấy tệp được kéo và thả
            var files = event.originalEvent.dataTransfer.files;
            if (index == 0) {
                data.set("file3", files[0]);
            } else if (index == 1) {
                data.set("file4", files[0]);
            } else if (index == 2) {
                data.set("file5", files[0]);
            } else if (index == 3) {
                data.set("file6", files[0]);
            }

            var fileType = files[0].type; // Lấy kiểu MIME của file
            var validImageTypes = ["image/jpeg", "image/png", "image/gif", "image/jpg"];
            if ($.inArray(fileType, validImageTypes) < 0) {
                // Nếu kiểu file không hợp lệ
                jQuery($(".errorMessage")[index]).css("display", "block");
                return;
            } else {
                jQuery($(".errorMessage")[index]).css("display", "none");
                showImage(files[0], index);
            }
        })
    });

// Upload bằng click
    imageUploadDefault.each(function (index) {

        // Sự kiện thay đổi khi người dùng chọn file
        jQuery(this).on('change', function () {
            if (index == 0) {
                data.set("file3", this.files[0]);
            } else if (index == 1) {
                data.set("file4", this.files[0]);
            } else if (index == 2) {
                data.set("file5", this.files[0]);
            } else if (index == 3) {
                data.set("file6", this.files[0]);
            }

            // Ẩn errorMessage ( nếu có)
            jQuery($(".errorMessage")[index]).css("display", "none");

            // Hiển thị tên file và button
            jQuery(imageUploadedFile[index]).text(this.files[0].name);
            showImage(this.files[0], index);
            this.value = "";
        })
    })

// Hiển thị ảnh
    function showImage(file, index) {
        // Hiển thị ảnh
        if (file) {
            jQuery(removeImageButton[index]).css("display", "inline-block");
            jQuery(uploadedImage[index]).css("display", "block");
            jQuery(imageUpload[index]).css("display", "none");
            jQuery(imageUploadedFile[index]).html(file.name);

            var reader = new FileReader();
            reader.onload = function (e) {
                jQuery(previewImage[index]).attr("src", e.target.result);
            }
            reader.readAsDataURL(file);
        }
    }

//Xóa file đã tải lên
    removeImageButton.each(function (index) {
        jQuery(this).click(function () {
            if (index == 0) {
                data.set("file3", undefined);
            } else if (index == 1) {
                data.set("file4", undefined);
            } else if (index == 2) {
                data.set("file5", undefined);
            } else if (index == 3) {
                data.set("file6", undefined);
            }

            jQuery(this).css("display", "none");

            jQuery(uploadedImage[index]).css("display", "none");
            jQuery(previewImage[index]).attr("src", "");
            jQuery(imageUpload[index]).css("display", "flex");
            jQuery(imageUploadedFile[index]).html("");
        })
    })

    // Config when page loaded (disable form element)
    function kickOn() {
        uploadImageContainer.each(function (index) {
            jQuery(this).click(function () {
                jQuery(imageUploadDefault[index]).click();
            })

            jQuery(this).on("dragover", function (event) {
                event.preventDefault();
                jQuery(this).addClass("dragover");
            })

            jQuery(this).on("dragleave", function (event) {
                event.preventDefault();
                jQuery(this).removeClass("dragover");
            })

            jQuery(this).on("drop", function (event) {
                jQuery(this).removeClass("dragover");
                event.preventDefault();

                // Lấy tệp được kéo và thả
                var files = event.originalEvent.dataTransfer.files;
                if (index == 0) {
                    data.set("file3", files[0]);
                } else if (index == 1) {
                    data.set("file4", files[0]);
                } else if (index == 2) {
                    data.set("file5", files[0]);
                } else if (index == 3) {
                    data.set("file6", files[0]);
                }

                var fileType = files[0].type; // Lấy kiểu MIME của file
                var validImageTypes = ["image/jpeg", "image/png", "image/gif", "image/jpg"];
                if ($.inArray(fileType, validImageTypes) < 0) {
                    // Nếu kiểu file không hợp lệ
                    jQuery($(".errorMessage")[index]).css("display", "block");
                    return;
                } else {
                    jQuery($(".errorMessage")[index]).css("display", "none");
                    showImage(files[0], index);
                }
            })
        });
    }

    var editBtn = jQuery(".edit-btn");
    var discardBtn = jQuery(".discard-btn");
    var saveBtn = jQuery(".save-btn");
    var select = jQuery("select");
    var input = jQuery("input");
    var checkbox = jQuery("input[type=checkbox]");
    var textArea = jQuery("textarea");

    var carId = jQuery("#carId");
    var address = jQuery("#show-address");
    var description = jQuery("#description");
    var additionalFunction = jQuery("#additional-function");
    var terms = jQuery("#term-of-use");
    var carStatus = jQuery("#status-select")

    select.prop("disabled", true);
    input.prop("readonly", true);
    textArea.prop("readonly", true);
    checkbox.prop("disabled", true);
    uploadImageContainer.off();

    editBtn.on("click", function (){
        discardBtn.css("display", "inline-block");
        saveBtn.css("display", "inline-block");
        editBtn.css("display", "none");
        select.prop("disabled", false);
        input.prop("readonly", false);
        checkbox.prop("disabled", false);
        textArea.prop("readonly", false);
        kickOn();
    })

    saveBtn.on("click", function (){
        var editDetail = validateEditDetail();
        var editPricing = validatePricing();
        if ((editDetail == true) && (editPricing == true)) {
            data.set("carId", carId.val());
            data.set("mileage", mileage.val());
            data.set("fuelConsumption", fuelConsumption.val());
            data.set("address", address.val());
            data.set("description", description.val());
            data.set("additionalFunction", additionalFunction.val());
            data.set("terms", terms.val());
            data.set("basePrice", basePrice.val());
            data.set("deposit", deposit.val());
            data.set("carStatus", carStatus.val());
            jQuery.ajax({
                url: "http://localhost:8080/car-owner/mycar/editdetails",
                type: "POST",
                data: data,
                processData: false,
                contentType: false,
                beforeSend: function(xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);  // Đính kèm CSRF token vào header
                },
                success: function (response) {
                    if (response == "unauthorized") {
                        window.location.href = "/car-owner/mycar";
                    } else {
                        window.location.href = "/car-owner/mycar/edit?carid=" + carId.val();
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.error("Lỗi khi tải lên:", textStatus, errorThrown);
                }
            })
        }
    })

    //validate edit_detail
    var mileage = jQuery("#mileage");
    var mileageWarning = jQuery("#mileage-warning");
    var mileageNumberWarning = jQuery("#mileage-number-warning");
    var city = jQuery("#city");
    var cityWarning = jQuery("#city-warning");
    var district = jQuery("#district");
    var districtWarning = jQuery("#district-warning");
    var ward = jQuery("#ward");
    var wardWarning = jQuery("#ward-warning");
    var fuelConsumption = jQuery("#fuel-consumption");
    var fuelConsumptionWarning = jQuery("#fuel-consumption-warning");
    var frontImageWarning = jQuery("#front-image-warning");
    var backImageWarning = jQuery("#back-image-warning");
    var leftImageWarning = jQuery("#left-image-warning");
    var rightImageWarning = jQuery("#right-image-warning");
    var mileageValidate, cityValidate, districtValidate, wardValidate, fuelConsumptionValidate, frontImageValidate,
        backImageValidate, leftImageValidate, rightImageValidate, editDetailValidated;

    function validateEditDetail() {
        cityValidate = validateValue(city, cityWarning);
        districtValidate = validateValue(district, districtWarning);
        wardValidate = validateValue(ward, wardWarning);
        frontImageValidate = validateImage(imgs.eq(0), frontImageWarning);
        backImageValidate = validateImage(imgs.eq(1), backImageWarning);
        leftImageValidate = validateImage(imgs.eq(2), leftImageWarning);
        rightImageValidate = validateImage(imgs.eq(3), rightImageWarning);

        let regex = /^-?\d+(\.\d+)?$/;
        fuelConsumptionValidate = checkNumber(fuelConsumption, fuelConsumptionWarning, regex);
        mileageValidate = validateValueAndNumber(mileage, mileageWarning, mileageNumberWarning);

        if (mileageValidate == false || cityValidate == false || districtValidate == false || wardValidate == false || fuelConsumptionValidate == false
            || backImageValidate == false || leftImageValidate == false || rightImageValidate == false || frontImageValidate == false
        ) {
            editDetailValidated = false;
        } else {
            editDetailValidated = true;
        }
        return editDetailValidated;
    }

    // validation Step3_Pricing
    var basePrice = jQuery("#base-price");
    var basePriceWarning = jQuery("#base-price-warning");
    var basePriceNumberWarning = jQuery("#base-price-number-warning");
    var deposit = jQuery("#deposit");
    var depositWarning = jQuery("#deposit-warning");
    var depositNumberWarning = jQuery("#deposit-number-warning");
    var depositValidate, basePriceValidate, stepPricingValidated;

    function validatePricing() {
        basePriceValidate = validateValueAndNumber(basePrice, basePriceWarning, basePriceNumberWarning);
        depositValidate = validateValueAndNumber(deposit, depositWarning, depositNumberWarning);

        if (basePriceValidate == false || depositValidate == false) {
            stepPricingValidated = false;
        } else {
            stepPricingValidated = true;
        }
        return stepPricingValidated;
    }

    function validateValue(dom, domWarning) {
        let validate;
        if (dom.val() == "") {
            domWarning.css("display", "block");
            validate = false;
        } else {
            domWarning.css("display", "none");
            validate = true;
        }
        return validate;
    }
    function checkNumber(dom, domWarning, regex) {
        let validate;
        if (dom.val() == "") {
            domWarning.css("display", "none");
            validate = true;
        } else {
            if (dom.val().match(regex)) {
                domWarning.css("display", "none");
                validate = true;
            } else {
                domWarning.css("display", "block");
                validate = false;
            }
        }
        return validate;
    }
    function validateValueAndNumber(dom, domWarning, domNumberWarning) {
        let regex = /^-?\d+(\.\d+)?$/;
        let validate;
        if (dom.val() == "") {
            domWarning.css("display", "block");
            domNumberWarning.css("display", "none");
            validate = false;
        } else {
            if (dom.val().match(regex)) {
                domWarning.css("display", "none");
                domNumberWarning.css("display", "none");
                validate = true;
            } else {
                domWarning.css("display", "none");
                domNumberWarning.css("display", "block");
                validate = false;
            }
        }
        return validate;
    }
    function validateImage(dom, domWarning){
        let validate;
        if(dom.attr("src") == ""){
            validate = false;
            domWarning.css("display", "block");
        } else {
            validate = true;
            domWarning.css("display", "none");
        }
        return validate;
    }
})