(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('FileUploadTestController', FileUploadTestController);

    FileUploadTestController.$inject = ['DataUtils', 'FileUploadTest'];

    function FileUploadTestController(DataUtils, FileUploadTest) {

        var vm = this;

        vm.fileUploadTests = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            FileUploadTest.query(function(result) {
                vm.fileUploadTests = result;
                vm.searchQuery = null;
            });
        }
    }
})();
