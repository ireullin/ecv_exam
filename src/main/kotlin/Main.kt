import controllers.*
import io.ktor.application.*
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import sessions.*
import javax.crypto.Mac


fun main(args:Array<String>) {
    embeddedServer(Netty, 22333) {
        routing {

            install(Sessions){
                cookie<SessionAWSSecurityPart1>(AWSSecurityPart1.name)
                cookie<SessionAWSSecurityPart2>(AWSSecurityPart2.name)
                cookie<SessionBigData20200413>(BigData20200413.name)
                cookie<SessionMachineLearning>(MachineLearning.name)
                cookie<SessionDataAnalytics>(DataAnalytics.name)
            }

            get("/"){Example(call).index()}

            route("/v0.1/exam/BigData_20200413") {
                get("/correct/{correct}") { BigData20200413(call){c,t->SessionBigData20200413(c,t)}.hit() }
                get("/question") { BigData20200413(call){c,t->SessionBigData20200413(c,t)}.getQuestion() }
                get("/question/{num}") { BigData20200413(call){c,t->SessionBigData20200413(c,t)}.getQuestion() }
            }

            route("/v0.1/exam/AWSSecurityPart1") {
                get("/correct/{correct}") { AWSSecurityPart1(call){c,t->SessionAWSSecurityPart1(c,t)}.hit() }
                get("/question") { AWSSecurityPart1(call){c,t->SessionAWSSecurityPart1(c,t)}.getQuestion() }
                get("/question/{num}") { AWSSecurityPart1(call){c,t->SessionAWSSecurityPart1(c,t)}.getQuestion() }
            }

            route("/v0.1/exam/AWSSecurityPart2") {
                get("/correct/{correct}") { AWSSecurityPart2(call){c,t->SessionAWSSecurityPart2(c,t)}.hit() }
                get("/question") { AWSSecurityPart2(call){c,t->SessionAWSSecurityPart2(c,t)}.getQuestion() }
                get("/question/{num}") { AWSSecurityPart2(call){c,t->SessionAWSSecurityPart2(c,t)}.getQuestion() }
            }

            route("/v0.1/exam/MachineLearning") {
                get("/correct/{correct}") { MachineLearning(call){ c, t->SessionMachineLearning(c,t)}.hit() }
                get("/question") { MachineLearning(call){c,t->SessionMachineLearning(c,t)}.getQuestion() }
                get("/question/{num}") { MachineLearning(call){c,t->SessionMachineLearning(c,t)}.getQuestion() }
            }

            route("/v0.1/exam/DataAnalytics") {
                get("/correct/{correct}") { DataAnalytics(call){ c, t->SessionDataAnalytics(c,t)}.hit() }
                get("/question") { DataAnalytics(call){c,t->SessionDataAnalytics(c,t)}.getQuestion() }
                get("/question/{num}") { DataAnalytics(call){c,t->SessionDataAnalytics(c,t)}.getQuestion() }
            }

            route("/echo"){
                get("/"){Example(call).echo()}
                get("/{name}"){Example(call).echoWithId()}
                post("/"){Example(call).echoFromPost()}
            }

            // url上面遇到assets就去resources裡頭找assets
            static("assets"){
                resources("assets")
            }
      }
   }.start(wait = true)

}
