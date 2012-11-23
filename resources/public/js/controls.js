var wheelcrowd = wheelcrowd || {};
wheelcrowd.controls = function() {

  var accessibleChange = function() {
    $("#accessible-radio").bind("change", function(event, ui) {
      var form = $(this).closest('form');
      $.post(form.attr('action'), form.serialize(), function(data) {
        var image = data.accessible == "true" ? "accessible.png" : "non-accessible.png";
        $('.accessible img').attr('src', "/images/" + image);
      });
      return false;
    });
  };

  var autocomplete = function() {
    $(".categories_label").hide();
    $("#search-basic").autocomplete({
      source: "/categories",
      target: $("#suggestions"),
      minLength: 3,
      link: "/venues?lat=" + $('#lat').attr('value') + "&lon=" + $('#lon').attr('value') + "&categoryId=",
      onLoadingFinished: function() { $(".categories_label").show() }
    });
  };

  return {
    autocomplete: autocomplete,
    accessibleChange: accessibleChange
  }

}();

$(document).bind('pageinit', function() {
  wheelcrowd.geoLocation.locate();
  wheelcrowd.controls.accessibleChange();
});
