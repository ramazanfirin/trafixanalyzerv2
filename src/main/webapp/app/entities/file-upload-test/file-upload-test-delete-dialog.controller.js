(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('FileUploadTestDeleteController',FileUploadTestDeleteController);

    FileUploadTestDeleteController.$inject = ['$uibModalInstance', 'entity', 'FileUploadTest'];

    function FileUploadTestDeleteController($uibModalInstance, entity, FileUploadTest) {
        var vm = this;

        vm.fileUploadTest = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FileUploadTest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
