(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .controller('AnalyzeOrderResultController', AnalyzeOrderResultController);

    AnalyzeOrderResultController .$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Scenario', 'Video','$window','Polygon','Line','VideoRecord','$translate','$location'];

    function AnalyzeOrderResultController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Scenario, Video,$window,Polygon,Line, VideoRecord,$translate,$location) {
        var vm = this;

		vm.analyzeOrder = entity;
        vm.scenario = entity.scenario;
        vm.clear = clear;
        vm.save = save;
        vm.videos = Video.query();
		//vm.update = update;
		vm.addPolygon = addPolygon;
		//vm.addToPolygonList = addToPolygonList;
		vm.resetMessage = resetMessage;
		//vm.deletePolygon = deletePolygon;
		vm.adding = false;
		//vm.cancelPolygon = cancelPolygon;
		vm.addLine = addLine;
		vm.deleteLine = deleteLine;
		vm.report =[];
		vm.updateLine=updateLine;

		vm.addMessageBefore = "Çizgi eklemek için, Ekle butonuna basınız"
	    vm.addMessageAfter = "Çizgi ekleyebilirsiniz"
	    vm.addMessage = vm.addMessageBefore;
	    vm.selectVideoMessage = ""
	    vm.polygons = []; 
	    vm.lines = []; 
	    vm.polygon = [];
	    vm.speedPolygons = [];
	    vm.points = $window.points;
	    vm.showPopup = showPopup;
	    vm.hidePopup = hidePopup;
	    //vm.createPolygon = vm.createPolygon();
	    vm.polygonType="COUNTING";

		vm.showClassification=showClassification;
		vm.showCounting=showCounting;
		vm.showSpeed=showSpeed;
		vm.showExcel=showExcel;
		vm.showDirectionResult = showDirectionResult;
		
		vm.showClassificationTab=true;
		vm.showCountingTab=false;
		vm.showSpeedTab=false;
		vm.showExcelTab=false;
		vm.showDirectionResultTab=false;

		vm.showClassificationTabActive="active";
		vm.showCountingTabActive="";
		vm.showSpeedTabActive="";
		vm.showExcelTabActive="";
		vm.showDirecitonResultTabActive="";

		vm.classificationData=[];
		vm.classificationChartList=[];

		vm.speedChartList=[];
		vm.directionReportResult=[];
		vm.language="tr";
		vm.baseUrl='http://'+$location.host()+':'+$location.port();
		
		loadAll();

		vm.myJson = {
  type: 'line',
  series: [
    { values: [54,23,34,23,43] },
    { values: [10,15,16,20,40] }
  ]
};


vm.myJson2 = {
    type: "pie",
    title: {
      textAlign: 'center',
      text: "My title"
    },
    plot: {
      slice: 50 //to make a donut
    },
    series: [{
      values: [3],
      text: "Total Commits"

    }, {
      values: [4],
      text: "Issues Solved"

    }, {
      values: [8],
      text: "Issues Submitted"
    }, {
      values: [7],
      text: "Number of Clones"

    }]
  };
		
        
        function loadAll () {
        //$("#exampleModal").modal("show");
        	console.log($translate.use());
        	vm.language=$translate.use();
        	if(vm.language=="tr"){
        		vm.downloadExcel = "Excel Indir"
        	}else{
         		vm.downloadExcel = "Download Excel File"
         	       
        	}
        	
        	
        	VideoRecord.getResultOfAnalyzeOrder({id:entity.id},getResultOfAnalyzeOrderSucccess,onSaveError)
        	
        	
        	if(vm.scenario.id == null)
        		Scenario.save(vm.scenario, onSaveSuccessFirst, onSaveError);
        	else{
        	//
        		Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListForFirstTime,onSaveError);
        		//Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:'SPEED'},getSpeedPolygonsSuccess,onSaveError);
        	}	
        		
        		
        }
        
        function getPolygonListForFirstTime(result){
			//resetAll();
			vm.polygons = result;
			$scope.$broadcast('ploygonDataReceived', "ramazan");
			Line.getLineListByScenarioId({id:vm.scenario.id},getLineListForFirstTime,onSaveError);
		}
		
		function getLineListForFirstTime(result){
			vm.lines = result;
			getClassificationData();
			$scope.$broadcast('lineDataReceived', "ramazan");
		}
        
        function getClassificationData(){
        	VideoRecord.getClassificationData({id:vm.analyzeOrder.id},getClassificationDataSuccess,onSaveError);
        	VideoRecord.getAverageSpeedData({id:vm.analyzeOrder.id},getAverageSpeedDataSuccess,onSaveError);
        	VideoRecord.getResultOfDirectionReport({id:vm.analyzeOrder.id},getResultOfDirectionReportSuccess,onSaveError);
        }
        
        function getClassificationDataSuccess(result){
        	vm.classificationData = result;
        	for(var i=0;i<result.length;i++){
        		var item = result[i];
        		var chart = createClassificationChart(item);
        		vm.classificationChartList.push(chart);
        	}
        }
        
        function getAverageSpeedDataSuccess(result){
        	vm.classificationData = result;
        	for(var i=0;i<result.length;i++){
        		var item = result[i];
        		var chart = createSpeedChart(item);
        		vm.speedChartList.push(chart);
        	}
        }
        
        function getResultOfDirectionReportSuccess(result){
        	vm.directionReportResult = result;
        	
        }
        
        function getSpeedPolygonsSuccess(result){
        	vm.speedPolygons = result;
        	$scope.$broadcast('speedPloygonDataReceived', "ramazan");
        }
        
        function getResultOfAnalyzeOrderSucccess(result){
        	vm.report = result;
        }
        
        function onSaveSuccessFirst (result) {
           vm.scenario = result;
           Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
        }

		function resetMessage(){
	    	vm.selectVideoMessage = ""
	    }
	    
	    function addLine () {
			
			Line.createLineByPolygons({
				scenarioId:vm.scenario.id,
				startPolygonId:selectedPolygons[0].attrs.id,
				endPolygonId:selectedPolygons[1].attrs.id,
				name:vm.addLineName
			},addLineSuccess,onSaveError);
            
        } 
        
        function addLineSuccess(){
 			vm.addMessage = vm.addMessageBefore;
            vm.adding = false;
            
//            for(var i=0;i<selectedPolygons.length;i++){
//				selectedPolygons[i].fill("lightgreen");;
//			}
            resetAll();
			vm.addLineName= "";
			hidePopup();
 			       
        }
        
        function resetAll(){
        
        	deleteFromScreen($window.selectedPolygons);	
        	deleteFromScreen($window.polygonListOnScreen);	
        	deleteFromScreen($window.lineListOnScreen);
        	deleteFromScreen($window.textListOnScreen);
        	
        
        	$window.selectedPolygons = [];
            $window.polygonListOnScreen=[];
			$window.lineListOnScreen=[];
			$window.textListOnScreen=[];
            $window.selectedPolygons = [];
            $window.poly = null;
            //loadAll();
            //$window.layer = new Konva.Layer();
        }
        
        function deleteFromScreen(list){
        	 for(var i=0;i<list.length;i++){
				list[i].destroy();
			}
        }
        function showPopup(){
        	if($window.selectedPolygons.length!=2){
				alert("2 adet seçim yapmalısınız");
				return;
			}
			
			$window.modal.style.display = "block";
        }
		
		function hidePopup(){
			$window.modal.style.display = "none";
        
		}

		function deleteLine(lineId){
			 
			 Line.delete({id: lineId},loadAll,onSaveError);
			 
		}
		
		function onPolygonSaveSuccess (result) {
             Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
        }

		function getPolygonListSuccess(result){
			vm.polygons = result;
		}

		function addPolygon () {
	    	vm.addMessage = vm.addMessageAfter;
	    	vm.adding = true;
	    }
	    
	    function getPolygonList () {
	    	Polygon.getPolygonListByScenarioId({id:vm.scenario.id,type:vm.polygonType},getPolygonListSuccess,onSaveError);
	    }

		function updateLine () {
			if(vm.selectedLine==null)
				VideoRecord.getResultOfAnalyzeOrder({id:entity.id},getResultOfAnalyzeOrderSucccess,onSaveError);
			else
			    VideoRecord.getResultOfAnalyzeOrderByLineId({id:entity.id,lineId:vm.selectedLine.id},updateLineSuccess,onSaveError);
	    }
		
		function updateLineSuccess(result){
			vm.report = result;
		}
		
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
//            if(vm.polygons == null || vm.polygons.length==0){
//            	alert('Senaryo içerisine polygon bulunmadığı için, senaryo silinecektir.Onaylıyor musunuz ? ');
//            	Scenario.delete({id:vm.scenario.id});
//            }
        }

        function save () {
//         $uibModalInstance.close();
//            vm.isSaving = true;
//            if (vm.scenario.id !== null) {
//                Scenario.update(vm.scenario, onSaveSuccess, onSaveError);
//            } else {
//                Scenario.save(vm.scenario, onSaveSuccess, onSaveError);
//            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('trafficanalzyzerv2App:scenarioUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

		function resetTabs(){
			//alert('sdf');
			vm.showClassificationTab=false;
			vm.showCountingTab=false;
			vm.showSpeedTab=false;
			vm.showExcelTab=false;
			vm.showDirectionResultTab=false;
			
			vm.showClassificationTabActive = "";
			vm.showCountingTabActive="";
			vm.showSpeedTabActive="";
			vm.showExcelTabActive="";
			vm.showDirectionResultTabActive="";
		}

		function showClassification(){
			resetTabs();
			vm.showClassificationTab=true;
			vm.showClassificationTabActive = "active";
		}
		
		function showCounting(){
			resetTabs();
			vm.showCountingTab=true;
			vm.showCountingTabActive="active";
		}
		
		function showSpeed(){
			resetTabs();
			vm.showSpeedTab=true;
			vm.showSpeedTabActive="active";
		}
		
		function showDirectionResult(){
			resetTabs();
			vm.showDirectionResultTab=true;
			vm.showDirectionResultTabActive="active";
		}
		
		function showExcel(){
			resetTabs();
			vm.showExcelTab=true;
			vm.showSpeedTabActive="";
		}
		
		
		
		
		
		
		
		
		
		
		function createClassificationChart(item){
        	var myJson2 = {
				    type: "pie",
				    title: {
				      textAlign: 'center',
				      text: item.lineName
				    },
				     "legend": {
    "x": "75%",
    "y": "15%",
    "border-width": 1,
    "border-color": "gray",
    "border-radius": "5px",
    "header": {
      "text": "Legend",
      "font-family": "Georgia",
      "font-size": 12,
      "font-color": "#3333cc",
      "font-weight": "normal"
    },
    "marker": {
      "type": "circle"
    },
    "toggle-action": "remove",
    "minimize": true,
    "icon": {
      "line-color": "#9999ff"
    },
    "max-items": 8,
    "overflow": "scroll"
  },
				    plot: {
				      slice: 50, //to make a donut
				      'value-box': {
					      text: "%pie-total-value",
					      placement: "center",
					      'font-color': "black",
					      'font-size':35,
					      'font-family': "Georgia",
					      'font-weight': "normal",
					      rules: [
					        {
					          rule: "%p != 0",
					          visible: false
					        }
      						]
   					 },
   					 tooltip: {
				      text: "%t: %v (%npv%)",
				      'font-color': "black",
				      'font-family': "Georgia",
				      'text-alpha':1,
				      'background-color': "white",
				      alpha:0.7,
				      'border-width': 1,
				      'border-color': "#cccccc",
				      'line-style': "dotted",
				      'border-radius': "10px",
				      padding: "10%",
				      placement: "node:out" //"node:out" or "node:center"
   					 },
				    },
				    series: []
			}	    
			
			for(var i=0;i<item.datas.length;i++){
				var data=item.datas[i];
				var seriesData = createSeriesData(data);
				myJson2.series.push(seriesData);
			}
			
			return myJson2;
        }
        
        function createSeriesData(data){
        	var values = {
		      values: [data.count],
		      text: data.type
		    }
		    
		    return values;
        }
        
        
        
        function createSpeedChart(item){
			var myConfig6 = {
				"type": "gauge",
				"title": {
					"textAlign": 'center',
					"text": item.lineName
				},
				"scale-r": {
					"aperture": 200,
					"values": "0:200:20",
					"item": {    //Scale Label Styling
						'font-size': 12,
						'font-weight': "bold",     //or "normal"
						'font-style': "normal",    //or "italic"
						'offset-r': -60  //To adjust the placement of your scale labels.
						//To adjust the angle of your scale labels.
					},
					"center": {
						"size": 35,
						"background-color": "#66CCFF #FFCCFF",
						"border-color": "none"
					},
					"ring": {  //Ring with Rules
						"size": 20,
						"rules": [
							{
								"rule": "%v >= 40 && %v <= 80",
								"background-color": "blue"
							},

							{
								"rule": "%v >= 80 && %v <= 120",
								"background-color": "green"
							},
							{
								"rule": "%v >= 120 && %v <=160",
								"background-color": "yellow"
							},
							{
								"rule": "%v >= 160 && %v <=180",
								"background-color": "red"
							}
						]
					}
				},
				"plot": {
					"csize": "10%",
					"size": "100%",
					"background-color": "#000000",
					"valueBox": {
						placement: 'center',
						text: '%v km/s', //default
						fontSize: 15,
					}
				},
				"series": [
					{ "values": [item.averageSpeed] }
				]
			};

			
			return myConfig6;
		}
        
    }
})();
