(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('Location', Location);

    Location.$inject = ['$resource'];

    function Location ($resource) {
        var resourceUrl =  'api/locations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'getAll': { method: 'GET', isArray: true, url: '/api/locations/getAll'},
            'search': { method: 'GET', isArray: true, url: '/api/locations/search'},
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
