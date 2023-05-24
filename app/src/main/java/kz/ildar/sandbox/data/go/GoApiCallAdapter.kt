package kz.ildar.sandbox.data.go

import okhttp3.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kz.ildar.sandbox.data.go.statebar.GoApiCallResultObserver

class GoApiResponse<T : Any>(
    val dto: T,
    val code: Int,
    val headers: Headers
)

interface GoApiCall<T : Any> {
    /**
     * @throws GoApiException when request finished with exception
     **/
    suspend fun singleRequest(): GoApiResponse<T>
}

sealed class GoApiException(report: String?) : RuntimeException(report)

class GoApiHttpException(
    val code: Int,
    val headers: Headers
) : GoApiException("$code")

class GoApiOtherException(val original: Throwable) : GoApiException(original.message)

class Headers(private val rawHeaders: Map<String, List<String>>) {
    fun header(key: String): String? = rawHeaders[key]?.firstOrNull()

    fun headers(key: String): List<String>? = rawHeaders[key]
}

class GoApiCallAdapter<T : Any>(
    private val callFactory: Call.Factory,
    private val responseType: Type,
    private val observer: GoApiCallResultObserver

) : CallAdapter<T, GoApiCall<T>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: retrofit2.Call<T>): GoApiCall<T> {
        return GoApiCallImpl(callFactory, call, observer)
    }
}

class GoApiCallAdapterFactory(private val observer: GoApiCallResultObserver) : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // check for suspend
        if (
            retrofit2.Call::class.java == getRawType(returnType)
            && returnType is ParameterizedType
            && getRawType(getParameterUpperBound(0, returnType)) == GoApiCall::class.java
        ) {
            throw IllegalStateException("Don't use suspend with GoApiCall")
        }

        if (getRawType(returnType) != GoApiCall::class.java) {
            return null
        }

        if (returnType !is ParameterizedType) {
            throw IllegalStateException("GoApiCall missing generic type!")
        }

        return GoApiCallAdapter<Any>(
            retrofit.callFactory(),
            getParameterUpperBound(0, returnType),
            observer
        )
    }
}

