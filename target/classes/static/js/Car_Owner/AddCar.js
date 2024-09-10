jQuery(document).ready(function () {
    var back = jQuery(".prev");
    var next = jQuery(".next");
    var submit = jQuery(".submit");
    var formData = document.getElementsByClassName("form-data")[0];

    var steps = jQuery(".step");
    var stepOne = jQuery(".step1")
    var stepTwo = jQuery(".step2")
    var stepThree = jQuery(".step3")
    var stepFour = jQuery(".step4")

    // Step1
    var upload = jQuery(".upload-file");
    var uploadDefault = jQuery(".upload-default");
    var uploadedFile = jQuery(".uploaded-file");
    var removeDocumentButton = jQuery(".remove-document-button");

    let file1, file2, file3, file4, file5, file6, file7;

    // Xử lý upload file
    upload.each(function (index) {
        jQuery(this).click(function () {
            jQuery(uploadDefault[index]).click();
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
                file1 = files[0];
            } else if (index == 1) {
                file2 = files[0];
            } else if (index == 2) {
                file3 = files[0];
            }

            var fileType = files[0].name.split('.').pop().toLowerCase(); // Lấy kiểu của file
            var validImageTypes = ["doc", "docx", "pdf", "jpg", "jpeg", "png"];
            if ($.inArray(fileType, validImageTypes) < 0) {
                // Nếu kiểu file không hợp lệ
                jQuery($(".documentErrorMessage")[index]).css("display", "block");
                return;
            } else {
                upFile(files[0], index);
            }
        })
    });

    // Upload bằng click
    uploadDefault.each(function (index) {
        // Sự kiện thay đổi khi người dùng chọn file
        jQuery(this).on('change', function () {
            if (index == 0) {
                file1 = this.files[0];
            } else if (index == 1) {
                file2 = this.files[0];
            } else if (index == 2) {
                file3 = this.files[0];
            }
            upFile(this.files[0], index);
            this.value = "";
        })
    });

    // Đọc file
    function upFile(file, index) {

        // Ẩn errorMessage (nếu có)
        jQuery($(".documentErrorMessage")[index]).css("display", "none");

        // Hiển thị tên file và button
        jQuery(removeDocumentButton[index]).css("display", "inline-block");
        jQuery(uploadedFile[index]).text(file.name);

        // Ẩn nút upfile
        jQuery(upload[index]).css("display", "none");
    }

    //Xóa file đã tải lên
    removeDocumentButton.each(function (index) {
        jQuery(this).click(function () {

            jQuery(this).css("display", "none");
            jQuery(uploadedFile[index]).text("");
            jQuery(upload[index]).css("display", "flex");
            if (index == 0) {
                file1 = undefined;
            } else if (index == 1) {
                file2 = undefined;
            } else if (index == 2) {
                file3 = undefined;
            }
        })
    })

    // Validate
    var license = jQuery("#license");
    var licenseWarning = jQuery("#license-warning");
    var color = jQuery("#color");
    var colorWarning = jQuery("#color-warning");
    var brand = jQuery("#brand");
    var brandWarning = jQuery("#brand-warning");
    var model = jQuery("#model");
    var modelWarning = jQuery("#model-warning");
    var productionYear = jQuery("#production-year");
    var productionYearWarning = jQuery("#production-year-warning");
    var seatNumber = jQuery("#seat-number");
    var seatNumberWarning = jQuery("#seat-number-warning");
    var transmission = jQuery("#transmission");
    var transmissionWarning = jQuery("#transmission-warning");
    var fuel = jQuery("#fuel");
    var fuelWarning = jQuery("#fuel-warning");
    var registrationWarning = jQuery("#registration-warning");
    var certificateWarning = jQuery("#certificate-warning");
    var insuranceWarning = jQuery("#insurance-warning");
    var licenseValidate, colorValidate, brandValidate, modelValidate, productionYearValidate,
        seatNumberValidate, transmissionValidate, fuelValidate, registrationValidate, certificateValidate,
        insuranceValidate, stepBasicValidated = false;

    function validateStepBasic() {
        // license validate
        if (license.val() == "") {
            licenseWarning.text("");
            licenseWarning.text("*License is required");
            licenseValidate = false;
        } else if (license.val() != "") {
            let licenseData = new FormData();
            let licenseName = license.val().split("/").pop();
            licenseData.append("license", licenseName);
            jQuery.ajax({
                url: "http://localhost:8080/mycar/validatelicense",
                type: "POST",
                data: licenseData,
                processData: false,
                contentType: false,
                success: function (response) {
                    if (response == true) {
                        licenseWarning.text("");
                        licenseValidate = true;
                    } else {
                        licenseWarning.text("");
                        licenseWarning.text("*Your license is not match. Check again!");
                        licenseValidate = false;
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.error("Error: ", textStatus, errorThrown);
                }
            })
        }

        // color validate
        if (color.val() == "") {
            colorWarning.text("*Color is required");
            colorValidate = false;
        } else {
            colorWarning.text("");
            colorValidate = true;
        }

        // brand validate
        if (brand.val() == "") {
            brandWarning.text("*Brand is required");
            brandValidate = false;
        } else {
            brandWarning.text("");
            brandValidate = true;
        }

        // model validate
        if (model.val() == "") {
            modelWarning.text("*Model is required");
            modelValidate = false;
        } else {
            modelWarning.text("");
            modelValidate = true;
        }

        // production Year validate
        if (productionYear.val() == "") {
            productionYearWarning.text("*Product year is required");
            productionYearValidate = false;
        } else {
            productionYearWarning.text("");
            productionYearValidate = true;
        }

        // Seat Number validate
        if (seatNumber.val() == "") {
            seatNumberWarning.text("*Seat Number is required");
            seatNumberValidate = false;
        } else {
            seatNumberWarning.text("");
            seatNumberValidate = true;
        }

        // Transmission validate
        if (transmission.val() == "") {
            transmissionWarning.text("*Transmission is required");
            transmissionValidate = false;
        } else {
            transmissionWarning.text("");
            transmissionValidate = true;
        }

        // Fuel validate
        if (fuel.val() == "") {
            fuelWarning.text("*Fuel is required");
            fuelValidate = false;
        } else {
            fuelWarning.text("");
            fuelValidate = true;
        }

        // Registration validate
        if (file1 == undefined) {
            registrationWarning.text("*Registration is required");
            registrationValidate = false;
        } else {
            registrationWarning.text("");
            registrationValidate = true;
        }

        // Certificate validate
        if (file2 == undefined) {
            certificateWarning.text("*Certificate is required");
            certificateValidate = false;
        } else {
            certificateWarning.text("");
            certificateValidate = true;
        }

        // Insurance validate
        if (file3 == undefined) {
            insuranceWarning.text("*Insurance is required");
            insuranceValidate = false;
        } else {
            insuranceWarning.text("");
            insuranceValidate = true;
        }

        if (
            licenseValidate == false || colorValidate == false || brandValidate == false || modelValidate == false
            || productionYearValidate == false || seatNumberValidate == false || transmissionValidate == false
            || fuelValidate == false || registrationValidate == false || certificateValidate == false
            || insuranceValidate == false
        ) {
            stepBasicValidated = false;
        } else {
            stepBasicValidated = true;
        }
    }

    // Step2
    var uploadImageContainer = jQuery(".upload-image-container");
    var imageUpload = jQuery(".image-upload-file");
    var imageUploadDefault = jQuery(".image-upload-default");
    var imageUploadedFile = jQuery(".image-uploaded-file");
    var uploadedImage = jQuery(".uploaded-image");
    var removeImageButton = jQuery(".remove-image-button");
    var previewImage = jQuery(".preview-image");

// Carousel
    var carouselIndicators = jQuery(".carousel-indicators");
    var carouselInner = jQuery(".carousel-inner");
    var slideNumber = 0;

// ImageUpload and Show (show at step 4)
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
                file4 = files[0];
            } else if (index == 1) {
                file5 = files[0];
            } else if (index == 2) {
                file6 = files[0];
            } else if (index == 3) {
                file7 = files[0];
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
                file4 = this.files[0];
            } else if (index == 1) {
                file5 = this.files[0];
            } else if (index == 2) {
                file6 = this.files[0];
            } else if (index == 3) {
                file7 = this.files[0];
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

                if (jQuery(previewImage[index]).attr("src") == "") {
                    // Thêm ảnh mới
                    // Hiển thị ảnh trên trang
                    jQuery(previewImage[index]).attr("src", e.target.result);
                    showAddImage(e.target.result);
                    slideNumber = slideNumber + 1;
                } else {
                    // Nếu đã có ảnh rồi cần xóa ảnh cũ rồi mới thêm ảnh mới
                    // Xóa ảnh cũ
                    slideNumber = slideNumber - 1;
                    removeImageAndShow(jQuery(previewImage[index]).attr("src"));

                    // Thêm ảnh mới
                    jQuery(previewImage[index]).attr("src", e.target.result);
                    showAddImage(e.target.result);
                    slideNumber = slideNumber + 1;
                }
            }
            reader.readAsDataURL(file);
        }
    }

//Xóa file đã tải lên
    removeImageButton.each(function (index) {
        jQuery(this).click(function () {

            let removeImageUrl = jQuery(previewImage[index]).attr("src");
            slideNumber = slideNumber - 1;
            removeImageAndShow(removeImageUrl);

            jQuery(this).css("display", "none");

            jQuery(uploadedImage[index]).css("display", "none");
            jQuery(previewImage[index]).attr("src", "");
            jQuery(imageUpload[index]).css("display", "flex");
            jQuery(imageUploadedFile[index]).html("");
        })
    })

// Carousel
    function showAddImage(url) {
        if (slideNumber == 0) {
            carouselIndicators.append(
                `<li data-target="#demo" data-slide-to="${slideNumber}" class="active"></li>`
            );
            carouselInner.append(
                `
                    <div class="carousel-item active">
                        <img src="${url}" alt="Car${slideNumber}">
                    </div>
                `
            )
        } else {
            carouselIndicators.append(
                `<li data-target="#demo" data-slide-to="${slideNumber}"></li>`
            );
            carouselInner.append(
                `
                    <div class="carousel-item">
                        <img src="${url}" alt="Car${slideNumber}">
                    </div>
                `
            )
        }
    }

    function removeImageAndShow(removeUrl) {
        jQuery(`li[data-slide-to="${slideNumber}"]`).remove();
        if ((jQuery(`.carousel-inner .carousel-item`).length > 1) && jQuery(`.carousel-item img[src="${removeUrl}"]`).closest("div.carousel-item").hasClass("active")) {
            jQuery(`.carousel-item img[src="${removeUrl}"]`).closest("div.carousel-item.active").remove();
            jQuery(`.carousel-inner .carousel-item`).eq(0).addClass("active");
        } else {
            jQuery(`.carousel-item img[src="${removeUrl}"]`).closest("div.carousel-item").eq(0).remove();
        }
    }

    // Arrow flow
    back.css("display", "none");
    next.on("click", function () {
        jQuery.each(steps, function (i) {
                back.css("display", "inline-block");
                if (jQuery(steps[i]).hasClass("current") && !jQuery(steps[i]).hasClass("done")) {
                    if (i == 0) {
                        validateStepBasic();
                        if (stepBasicValidated == true) {
                            jQuery(steps[i + 1]).addClass("current");
                            jQuery(steps[i]).removeClass("current").addClass("done");
                            stepOne.css("display", "none");
                            stepTwo.css("display", "block");
                        }
                    } else if (i == 1) {
                        jQuery(steps[i + 1]).addClass("current");
                        jQuery(steps[i]).removeClass("current").addClass("done");
                        stepTwo.css("display", "none");
                        stepThree.css("display", "block");
                    } else if (i == 2) {
                        jQuery(steps[i + 1]).addClass("current");
                        jQuery(steps[i]).removeClass("current").addClass("done");
                        stepThree.css("display", "none");
                        stepFour.css("display", "block");
                        next.css("display", "none");
                        submit.css("display", "inline-block");
                    }
                    return false;
                }
        })
    });
    back.on("click", function () {
        jQuery.each(steps, function (i) {
            if (jQuery(steps[i]).hasClass("done") && jQuery(steps[i + 1]).hasClass("current")) {
                jQuery(steps[i + 1]).removeClass("current");
                jQuery(steps[i]).removeClass("done").addClass("current");
                if (i == 2) {
                    stepFour.css("display", "none");
                    stepThree.css("display", "block");
                    next.css("display", "inline-block");
                    submit.css("display", "none");
                } else if (i == 1) {
                    stepThree.css("display", "none");
                    stepTwo.css("display", "block");
                } else if (i == 0) {
                    stepTwo.css("display", "none");
                    stepOne.css("display", "block");
                    back.css("display", "none");
                }
                return false;
            }
        })
    });

    submit.click(function (e) {
        e.preventDefault();
        var selectedFile = [];
        selectedFile.push(file1, file2, file3, file4, file5, file6, file7);
        var fileData = new FormData(formData);
        jQuery(selectedFile).each(function (index) {
            fileData.append("file" + index, this);
        })

        jQuery.ajax({
            url: "http://localhost:8080/mycar/add",
            type: "POST",
            data: fileData,
            processData: false,
            contentType: false,
            success: function (response) {
                console.log("Tải lên thành công:", response);
                window.location.href = "/addcar";
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Lỗi khi tải lên:", textStatus, errorThrown);
            }
        })
    })
})