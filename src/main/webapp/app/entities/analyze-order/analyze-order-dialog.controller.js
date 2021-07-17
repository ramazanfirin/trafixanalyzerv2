(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderDialogController', AnalyzeOrderDialogController);

    AnalyzeOrderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AnalyzeOrder', 'Video', 'Scenario', 'AnalyzeOrderDetails','$window','Line','Polygon'];

    function AnalyzeOrderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AnalyzeOrder, Video, Scenario, AnalyzeOrderDetails, $window, Line, Polygon) {
        var vm = this;

        vm.analyzeOrder = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.videos = Video.query();
        vm.scenarios = Scenario.query();
        vm.analyzeorderdetails = AnalyzeOrderDetails.query();
        vm.videoFileChanged = videoFileChanged;
        vm.scenarioChanged = scenarioChanged;
        vm.lines = [];
        vm.polygons = [];
        vm.polygonType = "COUNTING";

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
            if(vm.analyzeOrder.scenario.id != null)
            	scenarioChanged();
            
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

		function videoFileChanged(){
			$window.imageObj1.src = 'http://localhost:8080/api/videos/image/'+vm.analyzeOrder.video.id;
			$window.layer.draw();
		}
		
		function scenarioChanged(){
			Polygon.getPolygonListByScenarioId({id:vm.analyzeOrder.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
		}

		function getPolygonListSuccess(result){
			vm.polygons = result;
			Line.getLineListByScenarioId({id:vm.analyzeOrder.scenario.id},getLineListSuccess,onSaveError);
			$scope.$broadcast('ploygonDataReceived', "ramazan");
		}
		
		function getLineListSuccess(result){
			vm.lines = result;
			$scope.$broadcast('lineDataReceived', "ramazan");
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