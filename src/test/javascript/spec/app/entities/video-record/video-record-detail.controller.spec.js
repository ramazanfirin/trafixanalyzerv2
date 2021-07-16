'use strict';

describe('Controller Tests', function() {

    describe('VideoRecord Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVideoRecord, MockVideo, MockLine, MockDirection, MockAnalyzeOrder;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVideoRecord = jasmine.createSpy('MockVideoRecord');
            MockVideo = jasmine.createSpy('MockVideo');
            MockLine = jasmine.createSpy('MockLine');
            MockDirection = jasmine.createSpy('MockDirection');
            MockAnalyzeOrder = jasmine.createSpy('MockAnalyzeOrder');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'VideoRecord': MockVideoRecord,
                'Video': MockVideo,
                'Line': MockLine,
                'Direction': MockDirection,
                'AnalyzeOrder': MockAnalyzeOrder
            };
            createController = function() {
                $injector.get('$controller')("VideoRecordDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'trafficanalzyzerv2App:videoRecordUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
