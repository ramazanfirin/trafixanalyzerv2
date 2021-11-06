(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('ScenarioDialogDirectionController', ScenarioDialogDirectionController);

    ScenarioDialogDirectionController .$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Scenario', 'Video','$window','Polygon','Line','Direction','$location'];

    function ScenarioDialogDirectionController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Scenario, Video,$window,Polygon,Line,Direction,$location) {
        var vm = this;

        vm.scenario = entity;
        vm.clear = clear;
        vm.save = save;
        vm.videos = Video.query();
		//vm.update = update;
		//vm.addPolygon = addPolygon;
		//vm.addToPolygonList = addToPolygonList;
		vm.resetMessage = resetMessage;
		//vm.deletePolygon = deletePolygon;
		vm.adding = false;
		vm.cancelPolygon = cancelPolygon;
		vm.addLine = addLine;
		vm.deleteDirection = deleteDirection;
		vm.addItem = addItem;
		vm.showOnScreen = showOnScreen;

		vm.addMessageBefore = "Çizgi eklemek için, Ekle butonuna basınız"
	    vm.addMessageAfter = "Çizgi ekleyebilirsiniz"
	    vm.addMessage = vm.addMessageBefore;
	    vm.selectVideoMessage = ""
	    vm.polygons = []; 
	    vm.lines = []; 
	    vm.polygon = [];
	    vm.directions = [];
	    vm.points = $window.points;
	    vm.showPopup = showPopup;
	    vm.hidePopup = hidePopup;
	    //vm.createPolygon = vm.createPolygon();
	    vm.polygonType="COUNTING";
	    
	    vm.baseUrl='http://'+$location.host()+':'+$location.port();

		loadAll();

        function loadAll () {
        //$("#exampleModal").modal("show");
        	
        	if(vm.scenario.id == null)
        		Scenario.save(vm.scenario, onSaveSuccessFirst, onSaveError);
        	else{
        	//
        		//Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListForFirstTime,onSaveError);
        		Line.getLineListByScenarioId({id:vm.scenario.id},getLineListForFirstTime,onSaveError);
        	}	
        		
        }
        
//        function getPolygonListForFirstTime(result){
//			//resetAll();
//			vm.polygons = result;
//			//$scope.$broadcast('ploygonDataReceived', "ramazan");
//			Line.getLineListByScenarioId({id:vm.scenario.id},getLineListForFirstTime,onSaveError);
//		}
		
		function getLineListForFirstTime(result){
			vm.lines = result;
			Direction.getDirectionListByScenarioId({id:vm.scenario.id},getDirectionListForFirstTime,onSaveError);
			$scope.$broadcast('lineDataReceived', "ramazan");
		}
		
		function getDirectionListForFirstTime(result){
			vm.directions = result;
			
			$scope.$broadcast('directionDataReceived', "ramazan");
		}
        
        
        function onSaveSuccessFirst (result) {
           vm.scenario = result;
           Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
        }

		function resetMessage(){
	    	vm.selectVideoMessage = ""
	    }
	    
	    function addLine () {
			
			Direction.createDirectionByLines({
				scenarioId:vm.scenario.id,
				startLineId:selectedPolygons[0].attrs.id,
				endLineId:selectedPolygons[1].attrs.id,
				name:vm.addLineName
			},addDirectionSuccess,onSaveError);
            
        } 
        
        function addDirectionSuccess(){
 			vm.addMessage = vm.addMessageBefore;
            vm.adding = false;
            
//            for(var i=0;i<selectedPolygons.length;i++){
//				selectedPolygons[i].fill("lightgreen");;
//			}
            resetAll();
			vm.addLineName= "";
			hidePopup();
			loadAll();
			//$window.layer = new Konva.Layer();			
 			       
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

		function deleteDirection(directionId){
			 
			 Direction.delete({id: directionId},loadAll,onSaveError);
			 
		}
		
//		function onPolygonSaveSuccess (result) {
//             Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
//        }
//
//		function getPolygonListSuccess(result){
//			vm.polygons = result;
//		}
//
		function addItem () {
	    	vm.addMessage = vm.addMessageAfter;
	    	vm.adding = true;
	    	clearSelectedLines();
	    }
//	    
//	    function getPolygonList () {
//	    	Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
//	    }

		
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
            vm.isSaving = true;
            if (vm.scenario.id !== null) {
                Scenario.update(vm.scenario, onSaveSuccess, onSaveError);
            } else {
                Scenario.save(vm.scenario, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:scenarioUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

		function showOnScreen (item) {
		clearSelectedLines();
			console.log("item:"+item)
			for(var i=0;i<$window.lineListOnScreen.length;i++){
				if($window.lineListOnScreen[i].attrs.id == item.startLine.id || $window.lineListOnScreen[i].attrs.id == item.endLine.id)
					$window.lineListOnScreen[i].fill("blue");
			}
		}
		
		function clearSelectedLines(){
			for(var i=0;i<$window.lineListOnScreen.length;i++){
					$window.lineListOnScreen[i].fill("yellow");
				
			}
		}
		
		function cancelPolygon () {
			clearSelectedLines();
	    	vm.addMessage = vm.addMessageBefore;
            vm.adding = false;
            $window.points = [];
            $window.poly = null;
            //deleteFromScreen($window.tempId);
	    }
    }
})();
