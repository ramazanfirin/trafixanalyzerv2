(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('camera-record', {
            parent: 'entity',
            url: '/camera-record?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.cameraRecord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/camera-record/camera-records.html',
                    controller: 'CameraRecordController',
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
                    $translatePartialLoader.addPart('cameraRecord');
                    $translatePartialLoader.addPart('processType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('camera-record-detail', {
            parent: 'camera-record',
            url: '/camera-record/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.cameraRecord.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/camera-record/camera-record-detail.html',
                    controller: 'CameraRecordDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cameraRecord');
                    $translatePartialLoader.addPart('processType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CameraRecord', function($stateParams, CameraRecord) {
                    return CameraRecord.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'camera-record',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('camera-record-detail.edit', {
            parent: 'camera-record-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/camera-record/camera-record-dialog.html',
                    controller: 'CameraRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CameraRecord', function(CameraRecord) {
                            return CameraRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('camera-record.new', {
            parent: 'camera-record',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/camera-record/camera-record-dialog.html',
                    controller: 'CameraRecordDialogController',
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
                    $state.go('camera-record', null, { reload: 'camera-record' });
                }, function() {
                    $state.go('camera-record');
                });
            }]
        })
        .state('camera-record.edit', {
            parent: 'camera-record',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/camera-record/camera-record-dialog.html',
                    controller: 'CameraRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CameraRecord', function(CameraRecord) {
                            return CameraRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('camera-record', null, { reload: 'camera-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('camera-record.delete', {
            parent: 'camera-record',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/camera-record/camera-record-delete-dialog.html',
                    controller: 'CameraRecordDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CameraRecord', function(CameraRecord) {
                            return CameraRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('camera-record', null, { reload: 'camera-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
