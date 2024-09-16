jQuery(document).ready(function () {
    var document = jQuery(".show-document");
    var registration = jQuery(".registration");
    var certificate = jQuery(".certificate");
    var insurance = jQuery(".insurance");

    var documentArray = document.val().split(", ");
    var registrationName = documentArray[0].split("_")[1];
    registration.text(registrationName);
    registration.attr("href", documentArray[0]);
    registration.attr("download", "");

    var certificateName = documentArray[1].split("_")[1];
    certificate.text(certificateName);
    certificate.attr("href", documentArray[1]);
    certificate.attr("download", "");

    var insuranceName = documentArray[2].split("_")[1];
    insurance.text(insuranceName);
    insurance.attr("href", documentArray[2]);
    insurance.attr("download", "");
})