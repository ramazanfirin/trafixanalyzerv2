(function () {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
