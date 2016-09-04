(function () {
  var moneyTracker = angular.module("moneyTracker");

  moneyTracker.service("ExpenseService", ["traverson", "$http", function (traverson, $http) {
    traverson.registerMediaType(TraversonJsonHalAdapter.mediaType, TraversonJsonHalAdapter);
    var myTraverson = traverson.from("/api").jsonHal().useAngularHttp();

    this.list = function(url) {
      return $http({
        method: "GET",
        url: url
      });
    };

    this.listCurrent = function () {
      return myTraverson
          .newRequest()
          .follow("expenses", "current")
          .getResource().result;
    };

    this.create = function (expense) {
      return myTraverson
          .newRequest()
          .follow("expenses", "current")
          .post(expense).result;
    };

    this.deleteExpense = function (expense) {
      return $http({
        method: "DELETE",
        url: expense._links.self.href
      });
    }
  }]);
}());