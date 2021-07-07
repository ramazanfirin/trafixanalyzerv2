(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('LocationDetailController', LocationDetailController);

    LocationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Location', 'District'];

    function LocationDetailController($scope, $rootScope, $stateParams, previousState, entity, Location, District) {
        var vm = this;

        vm.location = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:locationUpdate', function(event, result) {
            vm.location = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
