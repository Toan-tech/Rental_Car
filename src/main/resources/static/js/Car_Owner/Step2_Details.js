// Step 2_Details
jQuery(document).ready(function () {
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

    // Hàm xử lý tải lên tệp
    // function imageUploadFiles(files, index) {
    //     var formData = new FormData();
    //
    //     // Duyệt qua các tệp và thêm vào FormData
    //     jQuery.each(files, function (i, file) {
    //         formData.append("file", file);
    //         console.log(file)
    //         jQuery(imageUploadedFile[index]).html(file.name);
    //     });
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
    //             var fileUrl = response;  // URL của ảnh nhận được từ server
    //
    //             if (jQuery(previewImage[index]).attr("src") == ""){
    //                 // Thêm ảnh mới
    //                 // Hiển thị ảnh trên trang
    //                 jQuery(previewImage[index]).attr("src", fileUrl);
    //                 showAddImage(fileUrl);
    //                 slideNumber = slideNumber + 1;
    //             } else {
    //                 // Nếu đã có ảnh rồi cần xóa ảnh cũ rồi mới thêm ảnh mới
    //                 // Xóa ảnh cũ
    //                 slideNumber = slideNumber - 1;
    //                 removeImageAndShow(jQuery(previewImage[index]).attr("src"));
    //
    //                 // Thêm ảnh mới
    //                 jQuery(previewImage[index]).attr("src", fileUrl);
    //                 showAddImage(fileUrl);
    //                 slideNumber = slideNumber + 1;
    //             }
    //
    //         },
    //         error: function (jqXHR, textStatus, errorThrown) {
    //             console.error("Lỗi khi tải lên:", textStatus, errorThrown);
    //         }
    //     })
    // }
})
