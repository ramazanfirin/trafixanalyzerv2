(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('DirectionDeleteController',DirectionDeleteController);

    DirectionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Direction'];

    function DirectionDeleteController($uibModalInstance, entity, Direction) {
        var vm = this;

        vm.direction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Direction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
