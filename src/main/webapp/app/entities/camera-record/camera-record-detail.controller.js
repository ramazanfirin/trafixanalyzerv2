(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('CameraRecordDetailController', CameraRecordDetailController);

    CameraRecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CameraRecord', 'Camera', 'Line', 'Direction'];

    function CameraRecordDetailController($scope, $rootScope, $stateParams, previousState, entity, CameraRecord, Camera, Line, Direction) {
        var vm = this;

        vm.cameraRecord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:cameraRecordUpdate', function(event, result) {
            vm.cameraRecord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
