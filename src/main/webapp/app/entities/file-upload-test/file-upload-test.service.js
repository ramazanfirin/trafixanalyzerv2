(function() {
    'use strict';
    angular
        .module('trafficanalzyzerv2App')
        .factory('FileUploadTest', FileUploadTest);

    FileUploadTest.$inject = ['$resource'];

    function FileUploadTest ($resource) {
        var resourceUrl =  'api/file-upload-tests/:id';

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
