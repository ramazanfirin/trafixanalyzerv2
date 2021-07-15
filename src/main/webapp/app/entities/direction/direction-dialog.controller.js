(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('DirectionDialogController', DirectionDialogController);

    DirectionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Direction', 'Scenario', 'Line'];

    function DirectionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Direction, Scenario, Line) {
        var vm = this;

        vm.direction = entity;
        vm.clear = clear;
        vm.save = save;
        vm.scenarios = Scenario.query();
        vm.lines = Line.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.direction.id !== null) {
                Direction.update(vm.direction, onSaveSuccess, onSaveError);
            } else {
                Direction.save(vm.direction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:directionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
