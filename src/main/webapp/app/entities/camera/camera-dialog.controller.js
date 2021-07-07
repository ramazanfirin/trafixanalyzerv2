(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('CameraDialogController', CameraDialogController);

    CameraDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Camera', 'Location'];

    function CameraDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Camera, Location) {
        var vm = this;

        vm.camera = entity;
        vm.clear = clear;
        vm.save = save;
        vm.locations = Location.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.camera.id !== null) {
                Camera.update(vm.camera, onSaveSuccess, onSaveError);
            } else {
                Camera.save(vm.camera, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:cameraUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
