package net.kaleidos.urbanairship

import com.urbanairship.api.client.*
import com.urbanairship.api.push.model.DeviceType
import com.urbanairship.api.push.model.DeviceTypeData
import com.urbanairship.api.push.model.PushPayload
import com.urbanairship.api.push.model.audience.Selectors
import com.urbanairship.api.push.model.notification.Notifications
import com.urbanairship.api.push.model.notification.Notification
import com.urbanairship.api.schedule.model.Schedule
import com.urbanairship.api.schedule.model.SchedulePayload
import com.urbanairship.api.push.model.notification.android.AndroidDevicePayload
import com.urbanairship.api.push.model.notification.ios.IOSDevicePayload

import org.springframework.beans.factory.InitializingBean

class UrbanAirshipService implements InitializingBean {

    static transactional = false

    def grailsApplication
    def urbanAirshipConfig

    void afterPropertiesSet() {
        // urbanAirshipConfig = grailsApplication.config.urbanAirship
        urbanAirshipConfig = grailsApplication.config.urbanAirship

        log.debug """Make sure key and secret are set Key:${urbanAirshipConfig.appKey}
                    Secret:Is null? ${urbanAirshipConfig.secretKey==null}
                    Master Secret:Is null? ${urbanAirshipConfig.secretMasterKey==null}
                    """
    }

    void sendPush(String alias, String alert, Map customFields) {

        def apiClient = _createApiClient()

        def payload = _createPayload(alias, alert, customFields)

        try {
            APIClientResponse<APIPushResponse> response = apiClient.push(payload)

            log.debug("PUSH SUCCEEDED")
            log.debug("RESPONSE: ${response.toString()}")
        }
        catch (APIRequestException ex){
            log.error("APIRequestException $ex")
            log.error("EXCEPTION ${ex.toString()}")

            APIError apiError = ex.getError().get()
            log.error("Error ${apiError.getError()}")
            if (apiError.getDetails().isPresent())     {
                APIErrorDetails apiErrorDetails = apiError.getDetails().get()
                log.error("Error details ${apiErrorDetails.getError()}")
            }

            throw ex
        }
        catch (IOException e){
            log.error("IOException in API request ${e.getMessage()}")

            throw e
        }
    }

    def _createPayload(alias, alert, customFields) {

        def payloadBuilder = PushPayload.newBuilder()
                                         .setAudience(Selectors.alias(alias))

        def notificationBuilder = Notification.newBuilder()
        notificationBuilder.setAlert(alert)

        if (customFields) {
            notificationBuilder.addDeviceTypeOverride(DeviceType.ANDROID,
                                AndroidDevicePayload.newBuilder()
                                    .addAllExtraEntries(customFields).build())

            notificationBuilder.addDeviceTypeOverride(DeviceType.IOS,
                                IOSDevicePayload.newBuilder()
                                    .addAllExtraEntries(customFields).build())
        }

        payloadBuilder.setNotification(notificationBuilder.build())
        payloadBuilder.setDeviceTypes(DeviceTypeData.all())
        PushPayload payload = payloadBuilder.build()

        log.debug "PAYLOAD: ${payload.toJSON()}"

        return payload
    }


    def _createApiClient() {
        APIClient apiClient = APIClient.newBuilder()
                       .setKey(urbanAirshipConfig.appKey)
                       .setSecret(urbanAirshipConfig.appMasterSecret)
                       .build()

        return apiClient
    }
}
