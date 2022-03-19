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
            'getClassificationData': { method: 'GET', isArray: true, url: '/api/video-records/getClassificationData/:id'},
            'getAverageSpeedData': { method: 'GET', isArray: true, url: '/api/video-records/getAverageSpeedData/:id'},
            'getResultOfDirectionReport': { method: 'GET', isArray: true, url: '/api/video-records/getResultOfDirectionReport/:id'},
            'getVisulationData': { method: 'GET', isArray: true, url: '/api/video-records/getVisulationData/:id'},
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
