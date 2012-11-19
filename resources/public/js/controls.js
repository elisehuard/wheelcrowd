$(document).bind('pageinit', function() {
  $("#accessible-flip").live("change", function(eventType, ui) {
    console.log('changing');
    $(this).closest('form').submit();
  });
});
