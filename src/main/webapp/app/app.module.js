(function() {
    'use strict';

    angular
        .module('testappApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            'ngSanitize',
            'com.2fdevs.videogular',
            'com.2fdevs.videogular.plugins.buffering',
            'com.2fdevs.videogular.plugins.controls',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
