'use strict';

describe('Controller Tests', function() {

    describe('CameraRecord Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCameraRecord, MockCamera, MockLine, MockDirection;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCameraRecord = jasmine.createSpy('MockCameraRecord');
            MockCamera = jasmine.createSpy('MockCamera');
            MockLine = jasmine.createSpy('MockLine');
            MockDirection = jasmine.createSpy('MockDirection');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CameraRecord': MockCameraRecord,
                'Camera': MockCamera,
                'Line': MockLine,
                'Direction': MockDirection
            };
            createController = function() {
                $injector.get('$controller')("CameraRecordDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'trafficanalzyzerv2App:cameraRecordUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
