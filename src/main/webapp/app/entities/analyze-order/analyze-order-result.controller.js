(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderResultController', AnalyzeOrderResultController);

    AnalyzeOrderResultController .$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Scenario', 'Video','$window','Polygon','Line','VideoRecord'];

    function AnalyzeOrderResultController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Scenario, Video,$window,Polygon,Line, VideoRecord) {
        var vm = this;

        vm.scenario = entity.scenario;
        vm.clear = clear;
        vm.save = save;
        vm.videos = Video.query();
		//vm.update = update;
		vm.addPolygon = addPolygon;
		//vm.addToPolygonList = addToPolygonList;
		vm.resetMessage = resetMessage;
		//vm.deletePolygon = deletePolygon;
		vm.adding = false;
		//vm.cancelPolygon = cancelPolygon;
		vm.addLine = addLine;
		vm.deleteLine = deleteLine;
		vm.report =[];

		vm.addMessageBefore = "Çizgi eklemek için, Ekle butonuna basınız"
	    vm.addMessageAfter = "Çizgi ekleyebilirsiniz"
	    vm.addMessage = vm.addMessageBefore;
	    vm.selectVideoMessage = ""
	    vm.polygons = []; 
	    vm.lines = []; 
	    vm.polygon = [];
	    vm.speedPolygons = [];
	    vm.points = $window.points;
	    vm.showPopup = showPopup;
	    vm.hidePopup = hidePopup;
	    //vm.createPolygon = vm.createPolygon();
	    vm.polygonType="COUNTING";

		loadAll();

        function loadAll () {
        //$("#exampleModal").modal("show");
        	
        	VideoRecord.getResultOfAnalyzeOrder({id:entity.id},getResultOfAnalyzeOrderSucccess,onSaveError)
        	
        	
        	if(vm.scenario.id == null)
        		Scenario.save(vm.scenario, onSaveSuccessFirst, onSaveError);
        	else{
        	//
        		Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListForFirstTime,onSaveError);
        		Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:'SPEED'},getSpeedPolygonsSuccess,onSaveError);
        	}	
        		
        		
        }
        
        function getPolygonListForFirstTime(result){
			//resetAll();
			vm.polygons = result;
			$scope.$broadcast('ploygonDataReceived', "ramazan");
			Line.getLineListByScenarioId({id:vm.scenario.id},getLineListForFirstTime,onSaveError);
		}
		
		function getLineListForFirstTime(result){
			vm.lines = result;
			
			$scope.$broadcast('lineDataReceived', "ramazan");
		}
        
        function getSpeedPolygonsSuccess(result){
        	vm.speedPolygons = result;
        	$scope.$broadcast('speedPloygonDataReceived', "ramazan");
        }
        
        function getResultOfAnalyzeOrderSucccess(result){
        	vm.report = result;
        }
        
        function onSaveSuccessFirst (result) {
           vm.scenario = result;
           Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
        }

		function resetMessage(){
	    	vm.selectVideoMessage = ""
	    }
	    
	    function addLine () {
			
			Line.createLineByPolygons({
				scenarioId:vm.scenario.id,
				startPolygonId:selectedPolygons[0].attrs.id,
				endPolygonId:selectedPolygons[1].attrs.id,
				name:vm.addLineName
			},addLineSuccess,onSaveError);
            
        } 
        
        function addLineSuccess(){
 			vm.addMessage = vm.addMessageBefore;
            vm.adding = false;
            
//            for(var i=0;i<selectedPolygons.length;i++){
//				selectedPolygons[i].fill("lightgreen");;
//			}
            resetAll();
			vm.addLineName= "";
			hidePopup();
 			       
        }
        
        function resetAll(){
        
        	deleteFromScreen($window.selectedPolygons);	
        	deleteFromScreen($window.polygonListOnScreen);	
        	deleteFromScreen($window.lineListOnScreen);
        	deleteFromScreen($window.textListOnScreen);
        	
        
        	$window.selectedPolygons = [];
            $window.polygonListOnScreen=[];
			$window.lineListOnScreen=[];
			$window.textListOnScreen=[];
            $window.selectedPolygons = [];
            $window.poly = null;
            //loadAll();
            //$window.layer = new Konva.Layer();
        }
        
        function deleteFromScreen(list){
        	 for(var i=0;i<list.length;i++){
				list[i].destroy();
			}
        }
        function showPopup(){
        	if($window.selectedPolygons.length!=2){
				alert("2 adet seçim yapmalısınız");
				return;
			}
			
			$window.modal.style.display = "block";
        }
		
		function hidePopup(){
			$window.modal.style.display = "none";
        
		}

		function deleteLine(lineId){
			 
			 Line.delete({id: lineId},loadAll,onSaveError);
			 
		}
		
		function onPolygonSaveSuccess (result) {
             Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
        }

		function getPolygonListSuccess(result){
			vm.polygons = result;
		}

		function addPolygon () {
	    	vm.addMessage = vm.addMessageAfter;
	    	vm.adding = true;
	    }
	    
	    function getPolygonList () {
	    	Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
	    }

		
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
//            if(vm.polygons == null || vm.polygons.length==0){
//            	alert('Senaryo içerisine polygon bulunmadığı için, senaryo silinecektir.Onaylıyor musunuz ? ');
//            	Scenario.delete({id:vm.scenario.id});
//            }
        }

        function save () {
         $uibModalInstance.close();
//            vm.isSaving = true;
//            if (vm.scenario.id !== null) {
//                Scenario.update(vm.scenario, onSaveSuccess, onSaveError);
//            } else {
//                Scenario.save(vm.scenario, onSaveSuccess, onSaveError);
//            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:scenarioUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
