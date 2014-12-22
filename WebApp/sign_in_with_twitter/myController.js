/**
 * Created by dongxueliu on 11/30/14.
 */
function myController($scope, $http){
    $scope.verifyUser=function(user_info){
        /*$http({
            method: 'POST',
            url:'createUser.php',
            data: $.param(user_info),
            headers:{ 'Content-Type': 'application/x-www-form-urlencoded'}
        })
            .success(function(data){

            });*/
        $window.location.href = '/userMainPage.html';
        $scope.jumpToUrl('/userMainPage.html');
    };
    $scope.jumpToUrl=function(path){
        alert("test2");
        //$location.path(path);
        //var curUrl = $location.absUrl();
        //$location.absUrl() = path;
        //$window.location.href = 'http://www.google.com';
        //$location.url('http://www.google.com')
        //$window.location.href = 'http://www.google.com';
        $window.location.href = 'http://www.google.com';
        alert("test3");
    };
    $scope.verifyRetailer=function(retailer_info){

    };
}