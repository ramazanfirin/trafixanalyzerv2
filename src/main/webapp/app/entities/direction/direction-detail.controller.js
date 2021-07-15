(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('DirectionDetailController', DirectionDetailController);

    DirectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Direction', 'Scenario', 'Line'];

    function DirectionDetailController($scope, $rootScope, $stateParams, previousState, entity, Direction, Scenario, Line) {
        var vm = this;

        vm.direction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:directionUpdate', function(event, result) {
            vm.direction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
