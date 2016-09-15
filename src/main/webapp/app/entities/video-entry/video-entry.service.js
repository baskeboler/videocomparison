(function() {
    'use strict';
    angular
        .module('testappApp')
        .factory('VideoEntry', VideoEntry);

    VideoEntry.$inject = ['$resource'];

    function VideoEntry ($resource) {
        var resourceUrl =  'api/video-entries/:id';

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
