var wheelcrowd = wheelcrowd || {};
wheelcrowd.controls = function() {

  var accessibleChange = function() {
    $("#accessible-radio").bind("change", function(event, ui) {
      var form = $(this).closest('form');
      $.post(form.attr('action'), form.serialize(), function(data) {
        var image = null;
        switch (data.accessible) {
          case "true":
            image = "accessible.png";
            break;
          case "false":
            image = "non-accessible.png";
            break;
          case "nil":
            image = "unknown.png";
         }
        $('.accessible img').attr('src', "/images/" + image);
      });
      return false;
    });
  };

  var backButton = function() {
    $('.back').click(function() {
      history.go(-1);
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
    accessibleChange: accessibleChange,
    backButton: backButton
  }

}();

$(document).bind('pageinit', function() {
  wheelcrowd.geoLocation.locate();
  wheelcrowd.controls.accessibleChange();
  wheelcrowd.controls.backButton();
});
