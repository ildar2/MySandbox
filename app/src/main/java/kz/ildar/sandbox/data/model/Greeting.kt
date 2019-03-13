package kz.ildar.sandbox.data.model

data class GreetingWrapper(val args: Greeting, val url: String)//for postman echo

data class Greeting(val id: Long, val content: String)