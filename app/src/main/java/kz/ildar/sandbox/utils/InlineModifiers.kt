package kz.ildar.sandbox.utils

class A {
    val publicValue = "public"
    private val privateValue = "private"
    @PublishedApi
    internal val internalValue = "internal"

    //notice that aLambda is marked as noinline.
    inline fun higherOrderFunction(noinline lambda1: () -> Unit, lambda2: () -> Unit, crossinline lambda3: () -> Unit) {
        publicValue
//        privateValue //can't access private from inline
        internalValue

        println("--==calling lambdas")
        lambda1()
        lambda2()
        lambda3()

        println("--==calling normalFunction with context")
        normalFunction {
            lambda1.invoke()
//            lambda2.invoke()
            lambda3.invoke()
        }

        println("--==calling normalFunction")
        normalFunction(lambda1)
//        normalFunction(lambda2)
//        normalFunction(lambda3)

        print("I won't be executed when you call callingFunction()")
    }

    fun normalFunction(aLambda: () -> Unit) {
        aLambda.invoke()
        return
    }

    fun callingFunction() {
        higherOrderFunction({
            publicValue
            privateValue
            internalValue
            println("lambda1 called")
//            return //Can't return here since this lambda is noinline
        }, {
            publicValue
            privateValue
            internalValue
            println("lambda2 called")
            return
        }, {
            publicValue
            privateValue
            internalValue
            println("lambda3 called")
//            return //Can't return here since this lambda is crossinline
        })
    }
}

fun main() {
    A().callingFunction()
}