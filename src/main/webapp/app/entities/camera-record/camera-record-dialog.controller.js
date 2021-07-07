(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('CameraRecordDialogController', CameraRecordDialogController);

    CameraRecordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CameraRecord', 'Camera', 'Line', 'Direction'];

    function CameraRecordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CameraRecord, Camera, Line, Direction) {
        var vm = this;

        vm.cameraRecord = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.cameras = Camera.query();
        vm.lines = Line.query();
        vm.directions = Direction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cameraRecord.id !== null) {
                CameraRecord.update(vm.cameraRecord, onSaveSuccess, onSaveError);
            } else {
                CameraRecord.save(vm.cameraRecord, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:cameraRecordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.insertDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
