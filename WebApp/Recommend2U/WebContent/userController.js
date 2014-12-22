/**
 * 
 */
app.controller("userController", ["$scope", "$stateParams", function($scope, $http, myService, $stateParams){
	$scope.user=$stateParams.input;
	//$scope.user=myService.getUser();
	alert($scope.user);
}]);