package com.itcluster.mobile.app.ext

import android.Manifest.permission
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionsUtil {

    companion object {

        /**
         * Код запроса разрешения на запись в файловое хранилище и к камере
         */
        const val STORAGE_CAM_PERMISSION_REQUEST = 1801

        /**
         * Код запроса разрешения на чтение телефонной книги
         */
        const val READ_CONTACTS_PERMISSION_REQUEST = 1802

        /**
         * Код запроса разрешения на запись в файловое хранилище
         */
        const val STORAGE_PERMISSION_REQUEST = 1803

        /**
         * Проверяет наличие разрешений на запись в файловое хранилище и к камере
         * @param fragment фрагмент, в котором осуществляется проверка
         */
        fun checkStorageCamPermissions(fragment: Fragment): Boolean {
            return if (fragment.hasStorageCamPermission()) {
                true
            } else {
                val permissions = arrayOf(
                    permission.READ_EXTERNAL_STORAGE,
                    permission.WRITE_EXTERNAL_STORAGE,
                    permission.CAMERA
                )
                fragment.requestPermissions(permissions, STORAGE_CAM_PERMISSION_REQUEST)
                false
            }
        }

        /**
         * Флаг наличия разрешений на запись в файловое хранилище и к камере в текущем фрагменте
         */
        fun Fragment.hasStorageCamPermission() =
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

        /**
         * Проверяет наличие разрешений на запись в файловое хранилище
         * @param activity активити, в которой осуществляется проверка
         */
        fun checkStoragePermission(activity: Activity): Boolean {
            return if (ContextCompat.checkSelfPermission(
                    activity,
                    permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    activity, permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                val permissions = arrayOf(
                    permission.READ_EXTERNAL_STORAGE,
                    permission.WRITE_EXTERNAL_STORAGE
                )
                requestPermissions(activity, permissions, STORAGE_PERMISSION_REQUEST)
                false
            }
        }

        /**
         * Показывает диалог на запрос разрешения к контактной книге телефона
         * @param fragment фрагмент, в котором осуществляется запуск диалога
         */
        fun showReadContactsPermissionDialog(fragment: Fragment) {
            val permissions = arrayOf(permission.READ_CONTACTS)
            fragment.requestPermissions(permissions, READ_CONTACTS_PERMISSION_REQUEST)
        }
    }
}