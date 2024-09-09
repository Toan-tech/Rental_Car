// Step 2_Details
jQuery(document).ready(function () {
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

// Hàm xử lý tải lên tệp
//     function imageUploadFiles(files, index) {
//         var formData = new FormData();
//
//         // Duyệt qua các tệp và thêm vào FormData
//         jQuery.each(files, function (i, file) {
//             formData.append("file", file);
//             console.log(file)
//             jQuery(imageUploadedFile[index]).html(file.name);
//         });
//
//         // Gửi yêu cầu Ajax để tải lên
//         jQuery.ajax({
//             url: "http://localhost:8080/upload/files",
//             type: "POST",
//             data: formData,
//             processData: false,
//             contentType: false,
//             success: function (response) {
//                 console.log("Tải lên thành công:", response);
//
//                 // Xử lý phản hồi từ server
//                 var fileUrl = response;  // URL của ảnh nhận được từ server
//
//                 if (jQuery(previewImage[index]).attr("src") == ""){
//                     // Thêm ảnh mới
//                     // Hiển thị ảnh trên trang
//                     jQuery(previewImage[index]).attr("src", fileUrl);
//                     showAddImage(fileUrl);
//                     slideNumber = slideNumber + 1;
//                 } else {
//                     // Nếu đã có ảnh rồi cần xóa ảnh cũ rồi mới thêm ảnh mới
//                     // Xóa ảnh cũ
//                     slideNumber = slideNumber - 1;
//                     removeImageAndShow(jQuery(previewImage[index]).attr("src"));
//
//                     // Thêm ảnh mới
//                     jQuery(previewImage[index]).attr("src", fileUrl);
//                     showAddImage(fileUrl);
//                     slideNumber = slideNumber + 1;
//                 }
//
//             },
//             error: function (jqXHR, textStatus, errorThrown) {
//                 console.error("Lỗi khi tải lên:", textStatus, errorThrown);
//             }
//         })
//     }

// Upload bằng click
    imageUploadDefault.each(function (index) {

        // Sự kiện thay đổi khi người dùng chọn file
        jQuery(this).on('change', function () {
            let file = this.files[0];

            // Ẩn errorMessage ( nếu có)
            jQuery($(".errorMessage")[index]).css("display", "none");

            // Hiển thị tên file và button

            jQuery(imageUploadedFile[index]).text(file.name);
            this.value = "";

            showImage(file, index);
        })
    })

    // Hiển thị ảnh
    function showImage(file, index) {
        // Hiển thị ảnh
        if (file){
            jQuery(removeImageButton[index]).css("display", "inline-block");
            jQuery(uploadedImage[index]).css("display", "block");
            jQuery(imageUpload[index]).css("display", "none");
            jQuery(imageUploadedFile[index]).html(file.name);
            var reader = new FileReader();
            reader.onload = function (e) {

                if (jQuery(previewImage[index]).attr("src") == ""){
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
    function showAddImage(url){
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

    function removeImageAndShow(removeUrl){
        jQuery(`li[data-slide-to="${slideNumber}"]`).remove();
        if ((jQuery(`.carousel-inner .carousel-item`).length > 1) && jQuery(`.carousel-item img[src="${removeUrl}"]`).closest("div.carousel-item").hasClass("active")){
            jQuery(`.carousel-item img[src="${removeUrl}"]`).closest("div.carousel-item.active").remove();
            jQuery(`.carousel-inner .carousel-item`).eq(0).addClass("active");
        } else {
            jQuery(`.carousel-item img[src="${removeUrl}"]`).closest("div.carousel-item").eq(0).remove();
        }
    }

// Select option
    var city = jQuery("#city");
    var district = jQuery("#district");
    var ward = jQuery("#ward");
    var selectedCity = "";
    let locationData = {
        HaNoi:{
            ThanhTri:["HuuThanhOai","TaThanhOai"],
            ThanhXuan:["DanPhuong","MyDinh"]
        },
        HoChiMinh:{
            Quan1:["VinhTuy","BaoSon"],
            Quan2:["PhucLoc","VanDien"]
        }
    }
    city.change(function () {
        district.empty();
        district.append(`<option value="">--- District ---</option>`);
        ward.empty();
        ward.append(`<option value="">--- Ward ---</option>`);
        selectedCity = city.val();
        var districtData = locationData[selectedCity] || {};
        var districtList = Object.keys(districtData);
        jQuery(districtList).each(function () {
            district.append(`<option value="${this}">${this}</option>`);
        })
    })

    district.change(function () {
        ward.empty();
        ward.append(`<option value="">--- Ward ---</option>`);
        var selectedDistrict = district.val();
        var wardList = locationData[selectedCity][selectedDistrict];
        jQuery(wardList).each(function () {
            ward.append(`<option value="${this}">${this}</option>`);
        })
    })
})
