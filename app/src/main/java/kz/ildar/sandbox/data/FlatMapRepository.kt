package kz.ildar.sandbox.data

import kz.ildar.sandbox.data.api.Api

class FlatMapRepository(private val api: Api) : ApiCallerInterface by ApiCaller() {
    suspend fun getObjectWithProperties() {
        api.postmanEcho().await().let {
//            api.postmanEchoNamed()
        }
    }
}