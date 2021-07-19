(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('FileUploadTestDialogController', FileUploadTestDialogController);

    FileUploadTestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'FileUploadTest'];

    function FileUploadTestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, FileUploadTest) {
        var vm = this;

        vm.fileUploadTest = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.fileUploadTest.id !== null) {
                FileUploadTest.update(vm.fileUploadTest, onSaveSuccess, onSaveError);
            } else {
                FileUploadTest.save(vm.fileUploadTest, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:fileUploadTestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFile = function ($file, fileUploadTest) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        fileUploadTest.file = base64Data;
                        fileUploadTest.fileContentType = $file.type;
                    });
                });
            }
        };

    }
})();
