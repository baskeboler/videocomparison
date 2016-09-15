(function () {
    'use strict';

    angular
        .module('testappApp')
        .controller('VideoEntryDialogController', VideoEntryDialogController);

    VideoEntryDialogController.$inject = [
        '$timeout', '$scope', '$stateParams',
        '$uibModalInstance', 'entity', 'VideoEntry',
        'Upload'
    ];

    function VideoEntryDialogController($timeout, $scope, $stateParams,
                                        $uibModalInstance, entity, VideoEntry,
                                        Upload) {
        var vm = this;

        vm.videoEntry = entity;
        vm.clear = clear;
        vm.save = save;
    vm.uploadVideo1 = uploadVideo1;
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.videoEntry.id !== null) {
                VideoEntry.update(vm.videoEntry, onSaveSuccess, onSaveError);
            } else {
                VideoEntry.save(vm.videoEntry, onSaveSuccess, onSaveError);
            }
        }

        function uploadVideo1(){
            Upload.upload({
                url: '/api/video-entries/' + vm.videoEntry.id + '/first',
                data: {
                    file: vm.file1
                }
            }).then(function (resp) {
                console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
            }, function (resp) {
                console.log('Error status: ' + resp.status);
            }, function (evt) {
                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
            });
        }

        function onSaveSuccess(result) {
            $scope.$emit('testappApp:videoEntryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
