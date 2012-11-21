$(document).bind('pageinit', function() {
  $("#accessible-flip").bind("change", function(event, ui) {
    var form = $(this).closest('form');
    $.post(form.attr('action'), form.serialize(), function(data) {
      var image = data.accessible == "true" ? "accessible.png" : "non-accessible.png";
      $('.accessible img').attr('src', "/images/" + image);
    });
    return false;
  });
});
