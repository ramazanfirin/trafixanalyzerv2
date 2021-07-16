(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderDetailsDeleteController',AnalyzeOrderDetailsDeleteController);

    AnalyzeOrderDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'AnalyzeOrderDetails'];

    function AnalyzeOrderDetailsDeleteController($uibModalInstance, entity, AnalyzeOrderDetails) {
        var vm = this;

        vm.analyzeOrderDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AnalyzeOrderDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
