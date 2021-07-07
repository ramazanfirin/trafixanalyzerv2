(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('LineDialogController', LineDialogController);

    LineDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Line', 'Scenario', 'Direction'];

    function LineDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Line, Scenario, Direction) {
        var vm = this;

        vm.line = entity;
        vm.clear = clear;
        vm.save = save;
        vm.scenarios = Scenario.query();
        vm.directions = Direction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.line.id !== null) {
                Line.update(vm.line, onSaveSuccess, onSaveError);
            } else {
                Line.save(vm.line, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:lineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
