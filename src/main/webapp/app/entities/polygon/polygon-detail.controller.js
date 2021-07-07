(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('PolygonDetailController', PolygonDetailController);

    PolygonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Polygon', 'Scenario'];

    function PolygonDetailController($scope, $rootScope, $stateParams, previousState, entity, Polygon, Scenario) {
        var vm = this;

        vm.polygon = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:polygonUpdate', function(event, result) {
            vm.polygon = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
