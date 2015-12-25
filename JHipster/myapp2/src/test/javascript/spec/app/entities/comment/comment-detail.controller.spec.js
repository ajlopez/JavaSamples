'use strict';

describe('Comment Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockComment, MockEmployee;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockComment = jasmine.createSpy('MockComment');
        MockEmployee = jasmine.createSpy('MockEmployee');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Comment': MockComment,
            'Employee': MockEmployee
        };
        createController = function() {
            $injector.get('$controller')("CommentDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'myapp2App:commentUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
