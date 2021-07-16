'use strict';

describe('Controller Tests', function() {

    describe('AnalyzeOrder Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAnalyzeOrder, MockVideo, MockScenario, MockAnalyzeOrderDetails;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAnalyzeOrder = jasmine.createSpy('MockAnalyzeOrder');
            MockVideo = jasmine.createSpy('MockVideo');
            MockScenario = jasmine.createSpy('MockScenario');
            MockAnalyzeOrderDetails = jasmine.createSpy('MockAnalyzeOrderDetails');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AnalyzeOrder': MockAnalyzeOrder,
                'Video': MockVideo,
                'Scenario': MockScenario,
                'AnalyzeOrderDetails': MockAnalyzeOrderDetails
            };
            createController = function() {
                $injector.get('$controller')("AnalyzeOrderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'trafficanalzyzerv2App:analyzeOrderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
