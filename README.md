[Radar](https://www.onradar.com) is the location platform for mobile apps. This is a Cordova plugin to use its SDK.

*This is still under heavy work*

## Installation

Install the Cordova plugin:

```bash
$ cordova plugin install cordova-plugin-radar
```


## Usage

(Check the main documentation)[https://www.onradar.com/documentation/sdk] for details.

### Initialize

Register and get your keys.

```js
Radar.initialize(key);
```


### Identify user

Before tracking the user's location, you must identify the user to Radar. To identify the user, call:

```js
Radar.setUserId(userId);
```

where `userId` is a stable unique ID string for the user.

To set an optional description for the user, displayed in the dashboard, call:

```js
Radar.setDescription(description);
```

where `description` is a string.

You only need to call these methods once, as these settings will be persisted across app sessions.

### Enable places

To set Facebook as your place data provider, call:

```js
Radar.setPlacesProvider(Radar.RadarPlacesProvider.FACEBOOK);
```

### Request permissions

Before tracking the user's location, the user must have granted location permissions for the app.

To determine the whether user has granted location permissions for the app, call:

```js
Radar.getPermissionsStatus().then((status) => {
  // do something with status
});
```

`status` will be a string, one of:

- `GRANTED`
- `DENIED`
- `UNKNOWN`

To request location permissions for the app, call:

```js
Radar.requestPermissions(background);
```

where `background` is a boolean indicating whether to request background location permissions or foreground location permissions. On Android, `background` will be ignored.

### Foreground tracking

Once you have initialized the SDK, you have identified the user, and the user has granted permissions, you can track the user's location.

To track the user's location in the foreground, call:

```js
Radar.trackOnce().then((result) => {
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
Radar.startTracking();
```

To stop tracking the user's location in the background (e.g., when the user logs out), call:

```js
Radar.stopTracking();
```

You only need to call these methods once, as these settings will be persisted across app sessions.

To listen for events and errors, you can add event listeners:

```js
Radar.on('events', (result) => {
  // do something with result.events, result.user
});

Radar.on('error', (err) => {
  // do something with err
});
```

You should remove event listeners when you are done with them (e.g., in `componentWillUnmount`):

```js
Radar.off('events');

Radar.off('error');
```

### Manual tracking

You can manually update the user's location by calling:

```js
const location = {
  latitude: 39.2904,
  longitude: -76.6122,
  accuracy: 65
};

Radar.updateLocation(location).then((result) => {
  // do something with result.events, result.user.geofences
}).catch((err) => {
  // optionally, do something with err
});
```