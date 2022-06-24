(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderController', AnalyzeOrderController);

    AnalyzeOrderController.$inject = ['$state', 'DataUtils', 'AnalyzeOrder', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Location'];

    function AnalyzeOrderController($state, DataUtils, AnalyzeOrder, ParseLinks, AlertService, paginationConstants, pagingParams, Location) {

        var vm = this;

		vm.search = search;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        
        vm.locations = Location.query();
 		vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        
        var currentDate = new Date(); 
        currentDate.setHours(0);
        currentDate.setMinutes(0);
        vm.startDate = currentDate;
        currentDate.setHours(23);
        currentDate.setMinutes(59);
 		vm.endDate = currentDate ;
	    vm.showHideVisilationWindow = showHideVisilationWindow;
 		
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
        
        function search () {
			if(vm.location == null)
				alert('Lokasyon Seçimi Yapınız.');
		
            AnalyzeOrder.search({
                locationId: vm.location.id,
                startDate: vm.startDate,
                endDate: vm.endDate
            }, onSuccessSearch, onError);
            function onSuccessSearch(data, headers) {
                vm.analyzeOrders = data;
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
		
		function showHideVisilationWindow(){
			AnalyzeOrder.switchVisulationWindow();
		}
		
		function onSaveError(error) {
                AlertService.error(error.data.message);
            }
       vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }    
    }
})();
