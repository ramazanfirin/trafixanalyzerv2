(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('Video', Video);

    Video.$inject = ['$resource', 'DateUtils'];

    function Video ($resource, DateUtils) {
        var resourceUrl =  'api/videos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'getFtpDirectoryPath': { method: 'GET', isArray: false, url: '/api/videos/getFtpDirectoryPath'},
			'getAll': { method: 'GET', isArray: true, url: '/api/videos/getAll?page=0&size=1000&sort=id,desc'},
			'search': { method: 'GET', isArray: true, url: '/api/videos/search'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.processDate = DateUtils.convertDateTimeFromServer(data.processDate);
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
