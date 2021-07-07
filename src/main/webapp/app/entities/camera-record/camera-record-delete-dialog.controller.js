(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('CameraRecordDeleteController',CameraRecordDeleteController);

    CameraRecordDeleteController.$inject = ['$uibModalInstance', 'entity', 'CameraRecord'];

    function CameraRecordDeleteController($uibModalInstance, entity, CameraRecord) {
        var vm = this;

        vm.cameraRecord = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CameraRecord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
