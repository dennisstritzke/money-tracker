(function () {
  var moneyTracker = angular.module("moneyTracker");

  moneyTracker.controller("MoneyTrackerController", ["$scope", "$http", "ExpenseService", function ($scope, $http, ExpenseService) {
    function createEmptyExpenses() {
      return {
        _embedded: {
          expenses: []
        }
      };
    }

    var months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    $scope.expenses = createEmptyExpenses();

    $scope.submit = createExpense;
    $scope.calculateTotal = function () {
      var total = 0;
      for (var i = 0; i < $scope.expenses._embedded.expenses.length; i++) {
        var expense = $scope.expenses._embedded.expenses[i];
        total = total + expense.amount;
      }
      return total;
    };

    $scope.deleteExpense = function (expense) {
      if(confirm("Realy delete expense '" + expense.comment + "'?")) {
        ExpenseService.deleteExpense(expense).then(function () {
          loadExpenses();
        }, function (err) {
          console.log("Something bad happened", err);
        });
      }
    };

    $scope.changeMonth = function (link) {
      ExpenseService.list(link).then(function (expenseResponse) {
        setExpensesInScope(expenseResponse.data);
      }, function (err) {
        console.log("Something went wrong...");
      })
    };

    $scope.selectedMonthName = function () {
      if($scope.expenses._links !== undefined) {
        var link = $scope.expenses._links.self.href;
        var linkComponents = link.split("/");

        var year = linkComponents[linkComponents.length - 2];
        var month = linkComponents[linkComponents.length - 1];
        return months[month - 1] + " " + year;
      } else {
        return "";
      }
    };

    function setExpensesInScope(expenseResource) {
      if(expenseResource._embedded == undefined) {
        expenseResource._embedded = {
          expenses: []
        };
      }
      $scope.expenses = expenseResource;
    }

    function loadExpenses() {
      ExpenseService.listCurrent().then(function (expenseResource) {
        setExpensesInScope(expenseResource);
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