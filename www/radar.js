'use strict';

const cordova = require('cordova');

const exec = (successCallback, errorCallback, action, args) => {
	if (!successCallback) {
		successCallback = () => {
		};
	}
	if (!errorCallback) {
		errorCallback = (error) => {
			console.log(error);
		};
	}
	cordova.exec(
		successCallback,
		errorCallback, 
		'radar', 
		action, 
		args
	);
};

var radarExport = {};

radarExport.initialize = function(key) {
	exec(() => {}, null, "initialize", [key]);
};

radarExport.setUserId = function(id) {
	exec(() => {}, null, "setUserId", [id]);
};

radarExport.setDescription = function(description) {
	exec(() => {}, null, "setDescription", [description]);
};

radarExport.setPlacesProvider = function(provider) {
	exec(() => {}, null, "setPlacesProvider", [provider]);
};

radarExport.getPermissionsStatus = function(callback) {
	exec(callback, null, "getPermissionsStatus", []);
};

radarExport.trackOnce = function(callback) {
	exec(callback, null, "trackOnce", []);
};

radarExport.startTracking = function() {
	exec(() => {}, null, "startTracking", []);
};

radarExport.stopTracking = function() {
	exec(() => {}, null, "stopTracking", []);
};

radarExport.acceptEvent = function(eventId, placeId) {
	exec(() => {}, null, "acceptEvent", [eventId, placeId]);
};

radarExport.rejectEvent = function(eventId) {
	exec(() => {}, null, "rejectEvent", [eventId]);
};

radarExport.updateLocation = function(location, callback, errorCallback) {
	exec(callback, errorCallback, "updateLocation", [location]);
};

radarExport.onEvents = function(callback) {
	exec((data) => { callback(data.events, data.user) }, null, "onEvents", []);
};

radarExport.onError = function(callback) {
	exec((data) => { callback(data.status) }, null, "onError", []);
};

radarExport.offEvents = function(callback) {
	exec(null, null, "offEvents", []);
};

radarExport.offError = function(callback) {
	exec(null, null, "offError", []);
};


module.exports = radarExport;
