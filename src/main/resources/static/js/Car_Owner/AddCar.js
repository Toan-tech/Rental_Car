jQuery(document).ready(function () {
    var back = jQuery(".prev");
    var next = jQuery(".next");
    var steps = jQuery(".step");
    var stepOne = jQuery(".step1")
    var stepTwo = jQuery(".step2")
    var stepThree = jQuery(".step3")
    var stepFour = jQuery(".step4")

    back.css("display", "none");
    next.bind("click", function () {
        jQuery.each(steps, function (i) {
            if (i >= 0 && i < 3){
                back.css("display", "inline-block");
                if (jQuery(steps[i]).hasClass("current") && !jQuery(steps[i]).hasClass("done")) {
                    jQuery(steps[i + 1]).addClass("current");
                    jQuery(steps[i]).removeClass("current").addClass("done");
                    if (i == 0) {
                        stepOne.css("display", "none");
                        stepTwo.css("display", "block");
                    } else if (i == 1) {
                        stepTwo.css("display", "none");
                        stepThree.css("display", "block");
                    } else if (i == 2) {
                        stepThree.css("display", "none");
                        stepFour.css("display", "block");
                    }
                    return false;
                }
            } else {
                return false;
            }

        })
    });
    back.bind("click", function () {
        jQuery.each(steps, function (i) {
            if (jQuery(steps[i]).hasClass("done") && jQuery(steps[i + 1]).hasClass("current")) {
                jQuery(steps[i + 1]).removeClass("current");
                jQuery(steps[i]).removeClass("done").addClass("current");
                if (i == 2) {
                    stepFour.css("display", "none");
                    stepThree.css("display", "block");
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

})