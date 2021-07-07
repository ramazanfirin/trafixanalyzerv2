(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('VideoRecordDialogController', VideoRecordDialogController);

    VideoRecordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VideoRecord', 'Video', 'Line', 'Direction'];

    function VideoRecordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, VideoRecord, Video, Line, Direction) {
        var vm = this;

        vm.videoRecord = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.videos = Video.query();
        vm.lines = Line.query();
        vm.directions = Direction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.videoRecord.id !== null) {
                VideoRecord.update(vm.videoRecord, onSaveSuccess, onSaveError);
            } else {
                VideoRecord.save(vm.videoRecord, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:videoRecordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.insertDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
