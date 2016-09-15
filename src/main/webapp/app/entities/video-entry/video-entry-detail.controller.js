(function() {
    'use strict';

    angular
        .module('testappApp')
        .controller('VideoEntryDetailController', VideoEntryDetailController);

    VideoEntryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VideoEntry'];

    function VideoEntryDetailController($scope, $rootScope, $stateParams, previousState, entity, VideoEntry) {
        var vm = this;

        vm.videoEntry = entity;
        vm.previousState = previousState.name;
        vm.video1Config= {
            sources: [
                {
                    src: '/api/video-entries/' + vm.videoEntry.id + '/first',
                    type: 'video/mpg'
                }
            ]
        };
        var unsubscribe = $rootScope.$on('testappApp:videoEntryUpdate', function(event, result) {
            vm.videoEntry = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
