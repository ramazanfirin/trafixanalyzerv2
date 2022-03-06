(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('Scenario', Scenario);

    Scenario.$inject = ['$resource'];

    function Scenario ($resource) {
        var resourceUrl =  'api/scenarios/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'insertScreenshot': { method: 'POST', isArray: false,url: 'api/scenarios/insertScreenshot'},
			'getAll': { method: 'GET', isArray: true, url: '/api/scenarios/getAll?page=0&size=1000&sort=id,desc'},
			'search': { method: 'GET', isArray: true, url: '/api/scenarios/search'},
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
