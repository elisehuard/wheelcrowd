var wheelcrowd = wheelcrowd || {};
wheelcrowd.geoLocation = function() {
  var locate = function() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(getPosition);
    } else {
      console.log("Geolocation is not supported by this browser.");
    }
  };

  var addLocationInput = function(name, value) {
    var input = document.createElement('input');
    input.setAttribute('name', name);
    input.setAttribute('type', 'hidden');
    input.setAttribute('value', value);
    return input;
  }
  var getPosition = function(position) {
    var latitude  = position.coords.latitude;
    var longitude = position.coords.longitude;
    var explore = document.getElementById("explore");
    if (explore) {
      var latInput = addLocationInput('lat', latitude);
      explore.appendChild(latInput);
      var lonInput = addLocationInput('lon', longitude);
      explore.appendChild(lonInput);
    }
  }

  return {
    locate: locate
  }

}();

wheelcrowd.geoLocation.locate();
