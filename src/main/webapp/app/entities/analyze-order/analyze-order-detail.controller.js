(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderDetailController', AnalyzeOrderDetailController);

    AnalyzeOrderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AnalyzeOrder', 'Video', 'Scenario', 'AnalyzeOrderDetails'];

    function AnalyzeOrderDetailController($scope, $rootScope, $stateParams, previousState, entity, AnalyzeOrder, Video, Scenario, AnalyzeOrderDetails) {
        var vm = this;

        vm.analyzeOrder = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:analyzeOrderUpdate', function(event, result) {
            vm.analyzeOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
