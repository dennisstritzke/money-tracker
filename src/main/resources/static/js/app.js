(function () {
  var moneyTracker = angular.module("moneyTracker");

  moneyTracker.controller("MoneyTrackerController", ["$scope", "$http", "ExpenseService", function ($scope, $http, ExpenseService) {
    var months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    $scope.expenses = [];

    $scope.submit = createExpense;
    $scope.calculateTotal = function () {
      var total = 0;
      for (var i = 0; i < $scope.expenses.length; i++) {
        var expense = $scope.expenses[i];
        total = total + expense.amount;
      }
      return total;
    };

    $scope.deleteExpense = function (expense) {
      ExpenseService.deleteExpense(expense).then(function () {
        loadExpenses();
      }, function (err) {
        console.log("Something bad happened", err);
      })
    };

    $scope.changeMonth = function (link) {
      ExpenseService.list(link).then(function (expenseResponse) {
        $scope.expenses = expenseResponse.data;
      }, function (err) {
        console.log("Something went wrong...");
      })
    };

    $scope.selectedMonthName = function () {
      var link = $scope.expenses._links.self.href;
      var linkComponents = link.split("/");

      var year = linkComponents[linkComponents.length - 2];
      var month = linkComponents[linkComponents.length - 1];
      return months[month - 1] + " " + year;
    };

    function loadExpenses() {
      ExpenseService.listCurrent().then(function (expenseResource) {
        $scope.expenses = expenseResource;
      }, function (err) {
        console.log("Something went wrong");
        console.log(err);
      });
    }

    function createExpense(expense) {
      ExpenseService.create(expense).then(function () {
        $scope.expense = {};
        loadExpenses();
      }, function () {
        console.log("Something went wrong...");
      })
    }

    loadExpenses();
  }]);
})();