(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('VideoRecordDetailController', VideoRecordDetailController);

    VideoRecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VideoRecord', 'Video', 'Line', 'Direction'];

    function VideoRecordDetailController($scope, $rootScope, $stateParams, previousState, entity, VideoRecord, Video, Line, Direction) {
        var vm = this;

        vm.videoRecord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:videoRecordUpdate', function(event, result) {
            vm.videoRecord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
