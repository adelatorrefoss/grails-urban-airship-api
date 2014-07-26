grails-urban-airship-api
========================

This plugin provides a service to send push notifications to your mobile devices via [Urban Airship](http://urbanairship.com/) from your grails application.

This plugin uses the existing [Urban Airship Java Client](http://docs.urbanairship.com/reference/libraries/java/).

Please note that only is implemented one single method: Send a push notification to an Urban Airship alias with additional info to Android and IOS.

If you need other methods, or additional info, please send me a Pull Request or contact me to do it together.

## Configuration

You have to include the following mandatory properties in your Config.groovy file, or any other properties file in your config path.

```groovy
urbanAirship {
    appKey = rVn5fAyTRTODsj9_1pRlfw
    appSecret = SzIIDcCiTBep-IKrJ1WlfA
    appMasterSecret = uBAVwwj5ST67gtZzU0DR-w
}
```

## Send push notification

The plugin provides a service to send the push notification to an alias in Urban Airship. All you have to do is to inject the `urbanAirshipService` into your grails artefacts.


```groovy
class MyService {
    def urbanAirshipService

    def sendPush() {
        def alias = "myalias"
        def message = "A push notification"
        def additionalInfo = ["url" : "http://www.urbanairship.com"]

        try {
            def alias =
            if (alias) {
                urbanAirshipService.sendPush(alias, message, additionalInfo)
            }
        } catch(Throwable e) {
            log.error("ERROR: ${e.getMessage()}")
            return false
        }
        return true
    }
}
```

The `sendPush` method supports and optional `Map` param with additional information to send to the application.
This information can be used, for example, when you have a new follower to send the id of the follower and when you click on the notification on your mobile device, the application can open the profile of your new follower.
This additional information is send in the `notification-><override-platform>-extra` field.

The json sent to Urban Airship provider is like this:

```
{
    "audience": {
        "alias": "myalias"
    },
    "device_types": "all",
    "notification": {
        "alert": "A pusth notification",
        "android": {
            "extra" : { "url" : "http://www.urbanairship.com"}
        },
        "ios": {
            "extra" : { "url" : "http://www.urbanairship.com"}
        }
    }
}
```



## Additional information

The full documentation of the Urban Airship API is available at [http://docs.urbanairship.com/index.html](http://docs.urbanairship.com/index.html). Please, check it out for additional information.


## Authors

You can send any questions and collaborations :-) to:

- Antonio de la Torre: antondelatorre@gmail.com ([@adelatorrefoss](https://twitter.com/adelatorrefoss))


## Release Notes

* 0.1 - 26/Jul/2014 - Initial version
