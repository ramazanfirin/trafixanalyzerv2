(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('FileUploadTestDetailController', FileUploadTestDetailController);

    FileUploadTestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'FileUploadTest'];

    function FileUploadTestDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, FileUploadTest) {
        var vm = this;

        vm.fileUploadTest = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('trafficanalzyzerv2App:fileUploadTestUpdate', function(event, result) {
            vm.fileUploadTest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
