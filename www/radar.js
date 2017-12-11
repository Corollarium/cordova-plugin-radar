'use strict';

const cordova = require('cordova');

const exec = (successCallback, errorCallback, action, args) => {
	cordova.exec(
		callback,
		errorCallback || (error) => {
			console.log(error);
		}, 
		'radar', 
		action, 
		args
	);
};

var radarExport = {};

radarExport.initialize = function(key) {
	exec(successCallback, errorCallback, "initialize", [key]);
};

radarExport.setUserId = function(id, successCallback, errorCallback) {
	exec(successCallback, errorCallback, "setUserId", [id]);
};

radarExport.setDescription = function(description, successCallback, errorCallback) {
	exec(successCallback, errorCallback, "setDescription", [description]);
};

radarExport.setPlacesProvider = function(provider, successCallback, errorCallback) {
	exec(successCallback, errorCallback, "setPlacesProvider", [provider]);
};

radarExport.getPermissionsStatus = function(successCallback) {
	exec(successCallback, null, "setPlacesProvider", []);
};

radarExport.trackOnce = function(successCallback, errorCallback) {
	exec(successCallback, errorCallback, "trackOnce", []);
};

radarExport.startTracking = function(
	onEventsReceived, onLocationUpdate, onError, successCallback, errorCallback
) {
	exec(successCallback, errorCallback, "startTracking", []);
};

radarExport.stopTracking = function(successCallback, errorCallback) {
	exec(successCallback, errorCallback, "stopTracking", []);
};

radarExport.updateLocation = function(location, successCallback, errorCallback) {
	exec(successCallback, errorCallback, "updateLocation", [location]);
};

module.exports = radarExport;
