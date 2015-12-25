'use strict';

angular.module('myappApp')
    .controller('EmployeeDetailController', function ($scope, $rootScope, $stateParams, entity, Employee) {
        $scope.employee = entity;
        $scope.load = function (id) {
            Employee.get({id: id}, function(result) {
                $scope.employee = result;
            });
        };
        var unsubscribe = $rootScope.$on('myappApp:employeeUpdate', function(event, result) {
            $scope.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
