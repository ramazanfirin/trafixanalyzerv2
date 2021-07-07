(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('CameraRecord', CameraRecord);

    CameraRecord.$inject = ['$resource', 'DateUtils'];

    function CameraRecord ($resource, DateUtils) {
        var resourceUrl =  'api/camera-records/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.insertDate = DateUtils.convertDateTimeFromServer(data.insertDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
