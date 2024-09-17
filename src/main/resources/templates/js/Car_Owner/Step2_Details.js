// Step 2_Details
jQuery(document).ready(function () {
// Select option
    var city = jQuery("#city");
    var district = jQuery("#district");
    var ward = jQuery("#ward");
    // var selectedCity = "";
    // let locationData = {
    //     HaNoi:{
    //         ThanhTri:["HuuThanhOai","TaThanhOai"],
    //         ThanhXuan:["DanPhuong","MyDinh"]
    //     },
    //     HoChiMinh:{
    //         Quan1:["VinhTuy","BaoSon"],
    //         Quan2:["PhucLoc","VanDien"]
    //     }
    // }
    // city.change(function () {
    //     district.empty();
    //     district.append(`<option value="">--- District ---</option>`);
    //     ward.empty();
    //     ward.append(`<option value="">--- Ward ---</option>`);
    //     selectedCity = city.val();
    //     var districtData = locationData[selectedCity] || {};
    //     var districtList = Object.keys(districtData);
    //     jQuery(districtList).each(function () {
    //         district.append(`<option value="${this}">${this}</option>`);
    //     })
    // })
    //
    // district.change(function () {
    //     ward.empty();
    //     ward.append(`<option value="">--- Ward ---</option>`);
    //     var selectedDistrict = district.val();
    //     var wardList = locationData[selectedCity][selectedDistrict];
    //     jQuery(wardList).each(function () {
    //         ward.append(`<option value="${this}">${this}</option>`);
    //     })
    // })

    jQuery.getJSON('https://raw.githubusercontent.com/ThangLeQuoc/vietnamese-provinces-database/master/json/simplified_json_generated_data_vn_units.json', function (data) {
        var cities = data;
        city.empty();
        city.append(`<option value="">--- City/Province ---</option>`);
        jQuery.each(cities, function (index, cityData) {
            city.append(`<option value="${cityData.NameEn}">${cityData.Name}</option>`)
        })

        city.on("change",function () {
            var cityName = jQuery(this).val();
            district.empty();
            ward.empty();
            district.append(`<option value="">--- District ---</option>`);
            ward.append(`<option value="">--- Ward ---</option>`);

            if (cityName) {
                var districts = cities.find(cityData => cityData.NameEn === cityName).District;
                jQuery.each(districts, function (index, districtData) {
                    district.append(`<option value="${districtData.NameEn}">${districtData.Name}</option>`);
                })
            }
        })

        district.on("change", function () {
            var districtName = jQuery(this).val();
            ward.empty();
            ward.append(`<option value="">--- Ward ---</option>`);

            if (districtName) {
                var cityName = city.val();
                var wards = cities.find(cityData => cityData.NameEn === cityName).District.find(districtData => districtData.NameEn === districtName).Ward;
                jQuery.each(wards, function (index, wardData) {
                    ward.append(`<option value="${wardData.NameEn}">${wardData.Name}</option>`);
                })
            }
        })
    })
})
