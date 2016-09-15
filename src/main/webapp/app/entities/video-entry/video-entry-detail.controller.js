(function() {
    'use strict';

    angular
        .module('testappApp')
        .controller('VideoEntryDetailController', VideoEntryDetailController);

    VideoEntryDetailController.$inject = ['$sce', '$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VideoEntry'];

    function VideoEntryDetailController($sce, $scope, $rootScope, $stateParams, previousState, entity, VideoEntry) {
        var vm = this;

        vm.videoEntry = entity;
        vm.previousState = previousState.name;
        vm.videoUrl = '/api/uploads/' + vm.videoEntry.id + '/first';
        vm.video1Config= {
            sources: [
                {
                    src: $sce.trustAsResourceUrl(vm.videoUrl),
                    type: 'video/mp4'
                }
            ],
            theme: {
                url: "http://www.videogular.com/styles/themes/default/latest/videogular.css"
            }
        };
        vm.video2Config= {
            sources: [
                {
                    src: $sce.trustAsResourceUrl(vm.videoUrl),
                    type: 'video/mp4'
                }
            ],
            theme: {
                url: "http://www.videogular.com/styles/themes/default/latest/videogular.css"
            }
        };
        var unsubscribe = $rootScope.$on('testappApp:videoEntryUpdate', function(event, result) {
            vm.videoEntry = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
