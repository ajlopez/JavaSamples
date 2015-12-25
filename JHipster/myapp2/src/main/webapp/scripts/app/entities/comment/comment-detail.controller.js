'use strict';

angular.module('myapp2App')
    .controller('CommentDetailController', function ($scope, $rootScope, $stateParams, entity, Comment, Employee) {
        $scope.comment = entity;
        $scope.load = function (id) {
            Comment.get({id: id}, function(result) {
                $scope.comment = result;
            });
        };
        var unsubscribe = $rootScope.$on('myapp2App:commentUpdate', function(event, result) {
            $scope.comment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
