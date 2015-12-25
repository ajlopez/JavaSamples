'use strict';

angular.module('myapp2App').controller('CommentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Comment', 'Employee',
        function($scope, $stateParams, $uibModalInstance, entity, Comment, Employee) {

        $scope.comment = entity;
        $scope.employees = Employee.query();
        $scope.load = function(id) {
            Comment.get({id : id}, function(result) {
                $scope.comment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('myapp2App:commentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.comment.id != null) {
                Comment.update($scope.comment, onSaveSuccess, onSaveError);
            } else {
                Comment.save($scope.comment, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
