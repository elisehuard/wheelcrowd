var wheelcrowd = wheelcrowd || {};
wheelcrowd.geoLocation = function() {
  var locate = function() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(getPosition);
    } else {
      console.log("Geolocation is not supported by this browser.");
    }
  };

  var getPosition = function(position) {
    var latitude  = position.coords.latitude;
    var longitude = position.coords.longitude;
    var explore = document.getElementById("explore");
    if (explore) {
      explore.setAttribute('href', "/venues?lat=" + latitude + "&lon=" + longitude);
    }
  }

  return {
    locate: locate
  }

}();

wheelcrowd.geoLocation.locate();
