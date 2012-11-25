var wheelcrowd = wheelcrowd || {};
wheelcrowd.images = function() {
  var load = function() {
    $.each($('.photo'), function(index, value) {
      var value = $(value);
      var id = value.attr('data-id');
      $.get("/venue/" + id + "/photo", function(data) {
        value.attr('src', data.photo);
      });
    });
  }
  return {
    load: load
  }
}();

wheelcrowd.images.load();
