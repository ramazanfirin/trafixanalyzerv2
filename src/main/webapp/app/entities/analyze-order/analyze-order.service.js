(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('AnalyzeOrder', AnalyzeOrder);

    AnalyzeOrder.$inject = ['$resource', 'DateUtils'];

    function AnalyzeOrder ($resource, DateUtils) {
        var resourceUrl =  'api/analyze-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
