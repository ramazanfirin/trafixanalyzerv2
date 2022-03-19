(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderPlayerController', AnalyzeOrderPlayerController);

    AnalyzeOrderPlayerController .$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Scenario', 'Video','$window','Polygon','Line','VideoRecord','$translate','AnalyzeOrder'];

    function AnalyzeOrderPlayerController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Scenario, Video,$window,Polygon,Line, VideoRecord,$translate,AnalyzeOrder) {
        var vm = this;

		vm.analyzeOrder = entity;
        vm.scenario = entity.scenario;
      	vm.log="test";
		
		vm.clear = clear;
		vm.polygonType="COUNTING";
		
		
        loadAll();

        function loadAll () {
			Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListForFirstTime,onSaveError);
        }
        
		function getPolygonListForFirstTime(result){
			//resetAll();
			vm.polygons = result;
			//$scope.$broadcast('ploygonDataReceived', "ramazan");
			Line.getLineListByScenarioId({id:vm.scenario.id},getLineListForFirstTime,onSaveError);
		}
		
		function getLineListForFirstTime(result){
			vm.lines = result;
			getVisulationData();
			$scope.$broadcast('lineDataReceived', "ramazan");
		}

		function getVisulationData(){
			VideoRecord.getVisulationData({id:vm.analyzeOrder.id},getVisulationDataSuccess,onSaveError);
			
		}
		
		function getVisulationDataSuccess(result){
			vm.visulationData = result;
			//var videoPath =vm.analyzeOrder.video.path.replace('/home/ramazan/Desktop/videos','');
			//$window.video.src = 'http://localhost:8000/'+videoPath;
			$window.play();
		}
		
		

		function onSaveError () {
            vm.isSaving = false;
        }

		function clear () {
            $uibModalInstance.dismiss('cancel');
//            if(vm.polygons == null || vm.polygons.length==0){
//            	alert('Senaryo içerisine polygon bulunmadığı için, senaryo silinecektir.Onaylıyor musunuz ? ');
//            	Scenario.delete({id:vm.scenario.id});
//            }
			for(var i=0;i<$window.timeouts.length;i++){
				clearTimeout(timeouts[i]);
			}
	
        }

		function onError(error) {
               // AlertService.error(error.data.message);
            }


                
    }
})();
