(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderDialogController', AnalyzeOrderDialogController);

    AnalyzeOrderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AnalyzeOrder', 'Video', 'Scenario', 'AnalyzeOrderDetails'];

    function AnalyzeOrderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AnalyzeOrder, Video, Scenario, AnalyzeOrderDetails) {
        var vm = this;

        vm.analyzeOrder = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.videos = Video.query();
        vm.scenarios = Scenario.query();
        vm.analyzeorderdetails = AnalyzeOrderDetails.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.analyzeOrder.id !== null) {
                AnalyzeOrder.update(vm.analyzeOrder, onSaveSuccess, onSaveError);
            } else {
                AnalyzeOrder.save(vm.analyzeOrder, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:analyzeOrderUpdate', result);
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
