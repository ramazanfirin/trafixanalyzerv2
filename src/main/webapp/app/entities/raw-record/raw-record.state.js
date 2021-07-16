(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('raw-record', {
            parent: 'entity',
            url: '/raw-record?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.rawRecord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/raw-record/raw-records.html',
                    controller: 'RawRecordController',
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
                    $translatePartialLoader.addPart('rawRecord');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('raw-record-detail', {
            parent: 'raw-record',
            url: '/raw-record/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.rawRecord.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/raw-record/raw-record-detail.html',
                    controller: 'RawRecordDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rawRecord');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RawRecord', function($stateParams, RawRecord) {
                    return RawRecord.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'raw-record',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('raw-record-detail.edit', {
            parent: 'raw-record-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/raw-record/raw-record-dialog.html',
                    controller: 'RawRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RawRecord', function(RawRecord) {
                            return RawRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('raw-record.new', {
            parent: 'raw-record',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/raw-record/raw-record-dialog.html',
                    controller: 'RawRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sessionID: null,
                                time: null,
                                objectType: null,
                                speed: null,
                                entry: null,
                                exit: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('raw-record', null, { reload: 'raw-record' });
                }, function() {
                    $state.go('raw-record');
                });
            }]
        })
        .state('raw-record.edit', {
            parent: 'raw-record',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/raw-record/raw-record-dialog.html',
                    controller: 'RawRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RawRecord', function(RawRecord) {
                            return RawRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('raw-record', null, { reload: 'raw-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('raw-record.delete', {
            parent: 'raw-record',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/raw-record/raw-record-delete-dialog.html',
                    controller: 'RawRecordDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RawRecord', function(RawRecord) {
                            return RawRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('raw-record', null, { reload: 'raw-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
