(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('RawRecordDeleteController',RawRecordDeleteController);

    RawRecordDeleteController.$inject = ['$uibModalInstance', 'entity', 'RawRecord'];

    function RawRecordDeleteController($uibModalInstance, entity, RawRecord) {
        var vm = this;

        vm.rawRecord = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RawRecord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
