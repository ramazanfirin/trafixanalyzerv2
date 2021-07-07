(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('video-record', {
            parent: 'entity',
            url: '/video-record?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.videoRecord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video-record/video-records.html',
                    controller: 'VideoRecordController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
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
                    $translatePartialLoader.addPart('videoRecord');
                    $translatePartialLoader.addPart('processType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('video-record-detail', {
            parent: 'video-record',
            url: '/video-record/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.videoRecord.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/video-record/video-record-detail.html',
                    controller: 'VideoRecordDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('videoRecord');
                    $translatePartialLoader.addPart('processType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'VideoRecord', function($stateParams, VideoRecord) {
                    return VideoRecord.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'video-record',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('video-record-detail.edit', {
            parent: 'video-record-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-record/video-record-dialog.html',
                    controller: 'VideoRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VideoRecord', function(VideoRecord) {
                            return VideoRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('video-record.new', {
            parent: 'video-record',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-record/video-record-dialog.html',
                    controller: 'VideoRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                insertDate: null,
                                vehicleType: null,
                                duration: null,
                                speed: null,
                                processType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('video-record', null, { reload: 'video-record' });
                }, function() {
                    $state.go('video-record');
                });
            }]
        })
        .state('video-record.edit', {
            parent: 'video-record',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-record/video-record-dialog.html',
                    controller: 'VideoRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VideoRecord', function(VideoRecord) {
                            return VideoRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('video-record', null, { reload: 'video-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('video-record.delete', {
            parent: 'video-record',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/video-record/video-record-delete-dialog.html',
                    controller: 'VideoRecordDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['VideoRecord', function(VideoRecord) {
                            return VideoRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('video-record', null, { reload: 'video-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
