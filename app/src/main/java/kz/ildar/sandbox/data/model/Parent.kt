package kz.ildar.sandbox.data.model

open class Parent {
    open fun sayMyName(): String {
        return "I am Parent"
    }
}

class Child : Parent() {
    override fun sayMyName(): String {
        return "I am Child"
    }
}