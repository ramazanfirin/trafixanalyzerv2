(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('VideoDetailController', VideoDetailController);

    VideoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Video', 'Location'];

    function VideoDetailController($scope, $rootScope, $stateParams, previousState, entity, Video, Location) {
        var vm = this;

        vm.video = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:videoUpdate', function(event, result) {
            vm.video = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
