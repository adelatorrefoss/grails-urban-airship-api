package net.kaleidos.urbanairship

import grails.plugin.spock.*
import grails.test.mixin.*
import spock.lang.*
import spock.util.mop.ConfineMetaClassChanges
import groovy.json.JsonSlurper

import com.urbanairship.api.client.*

@TestFor(UrbanAirshipService)
class UrbanAirshipServiceSpec extends Specification {

    def urbanAirshipService

    void setup() {
        service.grailsApplication.config = [
            "urbanAirship" : [
                appKey : "zZDIKbw-Sey-_xxxxx",
                appSecret : "wc0phoJvT--xxxx",
                appMasterSecret : "dZfypL2EQrq_xxxx"
            ]
        ]
    }

    void 'message push'() {
        given: "data: alias + message"
            def alias = "alias1"
            def message = "test message with a lot of characters"
            def additionaInfo = ["type":"error"]
            service.afterPropertiesSet()

        and: 'mock'
            def apiClientStub = Stub(APIClient)
            apiClientStub.push(_) >> null
            service.metaClass._createApiClient = { ->
                apiClientStub }

        when:
            service.sendPush(alias, message, additionaInfo)

        then:
            true
    }

    void 'message push without custom fields'() {
        given: "data: alias + message"
            def alias = "alias1"
            def message = "test message with a lot of characters"
            def additionaInfo = [:]

            service.afterPropertiesSet()

        and: 'mock'
            def apiClientStub = Stub(APIClient)
            apiClientStub.push(_) >> null
            service.metaClass._createApiClient = { ->
                apiClientStub }

        when:
            service.sendPush(alias, message, additionaInfo)

        then:
            true
    }

    // void 'error in message push'() {
    //     given: "data: alias + message"
    //         def alias = "alias1"
    //         def message = "test message with a lot of characters"
    //         def additionaInfo = [:]
    //         service.afterPropertiesSet()

    //     and: 'mock'
    //         def apiClientStub = Stub(APIClient)
    //         apiClientStub.push(_) >> { throw new APIRequestException() }
    //         service.metaClass._createApiClient = { ->
    //             apiClientStub }

    //     when:
    //         service.sendPush(alias, message, additionaInfo)

    //     then:
    //         thrown APIRequestException
    // }

    void 'create payload'() {
        given: "data: alias + message"
            def alias = "alias1"
            def message = "test message with a lot of characters"
            def additionaInfo = ["type":"error"]

        when:
            def payload = service._createPayload(alias, message, additionaInfo)

            def payloadJson = new JsonSlurper().parseText(payload.toJSON())

        then:
            // {
            //     "audience": {
            //         "alias": "alias1"
            //     },
            //     "device_types": "all",
            //     "notification": {
            //         "alert": "test message with a lot of characters",
            //         "android": {
            //             "extra": {
            //                 "type": "error"
            //             }
            //         },
            //         "ios": {
            //             "extra": {
            //                 "type": "error"
            //             }
            //         }
            //     }
            // }

            payloadJson.audience.alias == "alias1"
            payloadJson.device_types == "all"
            payloadJson.notification.alert == "test message with a lot of characters"
            payloadJson.notification.android.extra.type == "error"
            payloadJson.notification.ios.extra.type == "error"
    }
}
