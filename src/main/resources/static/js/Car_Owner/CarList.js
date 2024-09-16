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

    var payments = jQuery(".payment");
    var deposits = jQuery(".deposit");
    var carIndex = jQuery(".car-index")

    var bground = jQuery(".background");
    var popUp = jQuery(".pop-up");
    var displayInput = jQuery(".display-input");
    var depositVal = jQuery(".deposit-value");
    var paymentVal = jQuery(".payment-value");
    var carStatusValue = jQuery(".car-status-value");
    var text = jQuery(".pop-up p");
    var noBtn = jQuery(".no-btn");
    var yesBtn = jQuery(".yes-btn");

    deposits.each(function (index) {
        jQuery(this).on("click", function () {
            displayInput.val(index);
            depositVal.val(jQuery(this).data("id"));
            text.eq(0).text("Confirm deposit");
            text.eq(1).text("Please confirm that you have\n" +
                "receive the deposit this booking.\n" +
                "This will allow the customer to\n" +
                "pick-up the car at the agreed date\n" +
                "and time");
            bground.css("display", "block");
            popUp.css("display", "block");
        })
    })

    payments.each(function (index) {
        jQuery(this).on("click", function () {
            displayInput.val(index);
            carStatusValue.val(carIndex.eq(index).val());
            paymentVal.val(jQuery(this).data("id"));
            text.eq(0).text("Confirm payment");
            text.eq(1).text("Please confirm that you have\n" +
                "receive the payment for this\n" +
                "booking.")
            bground.css("display", "block");
            popUp.css("display", "block");
        })
    })

    noBtn.on("click", function () {
        displayInput.val("");
        depositVal.val("");
        paymentVal.val("");
        carStatusValue.val("");
        bground.css("display", "none");
        popUp.css("display", "none");
    })

    yesBtn.on("click", function () {
        bground.css("display", "none");
        popUp.css("display", "none");
        if (depositVal.val() != ""){
            deposits.eq(displayInput.val()).css("display", "none");
        } else {
            payments.eq(displayInput.val()).css("display", "none");
            status.eq(carStatusValue.val()).text("Available").css("color", "green");
        }
        var data = new FormData();
        data.append("payment", paymentVal.val());
        data.append("deposit", depositVal.val());
        jQuery.ajax({
            url: "http://localhost:8080/bookingstatus",
            type: "POST",
            data: data,
            processData: false,
            contentType: false,
            success: function (response) {
                console.log("Tải lên thành công:", response);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("Lỗi khi tải lên:", textStatus, errorThrown);
            }
        })
        displayInput.val("");
        depositVal.val("");
        paymentVal.val("");
        carStatusValue.val("");
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