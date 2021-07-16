(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('RawRecord', RawRecord);

    RawRecord.$inject = ['$resource', 'DateUtils'];

    function RawRecord ($resource, DateUtils) {
        var resourceUrl =  'api/raw-records/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.time = DateUtils.convertDateTimeFromServer(data.time);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
