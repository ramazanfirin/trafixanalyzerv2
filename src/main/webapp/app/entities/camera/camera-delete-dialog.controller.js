(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('CameraDeleteController',CameraDeleteController);

    CameraDeleteController.$inject = ['$uibModalInstance', 'entity', 'Camera'];

    function CameraDeleteController($uibModalInstance, entity, Camera) {
        var vm = this;

        vm.camera = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Camera.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
