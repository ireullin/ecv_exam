package controllers

import helpers.Html
import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.response.respondText
import libs.datetime.ImmutableDatetime
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.random.Random

class BigData20200413(val call: ApplicationCall){
    companion object{
        data class Question(val qNum:String, val q:String, val opts:List<String>, val ans:String, val comment:String)
        private val questions:MutableList<Question> = mutableListOf()
        private val r = Random(ImmutableDatetime.now().stamp())
    }

    private val log = LoggerFactory.getLogger(this.javaClass)

    suspend fun index(){
        call.respondText("fuck root", ContentType.Text.Html)
    }

    suspend fun getQuestion(){
        if(questions.isEmpty())
            readSrc()

        val num = call.parameters["num"]?.toInt()?: r.nextInt(1, questions.size)
        val idx = num -1
        val q = questions[idx]
        val html = Html("/question.ftl").render(
                "title" to "BigData20200413",
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
        val lines = File("data/BigData_20200413.txt").readLines()
        val subtitles = mutableListOf<MutableList<String>>()
        val qNumReg = Regex("Q\\d+")
        lines.forEach {line->
            when(qNumReg.matches(line)) {
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
                    line.startsWith("Ans:") -> part.add(mutableListOf(line.replace("Ans:","").trim() ))
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