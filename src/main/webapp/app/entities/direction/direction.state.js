(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('direction', {
            parent: 'entity',
            url: '/direction?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.direction.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/direction/directions.html',
                    controller: 'DirectionController',
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
                    $translatePartialLoader.addPart('direction');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('direction-detail', {
            parent: 'direction',
            url: '/direction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.direction.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/direction/direction-detail.html',
                    controller: 'DirectionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('direction');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Direction', function($stateParams, Direction) {
                    return Direction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'direction',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('direction-detail.edit', {
            parent: 'direction-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/direction/direction-dialog.html',
                    controller: 'DirectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Direction', function(Direction) {
                            return Direction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('direction.new', {
            parent: 'direction',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/direction/direction-dialog.html',
                    controller: 'DirectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                pathCoordinates: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('direction', null, { reload: 'direction' });
                }, function() {
                    $state.go('direction');
                });
            }]
        })
        .state('direction.edit', {
            parent: 'direction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/direction/direction-dialog.html',
                    controller: 'DirectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Direction', function(Direction) {
                            return Direction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('direction', null, { reload: 'direction' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('direction.delete', {
            parent: 'direction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/direction/direction-delete-dialog.html',
                    controller: 'DirectionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Direction', function(Direction) {
                            return Direction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('direction', null, { reload: 'direction' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
