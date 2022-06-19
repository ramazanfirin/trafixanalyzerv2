(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('AnalyzeOrder', AnalyzeOrder);

    AnalyzeOrder.$inject = ['$resource'];

    function AnalyzeOrder ($resource) {
        var resourceUrl =  'api/analyze-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
			'getLogs': { method: 'GET', isArray: false,url: 'api/analyze-orders/getLogs/:id'},
			'play': { method: 'GET', isArray: false,url: 'api/analyze-orders/play/:id'},
			'search': { method: 'GET', isArray: true,url: 'api/analyze-orders/search'},
			'startAnalyzeOrdersMultiple': { method: 'POST', isArray: false,url: 'api/analyze-orders-multiple'},
			
			
			'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
