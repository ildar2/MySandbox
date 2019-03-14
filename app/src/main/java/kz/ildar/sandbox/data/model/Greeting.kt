package kz.ildar.sandbox.data.model

interface GreetingsResponse {
    fun getContents(): String
}

data class GreetingWrapper(val args: Greeting, val url: String) : GreetingsResponse//for postman echo
{
    override fun getContents() = args.content
}

data class Greeting(val id: Long, val content: String) : GreetingsResponse//local server
{
    override fun getContents() = content
}