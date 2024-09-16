jQuery(window).on("load", function () {

    // Select option
    var city = jQuery("#city");
    var district = jQuery("#district");
    var ward = jQuery("#ward");
    var house = jQuery("#houseNumber");
    var selectedCity = "";
    let locationData = {
        HaNoi: {
            ThanhTri: ["HuuThanhOai", "TaThanhOai"],
            ThanhXuan: ["DanPhuong", "MyDinh"]
        },
        HoChiMinh: {
            Quan1: ["VinhTuy", "BaoSon"],
            Quan2: ["PhucLoc", "VanDien"]
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

    var showAddress = jQuery(".show-address");
    var addressArray = showAddress.val().split(", ");
    city.val(addressArray[0]).change();
    district.val(addressArray[1]).change();
    ward.val(addressArray[2]);
    if (addressArray.length = 4) {
        house.val(addressArray[3]);
    }

    updateAddress(ward);
    updateAddress(house);

    function updateAddress(dom) {
        dom.change(function () {
            if (house.val() == "") {
                showAddress.val(city.val() + ", " + district.val() + ", " + ward.val());
            } else {
                showAddress.val(city.val() + ", " + district.val() + ", " + ward.val() + ", " + house.val());
            }
            console.log(showAddress.val());
        })
    }

    //Checkbox additional function
    var additionalFunction = jQuery(".show-add-function");
    var selectedFunctions = additionalFunction.val().split(", ");
    jQuery("input[name='function']").each(function () {
        var checkboxValue = jQuery(this).val();
        if (selectedFunctions.includes(checkboxValue)) {
            jQuery(this).prop("checked", true);
        }
    })

    jQuery("input[name='function']").each(function () {
        jQuery(this).change(function () {
            additionalFunction.val("");
            var allFunctions = "";
            jQuery("input[name='function']:checked").each(function () {
                allFunctions += jQuery(this).val() + ", ";
            })
            if (allFunctions !== "") {
                allFunctions = allFunctions.slice(0, -2);
            }
            additionalFunction.val(allFunctions);
            // console.log(additionalFunction.val());
        })
    })
})