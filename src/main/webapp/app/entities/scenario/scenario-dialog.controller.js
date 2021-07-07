(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('ScenarioDialogController', ScenarioDialogController);

    ScenarioDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Scenario', 'Video','$window'];

    function ScenarioDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Scenario, Video,$window) {
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

		vm.addMessageBefore = "Çizgi eklemek için, Ekle butonuna basınız"
	    vm.addMessageAfter = "Çizgi ekleyebilirsiniz"
	    vm.addMessage = vm.addMessageBefore;
	    vm.selectVideoMessage = ""
	    vm.polygons = []; 
	    vm.polygon = [];
	    vm.points = $window.points;

		function resetMessage(){
	    	vm.selectVideoMessage = ""
	    }

		function deleteAll () {
	    	$window.points = [];
            $window.poly = null;
            vm.polygons = [];
	    }

		function addToPolygonList () {
	        var i;
	        vm.polygon = new Object();
			vm.polygon.id = $window.tempId;
			vm.polygon.name = "1";
			vm.polygon.data = [];
			
			for (i = 0; i < $window.points.length; i++) {
  				vm.polygon.data.push($window.points[i]);
			} 	
            vm.polygons.push(vm.polygon);
            $window.points = [];
            $window.poly = null;
            vm.addMessage = vm.addMessageBefore;
        } 


		 function changeMessage () {
	    	vm.addMessage = vm.addMessageAfter;
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
