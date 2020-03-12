# MySandbox

Check out and make use of [ApiCaller](https://github.com/ildar2/MySandbox/blob/master/app/src/main/java/kz/ildar/sandbox/data/ApiCaller.kt): CoroutineCaller implementation which handles all network errors with grace

Check out and make use of [UiCaller](https://github.com/ildar2/MySandbox/blob/master/app/src/main/java/kz/ildar/sandbox/ui/UiCaller.kt) implementation - it manages kotlin coroutine api requests automatically via `makeRequest()` function. Fragments just have to observe `statusLiveData` for `SHOW_LOADING` and `HIDE_LOADING` events

Check out and make use of [DisplayAdapter](https://github.com/ildar2/MySandbox/blob/master/app/src/main/java/kz/ildar/sandbox/utils/DisplayAdapter.kt) - it can manage complex lists by handing control to items
