package com.itcluster.mobile.feature.list.presentation

import com.itcluster.mobile.domain.network.ItClusterSDK
import com.itcluster.mobile.domain.network.models.auth.LoginReq
import com.itcluster.mobile.feature.list.model.AuthStore
import dev.icerock.moko.mvvm.livedata.*
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.units.TableUnitItem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val authStore: AuthStore
//    private val strings: Strings,
//    private val unitsFactory: UnitsFactory<T>
) : ViewModel() {

    private val sdk: ItClusterSDK by lazy { ItClusterSDK() }

    private val mainScope = MainScope()

    val counter: LiveData<String> get() = _counter.map { it.toString() }
    val isSuccess: LiveData<String> get() = _isSuccess

    private val _counter: MutableLiveData<Int> = MutableLiveData(0)
    private val _isSuccess: MutableLiveData<String> = MutableLiveData("")


//    private val _state: MutableLiveData<ResourceState<List<T>, Throwable>> =
//        MutableLiveData(initialValue = ResourceState.Empty())
//
//    val state: LiveData<ResourceState<List<TableUnitItem>, StringDesc>> = _state
//        .dataTransform {
//            map { news ->
//                news.map { unitsFactory.createTile(it) }
//            }
//        }
//        .errorTransform {
//            // new type inferrence require set types oO
//            map { it.message?.desc() ?: strings.unknownError.desc() }
//        }

    fun onCounterButtonPressed() {
        val current = _counter.value
        _counter.value = current + 1
    }

    fun onCreated() {
        authStore.accessToken?.also {
            _isSuccess.value = it
        }
    }
//
//    fun onRetryPressed() {
//        loadList()
//    }

//    fun onRefresh(completion: () -> Unit) {
//        viewModelScope.launch {
//            @Suppress("TooGenericExceptionCaught") // ktor on ios fail with Throwable when no network
//            try {
//                val items = listSource.getList()
//
//                _state.value = items.asState()
//            } catch (error: Exception) {
//                Napier.e("can't refresh", throwable = error)
//            } finally {
//                completion()
//            }
//        }
//    }
//
//    private fun loadList() {
//        _state.value = ResourceState.Loading()
//
//        viewModelScope.launch {
//            @Suppress("TooGenericExceptionCaught") // ktor on ios fail with Throwable when no network
//            try {
//                val items = listSource.getList()
//
//                _state.value = items.asState()
//            } catch (error: Exception) {
//                _state.value = ResourceState.Failed(error)
//            }
//        }
//    }

    interface UnitsFactory<T> {
        fun createTile(data: T): TableUnitItem
    }

    interface Strings {
        val unknownError: StringResource
    }
}