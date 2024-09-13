jQuery(window).on("load", function () {
    var images = jQuery(".images");
    var carousel = jQuery(".carousel-inner");
    var basePrice = jQuery(".base-price");
    var priceShow = jQuery(".price");
    var status = jQuery(".status");
    var carStatus = jQuery(".car-status");
    var starRate = jQuery(".star-rate");
    var star = jQuery(".star");
    var starCmt = jQuery("small");

    images.each(function (index) {
        let imageArray = jQuery(this).val().split(", ");
        carousel.eq(index).html(
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
            `
        );
        let carBasePrice = basePrice.eq(index).val();
        let numberValue = Number(carBasePrice)/1000;
        priceShow.eq(index).text(numberValue + " k/day");

        if (carStatus.eq(index).val() == "Available") {
            status.eq(index).css("color", "green");
            status.eq(index).text("Available");
        } else if (carStatus.eq(index).val() == "Booked") {
            status.eq(index).css("color", "blue");
            status.eq(index).text("Booked");
        } else if (carStatus.eq(index).val() == "Stopped") {
            status.eq(index).css("color", "red");
            status.eq(index).text("Stopped");
        }

        star.eq(index).html(rate(starRate.eq(index).val()));
        if (starRate.eq(index).val() != 0) {
            starCmt.eq(index).css("visibility", "hidden");
        }
    })

    function rate(star) {
        var innerHtml = "";
        for (var i = 1; i <= 5; i++){
            if (i <= star){
                innerHtml += "<i class='fa-solid fa-star' style='color: yellow'></i>";
            } else {
                innerHtml += "<i class='fa-solid fa-star'></i>";
            }
        }
        return innerHtml;
    }
})