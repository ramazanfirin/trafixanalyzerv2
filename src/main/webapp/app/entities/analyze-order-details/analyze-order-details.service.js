(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('AnalyzeOrderDetails', AnalyzeOrderDetails);

    AnalyzeOrderDetails.$inject = ['$resource'];

    function AnalyzeOrderDetails ($resource) {
        var resourceUrl =  'api/analyze-order-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
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
