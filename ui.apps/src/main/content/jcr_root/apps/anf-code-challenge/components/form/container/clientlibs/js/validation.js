$(function() {
    let $form = $("#exercise-1-form");
    $form.find('.button').after("<div class='result'></div>")
    $form.submit(function( event ) {
        event.preventDefault();
        var $formData = $(this).serialize();
        $.get('/bin/saveUserDetails', $formData)
            .done(function( data ) {
                $form.find('.result').html(data);
            });
    });
});