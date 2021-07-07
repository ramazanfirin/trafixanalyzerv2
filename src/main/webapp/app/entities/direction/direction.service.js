(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('Direction', Direction);

    Direction.$inject = ['$resource'];

    function Direction ($resource) {
        var resourceUrl =  'api/directions/:id';

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
