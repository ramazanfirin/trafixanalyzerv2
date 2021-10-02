(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('video', {
            parent: 'entity',
            url: '/video?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.video.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video/videos.html',
                    controller: 'VideoController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,desc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('video');
                    $translatePartialLoader.addPart('videoState');
                    $translatePartialLoader.addPart('global');
					$translatePartialLoader.addPart('videoType');
                    return $translate.refresh();
                }]
            }
        })
        .state('video-detail', {
            parent: 'video',
            url: '/video/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.video.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video/video-detail.html',
                    controller: 'VideoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('video');
                    $translatePartialLoader.addPart('videoState');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Video', function($stateParams, Video) {
                    return Video.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'video',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('video-detail.edit', {
            parent: 'video-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video/video-dialog.html',
                    controller: 'VideoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Video', function(Video) {
                            return Video.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('video.new', {
            parent: 'video',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video/video-dialog.html',
                    controller: 'VideoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                path: null,
                                name: null,
                                processDate: null,
                                startDate: null,
                                endDate: null,
                                state: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('video', null, { reload: 'video' });
                }, function() {
                    $state.go('video');
                });
            }]
        })
        .state('video.edit', {
            parent: 'video',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video/video-dialog.html',
                    controller: 'VideoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Video', function(Video) {
                            return Video.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('video', null, { reload: 'video' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('video.delete', {
            parent: 'video',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video/video-delete-dialog.html',
                    controller: 'VideoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Video', function(Video) {
                            return Video.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('video', null, { reload: 'video' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
