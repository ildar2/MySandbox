package kz.ildar.sandbox.acm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.di.CoroutineProvider
import kz.ildar.sandbox.ui.BaseViewModel

class Problem {
    interface A {
        fun foo()
    }

    internal class InternalA : A {
        override fun foo() {}
        internal fun secret() {}
    }

    open class Helper(a: A) : A by a {
        internal val realA = a as InternalA
    }

    class B : Helper(InternalA()) {
        fun bar() {
            realA.secret()
        }
    }
}

class Solution1 {

    interface Confirmation {
        val confLiveData: LiveData<String>
        fun getConfirmation()
    }

    internal class ConfirmationImpl : Confirmation {
        override val confLiveData: MutableLiveData<String> = MutableLiveData()
        override fun getConfirmation() {
            confLiveData.postValue("got conf")
        }
    }

    class MyViewModel : BaseViewModel(CoroutineProvider()), Confirmation {

        private val confirmation by lazy { ConfirmationImpl() }
        override val confLiveData: LiveData<String> = confirmation.confLiveData
        override fun getConfirmation() {
            makeRequest({ confirmation.getConfirmation() })
        }
    }

    class Fragment {
        fun onCreate() {
            val viewModel = MyViewModel()
            viewModel.getConfirmation()
            viewModel.confLiveData
        }
    }
}