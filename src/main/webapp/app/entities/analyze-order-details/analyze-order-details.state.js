(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('analyze-order-details', {
            parent: 'entity',
            url: '/analyze-order-details?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.analyzeOrderDetails.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/analyze-order-details/analyze-order-details.html',
                    controller: 'AnalyzeOrderDetailsController',
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
                    $translatePartialLoader.addPart('analyzeOrderDetails');
                    $translatePartialLoader.addPart('analyzeState');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('analyze-order-details-detail', {
            parent: 'analyze-order-details',
            url: '/analyze-order-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.analyzeOrderDetails.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/analyze-order-details/analyze-order-details-detail.html',
                    controller: 'AnalyzeOrderDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('analyzeOrderDetails');
                    $translatePartialLoader.addPart('analyzeState');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AnalyzeOrderDetails', function($stateParams, AnalyzeOrderDetails) {
                    return AnalyzeOrderDetails.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'analyze-order-details',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('analyze-order-details-detail.edit', {
            parent: 'analyze-order-details-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analyze-order-details/analyze-order-details-dialog.html',
                    controller: 'AnalyzeOrderDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AnalyzeOrderDetails', function(AnalyzeOrderDetails) {
                            return AnalyzeOrderDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('analyze-order-details.new', {
            parent: 'analyze-order-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analyze-order-details/analyze-order-details-dialog.html',
                    controller: 'AnalyzeOrderDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sessionId: null,
                                videoPath: null,
                                count: null,
                                classes: null,
                                directions: null,
                                speed: null,
                                jsonData: null,
                                errorMessage: null,
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
                    $state.go('analyze-order-details', null, { reload: 'analyze-order-details' });
                }, function() {
                    $state.go('analyze-order-details');
                });
            }]
        })
        .state('analyze-order-details.edit', {
            parent: 'analyze-order-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analyze-order-details/analyze-order-details-dialog.html',
                    controller: 'AnalyzeOrderDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AnalyzeOrderDetails', function(AnalyzeOrderDetails) {
                            return AnalyzeOrderDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('analyze-order-details', null, { reload: 'analyze-order-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('analyze-order-details.delete', {
            parent: 'analyze-order-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/analyze-order-details/analyze-order-details-delete-dialog.html',
                    controller: 'AnalyzeOrderDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AnalyzeOrderDetails', function(AnalyzeOrderDetails) {
                            return AnalyzeOrderDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('analyze-order-details', null, { reload: 'analyze-order-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
