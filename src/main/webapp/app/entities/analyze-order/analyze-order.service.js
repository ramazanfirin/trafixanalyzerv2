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
