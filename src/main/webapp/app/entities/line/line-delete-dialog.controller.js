(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('LineDeleteController',LineDeleteController);

    LineDeleteController.$inject = ['$uibModalInstance', 'entity', 'Line'];

    function LineDeleteController($uibModalInstance, entity, Line) {
        var vm = this;

        vm.line = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Line.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
