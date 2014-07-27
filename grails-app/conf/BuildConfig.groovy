grails.project.work.dir = "target"

grails.project.dependency.resolution = {
    inherits "global"
    log "warn"

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        compile 'com.urbanairship:java-client:0.2.2'
        test "org.objenesis:objenesis:2.1"
    }

    plugins {
        build (":release:2.2.0",":rest-client-builder:1.0.3") {
            export = false
        }
        test ':spock:0.7', ':code-coverage:1.2.7', {
            export = false
        }
        compile ":codenarc:0.19" , {export = false}
    }
}
