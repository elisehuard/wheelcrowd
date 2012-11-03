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
    // http call to transmit position to server
    var latitude  = position.coords.latitude;
    var longitude = position.coords.longitude;
    console.log(latitude, longitude);
  }

  return {
    locate: locate
  }

}();

wheelcrowd.geoLocation.locate();
