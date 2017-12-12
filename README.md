[Radar](https://www.onradar.com) is the location platform for mobile apps. This is a Cordova plugin to use its SDK.

## Installation

Install the Cordova plugin:

```bash
$ cordova plugin install cordova-plugin-radar
```


## Usage

[Check the main documentation](https://www.onradar.com/documentation/sdk) for details. There's a 
[basic test app example](https://github.com/Corollarium/cordova-plugin-radar-example) as well.

### Initialize

Register and get your keys.

```js
cordova.plugins.radar.initialize(key);
```


### Identify user

Before tracking the user's location, you must identify the user to cordova.plugins.radar. To identify the user, call:

```js
cordova.plugins.radar.setUserId(userId);
```

where `userId` is a stable unique ID string for the user.

To set an optional description for the user, displayed in the dashboard, call:

```js
cordova.plugins.radar.setDescription(description);
```

where `description` is a string.

You only need to call these methods once, as these settings will be persisted across app sessions.

### Enable places

To set Facebook as your place data provider, call:

```js
cordova.plugins.radar.setPlacesProvider('facebook');
```

### Request permissions

Before tracking the user's location, the user must have granted location permissions for the app. These
are requested automatically when you initialized radar.

To determine the whether user has granted location permissions for the app, call:

```js
cordova.plugins.radar.getPermissionsStatus().then((status) => {
  // do something with status
});
```

`status` will be a string, one of:

- `GRANTED`
- `DENIED`
- `UNKNOWN`

To request location permissions for the app, call:

```js
cordova.plugins.radar.requestPermissions(background);
```

where `background` is a boolean indicating whether to request background location permissions or foreground location permissions. On Android, `background` will be ignored.

### Foreground tracking

Once you have initialized the SDK, you have identified the user, and the user has granted permissions, you can track the user's location.

To track the user's location in the foreground, call:

```js
cordova.plugins.radar.trackOnce().then((result) => {
  // do something with result.location, result.events, result.user.geofences
}).catch((err) => {
  // optionally, do something with err
});
```

`err` will be a string, one of:

- `ERROR_PUBLISHABLE_KEY`: the SDK was not initialized
- `ERROR_USER_ID`: the user was not identified
- `ERROR_PERMISSIONS`: the user has not granted location permissions for the app
- `ERROR_LOCATION`: location services were unavailable, or the location request timed out
- `ERROR_NETWORK`: the network was unavailable, or the network connection timed out
- `ERROR_UNAUTHORIZED`: the publishable API key is invalid
- `ERROR_SERVER`: an internal server error occurred
- `ERROR_UNKNOWN`: an unknown error occurred

### Background tracking

Once you have initialized the SDK, you have identified the user, and the user has granted permissions, you can start tracking the user's location in the background.

To start tracking the user's location in the background, call:

```js
cordova.plugins.radar.startTracking();
```

To stop tracking the user's location in the background (e.g., when the user logs out), call:

```js
cordova.plugins.radar.stopTracking();
```

You only need to call these methods once, as these settings will be persisted across app sessions.

To listen for events and errors, you can add event listeners:

```js
cordova.plugins.radar.onEvents(
	(events, user) => {
		// do something with result.events, result.user
	}
);

cordova.plugins.radar.onError(
 (err) => {
  // do something with err
});
```

You should remove event listeners when you are done with them:

```js
cordova.plugins.radar.offEvents();

cordova.plugins.radar.offError();
```

### Manual tracking

You can manually update the user's location by calling:

```js
const location = {
  latitude: 39.2904,
  longitude: -76.6122,
  accuracy: 65
};

cordova.plugins.radar.updateLocation(
	{
		latitude: document.getElementById('latitude').value,
		longitude: document.getElementById('longitude').value,
		accuracy: document.getElementById('accuracy').value
	},
	(result) => {
		// do something with result.events, result.user.geofences
		console.log(result);
	}
);
```