(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderDeleteController',AnalyzeOrderDeleteController);

    AnalyzeOrderDeleteController.$inject = ['$uibModalInstance', 'entity', 'AnalyzeOrder'];

    function AnalyzeOrderDeleteController($uibModalInstance, entity, AnalyzeOrder) {
        var vm = this;

        vm.analyzeOrder = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AnalyzeOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
