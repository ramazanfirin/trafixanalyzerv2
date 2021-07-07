(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('DistrictDetailController', DistrictDetailController);

    DistrictDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'District', 'City'];

    function DistrictDetailController($scope, $rootScope, $stateParams, previousState, entity, District, City) {
        var vm = this;

        vm.district = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:districtUpdate', function(event, result) {
            vm.district = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
