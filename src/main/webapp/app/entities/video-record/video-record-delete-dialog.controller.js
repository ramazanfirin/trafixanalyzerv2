(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('VideoRecordDeleteController',VideoRecordDeleteController);

    VideoRecordDeleteController.$inject = ['$uibModalInstance', 'entity', 'VideoRecord'];

    function VideoRecordDeleteController($uibModalInstance, entity, VideoRecord) {
        var vm = this;

        vm.videoRecord = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VideoRecord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
