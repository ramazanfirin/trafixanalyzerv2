(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('polygon', {
            parent: 'entity',
            url: '/polygon?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.polygon.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/polygon/polygons.html',
                    controller: 'PolygonController',
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
                    $translatePartialLoader.addPart('polygon');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('polygon-detail', {
            parent: 'polygon',
            url: '/polygon/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.polygon.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/polygon/polygon-detail.html',
                    controller: 'PolygonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('polygon');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Polygon', function($stateParams, Polygon) {
                    return Polygon.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'polygon',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('polygon-detail.edit', {
            parent: 'polygon-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/polygon/polygon-dialog.html',
                    controller: 'PolygonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Polygon', function(Polygon) {
                            return Polygon.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('polygon.new', {
            parent: 'polygon',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/polygon/polygon-dialog.html',
                    controller: 'PolygonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                points: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('polygon', null, { reload: 'polygon' });
                }, function() {
                    $state.go('polygon');
                });
            }]
        })
        .state('polygon.edit', {
            parent: 'polygon',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/polygon/polygon-dialog.html',
                    controller: 'PolygonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Polygon', function(Polygon) {
                            return Polygon.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('polygon', null, { reload: 'polygon' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('polygon.delete', {
            parent: 'polygon',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/polygon/polygon-delete-dialog.html',
                    controller: 'PolygonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Polygon', function(Polygon) {
                            return Polygon.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('polygon', null, { reload: 'polygon' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
