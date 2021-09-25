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
		loadAll();

		
        
        function loadAll () {
        	AnalyzeOrder.getLogs({id:entity.id},getLogsSucccess,onError)
        		
        }
        
		function clear () {
            $uibModalInstance.dismiss('cancel');
//            if(vm.polygons == null || vm.polygons.length==0){
//            	alert('Senaryo içerisine polygon bulunmadığı için, senaryo silinecektir.Onaylıyor musunuz ? ');
//            	Scenario.delete({id:vm.scenario.id});
//            }
        }

		function onError(error) {
               // AlertService.error(error.data.message);
            }

		function getLogsSucccess(result) {
//                vm.log=result.value.replaceAll('\n','<br/>');
vm.log=result.value;
				console.log(vm.log);
          }
                
    }
})();
