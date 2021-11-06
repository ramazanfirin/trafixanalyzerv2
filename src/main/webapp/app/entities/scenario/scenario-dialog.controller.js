(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('ScenarioDialogController', ScenarioDialogController);

    ScenarioDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Scenario', 'Video','$window','Polygon','$location'];

    function ScenarioDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Scenario, Video,$window,Polygon,$location) {
        var vm = this;

        vm.scenario = entity;
        vm.clear = clear;
        vm.save = save;
        //vm.videos = Video.query();
		vm.videos = Video.getAll();
		vm.insertScreenshot = insertScreenshot;
		vm.addPolygon = addPolygon;
		vm.addToPolygonList = addToPolygonList;
		vm.resetMessage = resetMessage;
		vm.deletePolygon = deletePolygon;
		vm.adding = false;
		vm.cancelPolygon = cancelPolygon;

		vm.addMessageBefore = "Çizgi eklemek için, Ekle butonuna basınız"
	    vm.addMessageAfter = "Çizgi ekleyebilirsiniz"
	    vm.addMessage = vm.addMessageBefore;
	    vm.selectVideoMessage = ""
	    vm.polygons = []; 
	    vm.polygon = [];
	    vm.points = $window.points;
	    //vm.createPolygon = vm.createPolygon();
		vm.polygonType="COUNTING";
		
		vm.baseUrl='http://'+$location.host()+':'+$location.port();

		loadAll();

        function loadAll () {
        	if(vm.scenario.id == null)
        		Scenario.save(vm.scenario, onSaveSuccessFirst, onSaveError);
        	else{
        		Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListForFirstTime,onSaveError);
        	}	
        		
        }
        
        function getPolygonListForFirstTime(result){
			vm.polygons = result;
			console.log("from controller:"+vm.polygons.length);
			$scope.$broadcast('messagename', "ramazan");
		}
        
        function onSaveSuccessFirst (result) {
           vm.scenario = result;
           Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
        }

		function resetMessage(){
	    	vm.selectVideoMessage = ""
	    }
	    
	    function deletePolygon(id,index){
	    	 Polygon.deletePolygonById({id: id},deletePolygonSuccess,onSaveError);
	    }
	    
	    function deletePolygonSuccess(result,headers){
	   		 if(headers('X-trafficanalzyzerv2App-error')=="error.1001"){
	   		 	alert("Bu polygon çizgilerde kullanıldığı için silinemez. Önce çizgiyi siliniz");
	   		 	return;
	   		 }else{
	   		 	refresh();
	   		 	getPolygonList();
	     	 	//deleteFromScreen(index);
	    	}
	    }
	    
	    function refresh(){
	    	for (var i = 0; i < $window.polygonListOnScreen.length; i++) {
	    		var poly = $window.polygonListOnScreen[i];
	    		poly.destroy();
	    	}
	    	
	    	for (var i = 0; i < $window.textListOnScreen.length; i++) {
	    		var text = $window.textListOnScreen[i];
	    		text.destroy();
	    	
	    	}
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

		function addToPolygonList () {

			vm.polygon = new Object();
			vm.polygon.name = $window.tempId;
            vm.polygon.points="";
            for (var i = 0; i < $window.points.length; i++) {
  				if(vm.polygon.points=="")
  					vm.polygon.points = $window.points[i]; 
  				else
  					vm.polygon.points = vm.polygon.points+";"+$window.points[i];
			} 	
            
            $window.points = [];
            $window.poly = null;
            vm.addMessage = vm.addMessageBefore;
            vm.polygon.scenario = vm.scenario;
            vm.polygon.type=vm.polygonType;
			Polygon.save(vm.polygon, onPolygonSaveSuccess, onSaveError);
            vm.adding = false;
        } 

		function onPolygonSaveSuccess (result) {
             refresh();
             Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
             //alert('abc');         
        }

		function getPolygonListSuccess(result){
			vm.polygons = result;
			console.log("from controller:"+vm.polygons.length);
			$scope.$broadcast('messagename', "ramazan");
		}

		 function addPolygon () {
	    	vm.addMessage = vm.addMessageAfter;
	    	vm.adding = true;
	    }
	    
	    function cancelPolygon () {
	    	vm.addMessage = vm.addMessageBefore;
            vm.adding = false;
            $window.points = [];
            $window.poly = null;
            deleteFromScreen($window.tempId);
	    }

		function getPolygonList () {
	    	Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
	    }

		function insertScreenshot(){
	    	Scenario.insertScreenshot(vm.scenario, insertScreenshotSuccess, onSaveError);
	    }
	    
	    function insertScreenshotSuccess(){
	    	vm.selectVideoMessage = "Lütfen Video görüntüsünün gelmesini bekleyiniz";
	    	console.log("update bailadı");
			//$window.imageObj1 = new Image();
			$window.imageObj1.src = 'http://'+$location.host()+':'+$location.port()+'/api/scenarios/getScreenShoot/'+vm.scenario.id;
			$window.layer.draw();
			console.log("update bitti");
	    	//setTimeout(resetMessage, 1);
	    	resetMessage();
	    	//$window.location.reload();
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
