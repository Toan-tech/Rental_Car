jQuery(document).ready(function () {
    var specifyCheck = jQuery(".specify-check");
    var specify = jQuery(".specify");
    var otherSpecify = jQuery(".specify textarea");
    var termOfUse = jQuery(".term-of-use");
    var termOfUseStr;

    specifyCheck.change(function () {
        if (specifyCheck.is(":checked")) {
            specify.css("display", "block");
        } else {
            specify.css("display", "none");
            otherSpecify.val("");
        }
    })

    if (termOfUse.val().length > 10) {
        termOfUseStr = termOfUse.val().split("_")[0];
        otherSpecify.val(termOfUse.val().substring(11));
    } else {
        termOfUseStr = termOfUse.val();
    }

    var termOfUseArray = termOfUseStr.split(", ");
    jQuery(".term").each(function () {
        var checkboxValue = jQuery(this).val();
        if (termOfUseArray.includes(checkboxValue)) {
            jQuery(this).prop("checked", true);
        }
    });

    if (specifyCheck.is(":checked")) {
        specify.css("display", "block");
    } else {
        specify.css("display", "none");
    }

    jQuery(".term").each(function () {
        jQuery(this).change(function () {
            var terms = updateTerms(jQuery(".term"));
            termOfUse.val(terms);
            console.log(termOfUse.val());
        })
    })

    otherSpecify.change(function () {
        var terms = updateTerms(jQuery(".term"));
        termOfUse.val(terms);
        console.log(termOfUse.val());
    })

    function updateTerms(dom) {
        var terms = "";
        dom.each(function () {
            if (jQuery(this).is(":checked")) {
                terms = terms + jQuery(this).val() + ", " ;
            } else {
                terms = terms + "0, ";
            }
        })
        terms = terms.slice(0, -2);
        if (otherSpecify.val() != ""){
            terms = terms + "_" + otherSpecify.val();
        }
        return terms;
    }
})