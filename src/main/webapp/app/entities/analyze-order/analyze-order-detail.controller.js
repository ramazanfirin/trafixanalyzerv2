(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderDetailController', AnalyzeOrderDetailController);

    AnalyzeOrderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'AnalyzeOrder', 'Video', 'Scenario', 'AnalyzeOrderDetails'];

    function AnalyzeOrderDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, AnalyzeOrder, Video, Scenario, AnalyzeOrderDetails) {
        var vm = this;

        vm.analyzeOrder = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:analyzeOrderUpdate', function(event, result) {
            vm.analyzeOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
