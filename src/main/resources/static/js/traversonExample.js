(function () {
  traverson.registerMediaType(TraversonJsonHalAdapter.mediaType, TraversonJsonHalAdapter);
  traverson.from("http://localhost:8080/api")
      .jsonHal().follow("expenses", "current", "next", "next")
      .getResource(function (error, document) {
        if (error) {
          console.error('No luck :-)');
        } else {
          console.log('We have followed the path and reached our destination.');
          console.log(JSON.stringify(document));
        }
      });
}());