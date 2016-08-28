(function () {
  var moneyTracker = angular.module("moneyTracker");

  moneyTracker.service("ExpenseService", ["traverson", function (traverson) {
    traverson.registerMediaType(TraversonJsonHalAdapter.mediaType, TraversonJsonHalAdapter);
    var myTraverson = traverson.from("http://localhost:8080/api").jsonHal().useAngularHttp();

    this.listCurrent = function () {
      return myTraverson
          .newRequest()
          .follow("expenses", "current")
          .getResource().result;
    };
  }]);
}());