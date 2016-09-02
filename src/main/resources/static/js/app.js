(function () {
  var moneyTracker = angular.module("moneyTracker");

  moneyTracker.controller("MoneyTrackerController", ["$scope", "$http", "ExpenseService", function ($scope, $http, ExpenseService) {
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

    function loadExpenses() {
      ExpenseService.listCurrent().then(function (expenseResource) {
        if(expenseResource._embedded !== undefined) {
          $scope.expenses = expenseResource._embedded.expenses;
        }
      }, function (err) {
        console.log("Something went wrong");
        console.log(err);
      });
    }

    function createExpense(expense) {
      ExpenseService.create(expense).then(function() {
        $scope.expense = {};
        loadExpenses();
      }, function () {
        console.log("Something went wrong...");
      })
    }

    loadExpenses();
  }]);
})();