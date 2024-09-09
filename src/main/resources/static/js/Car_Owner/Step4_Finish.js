jQuery(document).ready(function() {
    // Step 4_Finish
    //Location
    var city = jQuery("#city");
    var district = jQuery("#district");
    var ward = jQuery("#ward");
    var homeNumber = jQuery("#home-number");
    var location = jQuery("#location");

    city.on("change", updateLocation);
    district.on("change", updateLocation);
    ward.on("change", updateLocation);
    homeNumber.on("change", updateLocation);

    function updateLocation(){
        var cityVal = city.val();
        var districtVal = district.val();
        var wardVal = ward.val();
        var homeNumberVal = homeNumber.val();
        var address = [];
        if(cityVal) {
            address.push(cityVal);
        }
        if (districtVal) {
            address.push(districtVal);
        }
        if(wardVal) {
            address.push(wardVal);
        }
        if (homeNumberVal) {
            address.push(homeNumberVal);
        }
        var fullAddress = address.join(" ,");

        location.html("<p>" + fullAddress + "</p>");
    };

    // Base price
    var basePrice = jQuery("#base-price");
    var showBasePrice = jQuery("#show-base-price");
    basePrice.on("change", function () {
        showBasePrice.html("<p>" + basePrice.val() + " k/day</p>");
    })
})