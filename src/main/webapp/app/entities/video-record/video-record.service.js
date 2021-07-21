(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('VideoRecord', VideoRecord);

    VideoRecord.$inject = ['$resource', 'DateUtils'];

    function VideoRecord ($resource, DateUtils) {
        var resourceUrl =  'api/video-records/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'getResultOfAnalyzeOrder': { method: 'GET', isArray: true,url:'/api/video-records/getResultOfAnalyzeOrder/:id'},
            'getResultOfAnalyzeOrderByLineId': { method: 'GET', isArray: true,url:'/api/video-records/getResultOfAnalyzeOrderAndLineId/:id/:lineId'},
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
