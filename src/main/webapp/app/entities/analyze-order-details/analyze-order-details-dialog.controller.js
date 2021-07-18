(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderDetailsDialogController', AnalyzeOrderDetailsDialogController);

    AnalyzeOrderDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'AnalyzeOrderDetails'];

    function AnalyzeOrderDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, AnalyzeOrderDetails) {
        var vm = this;

        vm.analyzeOrderDetails = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.analyzeOrderDetails.id !== null) {
                AnalyzeOrderDetails.update(vm.analyzeOrderDetails, onSaveSuccess, onSaveError);
            } else {
                AnalyzeOrderDetails.save(vm.analyzeOrderDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:analyzeOrderDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
