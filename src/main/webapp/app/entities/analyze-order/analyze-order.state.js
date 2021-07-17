(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('analyze-order', {
            parent: 'entity',
            url: '/analyze-order?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.analyzeOrder.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/analyze-order/analyze-orders.html',
                    controller: 'AnalyzeOrderController',
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
                    $translatePartialLoader.addPart('analyzeOrder');
                    $translatePartialLoader.addPart('analyzeState');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('analyze-order-detail', {
            parent: 'analyze-order',
            url: '/analyze-order/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.analyzeOrder.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/analyze-order/analyze-order-detail.html',
                    controller: 'AnalyzeOrderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('analyzeOrder');
                    $translatePartialLoader.addPart('analyzeState');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AnalyzeOrder', function($stateParams, AnalyzeOrder) {
                    return AnalyzeOrder.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'analyze-order',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('analyze-order-detail.edit', {
            parent: 'analyze-order-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analyze-order/analyze-order-dialog.html',
                    controller: 'AnalyzeOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AnalyzeOrder', function(AnalyzeOrder) {
                            return AnalyzeOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('analyze-order.new', {
            parent: 'analyze-order',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analyze-order/analyze-order-dialog.html',
                    controller: 'AnalyzeOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startDate: null,
                                endDate: null,
                                processDuration: null,
                                videoDuration: null,
                                state: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('analyze-order', null, { reload: 'analyze-order' });
                }, function() {
                    $state.go('analyze-order');
                });
            }]
        })
        .state('analyze-order.edit', {
            parent: 'analyze-order',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analyze-order/analyze-order-dialog.html',
                    controller: 'AnalyzeOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AnalyzeOrder', function(AnalyzeOrder) {
                            return AnalyzeOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('analyze-order', null, { reload: 'analyze-order' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('analyze-order.delete', {
            parent: 'analyze-order',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analyze-order/analyze-order-delete-dialog.html',
                    controller: 'AnalyzeOrderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AnalyzeOrder', function(AnalyzeOrder) {
                            return AnalyzeOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('analyze-order', null, { reload: 'analyze-order' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();