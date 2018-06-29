$(document).ready(function() {
    console.log("ready");

    $("#cmd-edit-notes").hide();
    $("#note-box-edit").hide();

    $("#description-box").on("mouseenter", function() {
        $("#cmd-edit-notes").show();
    });

    $("#description-box").on("mouseleave", function() {
        $("#cmd-edit-notes").hide();
    });

    $("#description-box").on("click", function() {

        $("#note-box-edit").show();
        $("#description-box").hide();
    });
});