(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderController', AnalyzeOrderController);

    AnalyzeOrderController.$inject = ['$state', 'DataUtils', 'AnalyzeOrder', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function AnalyzeOrderController($state, DataUtils, AnalyzeOrder, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
 		
		vm.play = play;
        loadAll();

        function loadAll () {
            AnalyzeOrder.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.analyzeOrders = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

		function play(analyzeorder) {
           //alert(analyzeorder.id);
		   AnalyzeOrder.play({id:analyzeorder.id},playSuccess,onSaveError);
        }

		function playSuccess(result){
			//resetAll();
			
		}
		
		function onSaveError(error) {
                AlertService.error(error.data.message);
            }
    }
})();
