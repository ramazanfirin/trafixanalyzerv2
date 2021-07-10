(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('Polygon', Polygon);

    Polygon.$inject = ['$resource'];

    function Polygon ($resource) {
        var resourceUrl =  'api/polygons/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'getPolygonListByScenarioId': { method: 'GET', isArray: true,url: 'api/polygons/getPolygonListByScenarioId/:id'},
            'deletePolygonById': { method: 'GET', isArray: true,url: 'api/polygons/deletePolygonById/:id'},
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
