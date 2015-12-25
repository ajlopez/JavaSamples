'use strict';

angular.module('myappApp')
	.controller('CommentDeleteController', function($scope, $uibModalInstance, entity, Comment) {

        $scope.comment = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Comment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
