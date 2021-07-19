(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('file-upload-test', {
            parent: 'entity',
            url: '/file-upload-test',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.fileUploadTest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/file-upload-test/file-upload-tests.html',
                    controller: 'FileUploadTestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fileUploadTest');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('file-upload-test-detail', {
            parent: 'file-upload-test',
            url: '/file-upload-test/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.fileUploadTest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/file-upload-test/file-upload-test-detail.html',
                    controller: 'FileUploadTestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fileUploadTest');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FileUploadTest', function($stateParams, FileUploadTest) {
                    return FileUploadTest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'file-upload-test',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('file-upload-test-detail.edit', {
            parent: 'file-upload-test-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-upload-test/file-upload-test-dialog.html',
                    controller: 'FileUploadTestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FileUploadTest', function(FileUploadTest) {
                            return FileUploadTest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('file-upload-test.new', {
            parent: 'file-upload-test',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-upload-test/file-upload-test-dialog.html',
                    controller: 'FileUploadTestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                file: null,
                                fileContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('file-upload-test', null, { reload: 'file-upload-test' });
                }, function() {
                    $state.go('file-upload-test');
                });
            }]
        })
        .state('file-upload-test.edit', {
            parent: 'file-upload-test',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-upload-test/file-upload-test-dialog.html',
                    controller: 'FileUploadTestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FileUploadTest', function(FileUploadTest) {
                            return FileUploadTest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('file-upload-test', null, { reload: 'file-upload-test' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('file-upload-test.delete', {
            parent: 'file-upload-test',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/file-upload-test/file-upload-test-delete-dialog.html',
                    controller: 'FileUploadTestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FileUploadTest', function(FileUploadTest) {
                            return FileUploadTest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('file-upload-test', null, { reload: 'file-upload-test' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
