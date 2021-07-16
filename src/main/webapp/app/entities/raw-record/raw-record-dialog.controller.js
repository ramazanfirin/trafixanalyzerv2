(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('RawRecordDialogController', RawRecordDialogController);

    RawRecordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RawRecord'];

    function RawRecordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RawRecord) {
        var vm = this;

        vm.rawRecord = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.rawRecord.id !== null) {
                RawRecord.update(vm.rawRecord, onSaveSuccess, onSaveError);
            } else {
                RawRecord.save(vm.rawRecord, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:rawRecordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.time = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
