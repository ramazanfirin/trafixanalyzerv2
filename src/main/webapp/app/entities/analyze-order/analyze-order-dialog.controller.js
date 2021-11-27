(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderDialogController', AnalyzeOrderDialogController);

    AnalyzeOrderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AnalyzeOrder', 'Video', 'Scenario', 'AnalyzeOrderDetails','$window','Line','Polygon','$location'];

    function AnalyzeOrderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AnalyzeOrder, Video, Scenario, AnalyzeOrderDetails, $window, Line, Polygon,$location) {
        var vm = this;

        vm.analyzeOrder = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        //vm.videos = Video.query();
 		vm.videos = Video.getAll()
        //vm.scenarios = Scenario.query();
        vm.scenarios = Scenario.getAll()
        
		vm.analyzeorderdetails = AnalyzeOrderDetails.query();
        vm.videoFileChanged = videoFileChanged;
        vm.scenarioChanged = scenarioChanged;
        vm.lines = [];
        vm.polygons = [];
        vm.polygonType = "COUNTING";
        vm.baseUrl='http://'+$location.host()+':'+$location.port();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
//            if(vm.analyzeOrder.scenario.id != null)
//            	scenarioChanged();
            
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

		function videoFileChanged(){
			$window.imageObj1.src = 'http://'+$location.host()+':'+$location.port()+'/api/videos/image/'+vm.analyzeOrder.video.id;
			$window.layer.draw();
		}
		
		function scenarioChanged(){
		resetAll();
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

		function deleteFromScreen(list){
        	 for(var i=0;i<list.length;i++){
				list[i].destroy();
			}
        }

		function resetAll(){
        
        
        	deleteFromScreen($window.polygonListOnScreen);	
        	deleteFromScreen($window.lineListOnScreen);
        	deleteFromScreen($window.textListOnScreen);
        	deleteFromScreen($window.arrowListOnScreen);
        
        	$window.selectedPolygons = [];
            $window.polygonListOnScreen=[];
			$window.lineListOnScreen=[];
			$window.textListOnScreen=[];
            $window.selectedPolygons = [];
            $window.arrowListOnScreen = [];
            $window.poly = null;
            //loadAll();
            //$window.layer = new Konva.Layer();
        }

        function save () {
            vm.isSaving = true;
            if (vm.analyzeOrder.id !== null) {
            	
                AnalyzeOrder.update(vm.analyzeOrder, onSaveSuccess, onSaveError);
            } else {
            var dataURL = $window.stage.toDataURL();
                vm.analyzeOrder.screenShoot=dataURL.replace('data:image/png;base64,','');
                vm.analyzeOrder.screenShootContentType='image/png';
                AnalyzeOrder.save(vm.analyzeOrder, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:analyzeOrderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError (data) {
            alert('hata olustu.'+ data.data.title)
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
