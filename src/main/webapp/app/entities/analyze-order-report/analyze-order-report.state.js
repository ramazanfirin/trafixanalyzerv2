(function() {
    'use strict';

    angular
        .module('trafficanalzyzerv2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('analyze-order-report', {
            parent: 'entity',
            url: '/analyze-order-report?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'trafficanalzyzerv2App.analyzeOrder.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/analyze-order-report/analyze-orders-report.html',
                    controller: 'AnalyzeOrderReportController',
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
                    $translatePartialLoader.addPart('analyzeOrder');
                    $translatePartialLoader.addPart('analyzeState');
                    $translatePartialLoader.addPart('global');
                    $translatePartialLoader.addPart('analyzeOrderDetails');
                    $translatePartialLoader.addPart('video');
					$translatePartialLoader.addPart('videoRecord');
                    return $translate.refresh();
                }]
            }
        })
       
    }

})();
