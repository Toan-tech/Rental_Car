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

})

jQuery(window).on("load", function () {
    var images = jQuery(".images");
    var carousel = jQuery(".carousel-inner");
    var basePrice = jQuery(".base-price");
    var priceShow = jQuery(".show-price");
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

    var payment = jQuery(".payment");
    var deposit = jQuery(".deposit");

    var bground = jQuery(".background");
    var popUp = jQuery(".pop-up");
    var displayInput = jQuery(".display-input");
    var depositVal = jQuery(".deposit-value");
    var paymentVal = jQuery(".payment-value");
    var text = jQuery(".pop-up p");
    var noBtn = jQuery(".no-btn");
    var yesBtn = jQuery(".yes-btn");

    deposit.on("click", function () {
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

    payment.on("click", function () {
            paymentVal.val(jQuery(this).data("id"));
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

    yesBtn.on("click", function () {
        bground.css("display", "none");
        popUp.css("display", "none");
        if (depositVal.val() != ""){
            deposit.css("display", "none");
        } else {
            payment.css("display", "none");
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