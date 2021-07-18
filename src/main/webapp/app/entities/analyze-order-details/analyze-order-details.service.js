(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('AnalyzeOrderDetails', AnalyzeOrderDetails);

    AnalyzeOrderDetails.$inject = ['$resource', 'DateUtils'];

    function AnalyzeOrderDetails ($resource, DateUtils) {
        var resourceUrl =  'api/analyze-order-details/:id';

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
