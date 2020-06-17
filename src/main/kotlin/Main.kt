import controllers.BigData20200413
import controllers.Example
import io.ktor.application.*
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.*2
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import libs.json.toJson

fun main(args:Array<String>) {
//    val test = "A. Copy the data into Amazon ElastiCache to perform text analysis on the in-memory"
//    val optReg = Regex("^[A-H]{1}\\.\\s+[\\s\\S]*$")
//    println(optReg.matches(test))
//    return

    embeddedServer(Netty, 22333) {
        routing {
//            get("/"){Example(call).index()}

            route("/v0.1/exam/BigData_20200413") {
                get("/") { BigData20200413(call).index() }
                get("/question") { BigData20200413(call).getQuestion() }
                get("/question/{num}") { BigData20200413(call).getQuestion() }
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
