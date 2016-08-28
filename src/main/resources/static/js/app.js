(function () {
  var moneyTracker = angular.module("moneyTracker", []);

  moneyTracker.controller("MoneyTrackerController", ["$scope", "$http", function ($scope, $http) {
    $scope.expense = {};

    $scope.submit = function (expense) {
      $http.post("/api/expenses", {
        comment: expense.comment,
        amount: expense.amount,
        year: currentDate().year,
        month: currentDate().month
      }).then(function () {
        console.log("Success!");
      }, function () {
        console.log("Failure!");
      });
      console.log(expense);
    };

    function getExpenses(year, month) {
      return $http.get("/api/expenses", {
        param: {
          year: year,
          month: month
        }
      });
    }

    function currentDate() {
      var date = new Date();

      return {
        year: date.getFullYear(),
        month: date.getMonth() + 1
      };
    }

    $scope.calculateTotal = function () {
      var total = 0;
      for (var i = 0; i < $scope.expenses.length; i++) {
        var expense = $scope.expenses[i];
        total = total + expense.amount;
      }
      return total;
    };

    $scope.expenses = [];
    getExpenses(currentDate().year, currentDate().month).then(function(result) {
        $scope.expenses = result.data._embedded.expenses;
    }, function() {
        console.log("Problem loading expenses!")
    });
  }]);
})();