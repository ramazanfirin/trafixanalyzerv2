(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('PolygonDialogController', PolygonDialogController);

    PolygonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Polygon', 'Scenario'];

    function PolygonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Polygon, Scenario) {
        var vm = this;

        vm.polygon = entity;
        vm.clear = clear;
        vm.save = save;
        vm.scenarios = Scenario.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.polygon.id !== null) {
                Polygon.update(vm.polygon, onSaveSuccess, onSaveError);
            } else {
                Polygon.save(vm.polygon, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:polygonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
