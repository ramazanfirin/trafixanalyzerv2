(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderDetailsDetailController', AnalyzeOrderDetailsDetailController);

    AnalyzeOrderDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'AnalyzeOrderDetails'];

    function AnalyzeOrderDetailsDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, AnalyzeOrderDetails) {
        var vm = this;

        vm.analyzeOrderDetails = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:analyzeOrderDetailsUpdate', function(event, result) {
            vm.analyzeOrderDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
