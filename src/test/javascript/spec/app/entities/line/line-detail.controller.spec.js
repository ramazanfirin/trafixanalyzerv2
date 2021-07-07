'use strict';

describe('Controller Tests', function() {

    describe('Line Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLine, MockScenario, MockDirection;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLine = jasmine.createSpy('MockLine');
            MockScenario = jasmine.createSpy('MockScenario');
            MockDirection = jasmine.createSpy('MockDirection');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Line': MockLine,
                'Scenario': MockScenario,
                'Direction': MockDirection
            };
            createController = function() {
                $injector.get('$controller')("LineDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'trafficanalzyzerv2App:lineUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
