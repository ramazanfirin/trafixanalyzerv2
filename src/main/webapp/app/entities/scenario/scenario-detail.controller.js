(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('ScenarioDetailController', ScenarioDetailController);

    ScenarioDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Scenario', 'Video'];

    function ScenarioDetailController($scope, $rootScope, $stateParams, previousState, entity, Scenario, Video) {
        var vm = this;

        vm.scenario = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:scenarioUpdate', function(event, result) {
            vm.scenario = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
