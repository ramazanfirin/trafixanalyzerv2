(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('LineDetailController', LineDetailController);

    LineDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Line', 'Scenario', 'Direction', 'Polygon'];

    function LineDetailController($scope, $rootScope, $stateParams, previousState, entity, Line, Scenario, Direction, Polygon) {
        var vm = this;

        vm.line = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:lineUpdate', function(event, result) {
            vm.line = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
