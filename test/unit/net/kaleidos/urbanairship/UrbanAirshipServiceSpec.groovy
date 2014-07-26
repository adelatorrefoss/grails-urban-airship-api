package net.kaleidos.urbanairship

import grails.plugin.spock.*
import grails.test.mixin.*
import spock.lang.*

@TestFor(UrbanAirshipService)
class UrbanAirshipServiceSpec extends Specification {

    def urbanAirshipService

    void setup() {
        service.grailsApplication.config = [
            "urbanAirship" : [
                appKey : "zZDIKbw-Sey-_f0HOW6s1g",
                appSecret : "wc0phoJvT--SOne383lpGw",
                appMasterSecret : "dZfypL2EQrq_4QGOmcCkhQ"
            ]
        ]
    }

    void 'message push'() {
        given: "data: alias + message"
            def alias = "alias1"
            def message = "test message with a lot of characters"
            service.afterPropertiesSet()

        when:
            service.sendPush(alias, message)

        then:
            true
    }
}
