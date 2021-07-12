(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('ScenarioDialogLineController', ScenarioDialogLineController);

    ScenarioDialogLineController .$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Scenario', 'Video','$window','Polygon'];

    function ScenarioDialogLineController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Scenario, Video,$window,Polygon) {
        var vm = this;

        vm.scenario = entity;
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

		vm.addMessageBefore = "Çizgi eklemek için, Ekle butonuna basınız"
	    vm.addMessageAfter = "Çizgi ekleyebilirsiniz"
	    vm.addMessage = vm.addMessageBefore;
	    vm.selectVideoMessage = ""
	    vm.polygons = []; 
	    vm.polygon = [];
	    vm.points = $window.points;
	    //vm.createPolygon = vm.createPolygon();

		loadAll();

        function loadAll () {
        //$("#exampleModal").modal("show");
        
        	if(vm.scenario.id == null)
        		Scenario.save(vm.scenario, onSaveSuccessFirst, onSaveError);
        	else{
        		Polygon.getPolygonListByScenarioId({id:vm.scenario.id},getPolygonListForFirstTime,onSaveError);
        	}	
        		
        }
        
        function getPolygonListForFirstTime(result){
			vm.polygons = result;
			$scope.$broadcast('messagename', "ramazan");
		}
        
        function onSaveSuccessFirst (result) {
           vm.scenario = result;
           Polygon.getPolygonListByScenarioId({id:vm.scenario.id},getPolygonListSuccess,onSaveError);
        }

		function resetMessage(){
	    	vm.selectVideoMessage = ""
	    }
	    
	    function addLine () {
			if($window.selectedPolygons.length!=2){
				alert("2 adet seçim yapmalısınız");
				return;
			}
			vm.addMessage = vm.addMessageBefore;
            //Polygon.save(vm.polygon, onPolygonSaveSuccess, onSaveError);
            vm.adding = false;
            
            for(var i=0;i<selectedPolygons.length;i++){
				selectedPolygons[i].fill("lightgreen");;
			}
            $window.selectedPolygons = [];
            
        } 

		function onPolygonSaveSuccess (result) {
             Polygon.getPolygonListByScenarioId({id:vm.scenario.id},getPolygonListSuccess,onSaveError);
        }

		function getPolygonListSuccess(result){
			vm.polygons = result;
		}

		function addPolygon () {
	    	vm.addMessage = vm.addMessageAfter;
	    	vm.adding = true;
	    }
	    
	    function getPolygonList () {
	    	Polygon.getPolygonListByScenarioId({id:vm.scenario.id},getPolygonListSuccess,onSaveError);
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


    }
})();
