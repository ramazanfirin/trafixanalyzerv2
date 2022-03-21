(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderReportController', AnalyzeOrderReportController);

    AnalyzeOrderReportController.$inject = ['$state', 'DataUtils', 'AnalyzeOrder', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Location','Scenario','VideoRecord','$window'];

    function AnalyzeOrderReportController($state, DataUtils, AnalyzeOrder, ParseLinks, AlertService, paginationConstants, pagingParams, Location,Scenario,VideoRecord,$window) {

        var vm = this;

		vm.search = search;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = openFile;
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
	    //vm.scenarios = Scenario.getAll()
        vm.getScenarioByLocationId = getScenarioByLocationId;
 		
		
        function search () {
			if(vm.location == null || vm.scenario == null){
				alert('Tüm Alanları Doldurunuz');
				return;
			}	
			
		
		VideoRecord.getDirectionReportByScnario({id:vm.scenario.id},getDirectionReportByScnarioSuccess,onSaveError);
		
		  
		    
        }

		function getDirectionReportByScnarioSuccess(result){
			console.log("ffdfsdfs");
			vm.result = result;
			
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

		function getScenarioByLocationId(){
			console.log(vm.location);
			Scenario.findScenarioListByLocationId({id:vm.location.id},getScenarioByLocationIdSuccess,onSaveError);
		}
		
		function getScenarioByLocationIdSuccess(result){
			vm.scenarios = result;
		}	
		
		function openFile(id){
			$window.open($window.location.origin+'/api/analyze-orders/getScreenShoot/'+id,'_blank','height=468,width=832');
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
