(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('ScenarioDialogController', ScenarioDialogController);

    ScenarioDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Scenario', 'Video','$window','Polygon'];

    function ScenarioDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Scenario, Video,$window,Polygon) {
        var vm = this;

        vm.scenario = entity;
        vm.clear = clear;
        vm.save = save;
        vm.videos = Video.query();
		vm.update = update;
		vm.changeMessage = changeMessage;
		vm.addToPolygonList = addToPolygonList;
		vm.deleteAll = deleteAll;
		vm.resetMessage = resetMessage;
		vm.deletePolygon = deletePolygon;

		vm.addMessageBefore = "Çizgi eklemek için, Ekle butonuna basınız"
	    vm.addMessageAfter = "Çizgi ekleyebilirsiniz"
	    vm.addMessage = vm.addMessageBefore;
	    vm.selectVideoMessage = ""
	    vm.polygons = []; 
	    vm.polygon = [];
	    vm.points = $window.points;

		loadAll();

        function loadAll () {
        	Scenario.save(vm.scenario, onSaveSuccessFirst, onSaveError);
        }
        function onSaveSuccessFirst (result) {
           vm.scenario = result;
        }

		function resetMessage(){
	    	vm.selectVideoMessage = ""
	    }
	    
	    function deletePolygon(id,index){
	    	 Polygon.deletePolygonById({id: id},getPolygonList,onSaveError);
	    	 deleteFromScreen(index);
	    }
	    
	    function deleteFromScreen(index){
	    	for (var i = 0; i < $window.polygonListOnScreen.length; i++) {
	    		var poly = $window.polygonListOnScreen[i];
	    		if(poly.attrs.id==index){
	    			//alert(index);
	    			poly.destroy();
  					$window.layer.draw();
	    		}
	    	}
	    	
	    	for (var i = 0; i < $window.textListOnScreen.length; i++) {
	    		var text = $window.textListOnScreen[i];
	    		if(text.attrs.text==index){
	    			//alert(index);
	    			text.destroy();
  					$window.layer.draw();
	    		}
	    	}
	    }

		function deleteAll () {
	    	$window.points = [];
            $window.poly = null;
            vm.polygons = [];
	    }

		function addToPolygonList () {
	        var i;
			
			vm.polygon = new Object();
			vm.polygon.name = $window.tempId;
            vm.polygon.points="";
            for (i = 0; i < $window.points.length; i++) {
  				if(vm.polygon.points=="")
  					vm.polygon.points = $window.points[i]; 
  				else
  					vm.polygon.points = vm.polygon.points+";"+$window.points[i];
			} 	
            
            $window.points = [];
            $window.poly = null;
            vm.addMessage = vm.addMessageBefore;
            vm.polygon.scenario = vm.scenario;
			Polygon.save(vm.polygon, onPolygonSaveSuccess, onSaveError);
            
        } 

		function onPolygonSaveSuccess (result) {
             Polygon.getPolygonListByScenarioId({id:vm.scenario.id},getPolygonListSuccess,onSaveError);
             //alert('abc');         
        }

		function getPolygonListSuccess(result){
			vm.polygons = result;
//			for (i = 0; i < vm.polygons.length; i++) {
//  				if(vm.polygon.points=="")
//  					vm.polygon.points = $window.points[i]; 
//  				else
//  					vm.polygon.points = vm.polygon.points+";"+$window.points[i];
//			}
		}

		 function changeMessage () {
	    	vm.addMessage = vm.addMessageAfter;
	    }

		function getPolygonList () {
	    	Polygon.getPolygonListByScenarioId({id:vm.scenario.id},getPolygonListSuccess,onSaveError);
	    }

		function update(){
	    	vm.selectVideoMessage = "Lütfen Video görüntüsünün gelmesini bekleyiniz";
	    	
	    	console.log("update bailadı");
			$window.imageObj1.src = 'http://localhost:8080/api/videos/image/'+vm.scenario.video.id;
			$window.layer.draw();
			console.log("update bitti");
	    	//setTimeout(resetMessage, 1);
	    	resetMessage();
	    	
	    }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
            if(vm.polygons == null || vm.polygons.length==0){
            	alert('Senaryo içerisine polygon bulunmadığı için, senaryo silinecektir.Onaylıyor musunuz ? ');
            	Scenario.delete({id:vm.scenario.id});
            }
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
