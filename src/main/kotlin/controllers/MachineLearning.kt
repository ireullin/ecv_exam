package controllers

import helpers.Html
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.sessions.sessions
import libs.datetime.ImmutableDatetime
import libs.json.Json
import org.slf4j.LoggerFactory
import sessions.HitSession
import java.io.File
import kotlin.random.Random

class MachineLearning(val call: ApplicationCall, val newSession:(correct:Int, total:Int)->HitSession){
    companion object{
        data class Question(val qNum:String, val q:String, val opts:List<String>, val ans:String, val comment:String)
        val name = "MachineLearning"
        private val questions:MutableList<Question> = mutableListOf()
        private val r = Random(ImmutableDatetime.now().stamp())
    }

    private val log = LoggerFactory.getLogger(this.javaClass)


    suspend fun hit(){
        val correct = call.parameters["correct"]?.toInt()?:0
        val sess = (call.sessions.get(name)?:newSession(0,0)) as HitSession
        val newSess = newSession( sess.correct+correct, sess.total+1 )
        call.sessions.set(name, newSess)
        call.respondText( Json.stringify(newSess) ,ContentType.Application.Json)
    }

    suspend fun getQuestion(){
        if(questions.isEmpty())
            readSrc()

        val sess = (call.sessions.get(name)?:newSession(0,0)) as HitSession
        val num = call.parameters["num"]?.toInt()?: r.nextInt(1, questions.size)
        val idx = num -1
        val q = questions[idx]
        val html = Html("/question.ftl").render(
                "correct" to sess.correct,
                "total" to sess.total,
                "title" to "BigData 20200413",
                "path" to name,
                "num" to q.qNum,
                "content" to q.q,
                "opts" to q.opts,
                "ans" to q.ans,
                "pre" to num-1,
                "next" to num+1
        )
        call.respondText(html,ContentType.Text.Html)
    }


    fun readSrc(){
        val lines = File("data/ml.txt").readLines(Charsets.UTF_8).map{it.trim()}.filter { it.isNotEmpty() }
        val subtitles = mutableListOf<MutableList<String>>()
        val qNumReg = Regex("^\\d+")
        lines.forEach {line->
            println(line)
            when( qNumReg.containsMatchIn(line)) {
                true -> subtitles.add(mutableListOf(line))
                false -> subtitles.last().add(line)
            }
        }

        val optReg = Regex("^[A-H]{1}\\.\\s+[\\s\\S]*$")
        val _questions = subtitles.map {sub->
            val part = mutableListOf(mutableListOf<String>()) //mutableListOf<MutableList<String>>()
            sub.drop(1).forEach{ line->
                log.info(line)
                when{
                    optReg.matches(line) -> part.add(mutableListOf(line))
                    line.startsWith("Ans :") -> part.add(mutableListOf(line.replace("Ans :","").trim() ))
                    else -> part.last().add(line)
                }
            }
            val t = part.map { it.joinToString("\n") }
            log.info(t.toString())
            Question(sub[0], t.first(), t.drop(1).dropLast(1), t.last(),"" )
        }

        questions.clear()
        questions.addAll(_questions)
    }
}