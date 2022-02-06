package com.itcluster.mobile.app.presentation.view

import android.Manifest
import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.itcluster.mobile.app.R
import com.itcluster.mobile.app.core.utils.viewBinding
import com.itcluster.mobile.app.databinding.ActivityRootBinding
import com.itcluster.mobile.app.models.IncomingMessage
import com.itcluster.mobile.app.models.ResponsePayload
import com.itcluster.mobile.app.presentation.view.base.BaseActivity
import com.itcluster.mobile.kmm.shared.GymMindSDK
import com.itcluster.mobile.kmm.shared.cache.DatabaseDriverFactory
import com.itcluster.mobile.kmm.shared.network.AppSocket
import com.fasterxml.jackson.databind.ObjectMapper
import com.itcluster.mobile.app.ext.log.LogSniffer
import com.itcluster.mobile.app.ext.shakedetector.ShakeDetector
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val mainScope = MainScope()

    private val vb by viewBinding(ActivityRootBinding::inflate)

    private lateinit var launchesRecyclerView: RecyclerView
    private lateinit var progressBarView: FrameLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val sdk = GymMindSDK(
        DatabaseDriverFactory(this),
        AppSocket(SOCKET_ENDPOINT)
    )

    private var shakeDetector: ShakeDetector? = null

    private val storagePermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::onPermissionResult
    )

    private val objectMapper = ObjectMapper()

    private val jsonGreering = "{\n" +
            "    \"from\":\"Bugtsa\",\n" +
            "    \"text\":\"Hey, Chelovek\"\n" +
            "}"

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GymMind)
        super.onCreate(savedInstanceState)
        title = "Bugtsa Test"
        setContentView(vb.root)
        vb.navigationBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {

                }
                R.id.navigation_levels -> {

                }
                R.id.navigation_profile -> {

                }
            }
            true
        }

        shakeDetector = ShakeDetector(getSystemService(SENSOR_SERVICE) as SensorManager) {
            requestWriteStoragePermissions()
        }
//        }
//        swipeRefreshLayout.setOnRefreshListener {
//            swipeRefreshLayout.isRefreshing = false
//            displayLaunches(true)
//        }
//        displayLaunches(false)
//        showGameState()
    }

    override fun onResume() {
        super.onResume()
        shakeDetector?.registerListener()
    }

    override fun onPause() {
        super.onPause()
        shakeDetector?.unregisterListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    open fun showDrawer(viewLifecycleOwner: LifecycleOwner, anchor: View, context: Context) {
        vb.rootLayout.openDrawer(GravityCompat.START)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            android.R.id.home -> {
                vb.rootLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun requestWriteStoragePermissions() {
        storagePermissionResult.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            performSendLogs()
        } else {
            showToast("Дайте разрешения на запись файла, чтобы его отправить")
        }
    }

    private fun performSendLogs() {
        LogSniffer.apply {
            sendLogs(this@MainActivity)
        }
    }

    private fun initDrawer() {
        vb.rootLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun initNavigation() {
        vb.navigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            vb.rootLayout.close()
        }
    }

//    @SuppressLint("NotifyDataSetChanged")
//    private fun displayLaunches(needReload: Boolean) {
//        progressBarView.isVisible = true
//        mainScope.launch {
//            kotlin.runCatching {
//                sdk.getLaunches(needReload)
//            }.onSuccess {
//                launchesRvAdapter.launches = it
//                launchesRvAdapter.notifyDataSetChanged()
//            }.onFailure {
//                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_SHORT).show()
//            }
//            progressBarView.isVisible = false
//        }
//    }

    private fun initSocket() {
        val appSocket = sdk.getSocket()

        appSocket.stateListener = {
            Timber.tag(TAG).d("state: $it")
            if (it == AppSocket.State.CLOSED) {
                Timber.tag(TAG).d("start after state: $it")
                connectToWs(appSocket)
            }
            if (it == AppSocket.State.CONNECTED) {
                appSocket.send(jsonGreering)
            }
        }

        appSocket.messageListener = { payload ->
            val response = try {
                ResponsePayload.IncomingText(
                    objectMapper.readValue(
                        payload,
                        IncomingMessage::class.java
                    )
                )
            } catch (e: Exception) {
                ResponsePayload.IncomingString(payload)
            }

            GlobalScope.launch(Dispatchers.Main) {
                Timber.tag(TAG).d("New message: $response")
                showToast("Message from server: $response")
            }
        }
    }

    private fun connectToWs(appSocket: AppSocket) {
        try {
            Timber.tag(TAG).d("try connect to server")
            appSocket.connect()
            Timber.tag(TAG).d("error state at connect: ${appSocket.socketError}")
        } catch (e: Exception) {
            Timber.tag(TAG).e("connect error: $e")
        }
    }

    private fun showToast(message: String?) {
        message?.also {
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        const val SOCKET_ENDPOINT = "ws://10.0.2.2:9055/websocket"

        private const val TAG = "GYM_MIND_MAIN_TAG"
    }
}