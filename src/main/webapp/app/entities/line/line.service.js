(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('Line', Line);

    Line.$inject = ['$resource'];

    function Line ($resource) {
        var resourceUrl =  'api/lines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'createLineByPolygons': { method: 'POST', isArray: false, url: 'api/lines/createLineByPolygons'},
            'getLineListByScenarioId': { method: 'GET', isArray: true, url: 'api/lines/getLineListByScenarioId/:id'},
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
