(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('PolygonDeleteController',PolygonDeleteController);

    PolygonDeleteController.$inject = ['$uibModalInstance', 'entity', 'Polygon'];

    function PolygonDeleteController($uibModalInstance, entity, Polygon) {
        var vm = this;

        vm.polygon = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Polygon.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
