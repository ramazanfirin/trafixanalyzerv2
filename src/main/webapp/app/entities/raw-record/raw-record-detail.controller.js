(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('RawRecordDetailController', RawRecordDetailController);

    RawRecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RawRecord'];

    function RawRecordDetailController($scope, $rootScope, $stateParams, previousState, entity, RawRecord) {
        var vm = this;

        vm.rawRecord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:rawRecordUpdate', function(event, result) {
            vm.rawRecord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
