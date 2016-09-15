(function() {
    'use strict';

    angular
        .module('testappApp')
        .controller('VideoEntryDeleteController',VideoEntryDeleteController);

    VideoEntryDeleteController.$inject = ['$uibModalInstance', 'entity', 'VideoEntry'];

    function VideoEntryDeleteController($uibModalInstance, entity, VideoEntry) {
        var vm = this;

        vm.videoEntry = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VideoEntry.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
