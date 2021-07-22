(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('VideoDialogController', VideoDialogController);

    VideoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Video', 'Location', '$window'];

    function VideoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Video, Location,  $window) {
        var vm = this;

        vm.video = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.locations = Location.query();
        vm.ftpDirectoryPath = ""	;
        
        getFtpDirectoryPath();
        
        function getFtpDirectoryPath(){
        	Video.getFtpDirectoryPath({},getFtpDirectoryPathSucccess,onSaveError)
        }
        
        function getFtpDirectoryPathSucccess(result){
			vm.ftpDirectoryPath = result.value;        
        }
        
        $window.addEventListener("onPickItem", function(evt) {
    		vm.video.path = vm.ftpDirectoryPath+evt.detail;
    		vm.video.name = evt.detail
    		//alert(evt.detail + " dosyasını seçtiniz");
		}, false);

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.video.id !== null) {
                Video.update(vm.video, onSaveSuccess, onSaveError);
            } else {
                Video.save(vm.video, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:videoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.processDate = false;
        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
