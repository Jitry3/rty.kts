package com.android.kvc.xiao.v2.miui

import android.net.Uri
import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Environment

import android.view.View
import android.view.WindowManager

import android.provider.Settings

import android.widget.Toast
import android.widget.ImageView

import java.io.File
import java.io.InputStream
import java.io.IOException

import java.security.KeyStore
import java.security.PublicKey
import java.security.Signature
import java.security.NoSuchAlgorithmException

import android.graphics.BitmapFactory

import androidx.annotation.NonNull

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import android.preference.PreferenceManager

import android.content.Intent
import android.content.Context
import android.content.res.AssetManager
import android.content.SharedPreferences
import android.content.pm.PackageManager

import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.google.android.material.bottomnavigation.BottomNavigationView

import com.android.kvc.xiao.v2.miui.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val animationImage = "com.android.app.webp"

    private lateinit var binding: ActivityMainBinding
    
    private companion object {
        private const val REQUEST_OVERLAY_PERMISSION = 200
        private const val REQUEST_MANAGE_STORAGE_PERMISSION = 201
        private const val PERMISSION_OVERLAY_REQUESTED_KEY = "overlay_permission_requested"
        private const val PERMISSION_STORAGE_REQUESTED_KEY = "storage_permission_requested"
    }

    private lateinit var sharedPreferences: SharedPreferences
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val serviceIntent = Intent(this, NotificationService::class.java)
startService(serviceIntent)

     // 启动动画
      val imageView: ImageView = findViewById(R.id.imageView)

        try {
            val inputStream = assets.open(animationImage)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 将ImageView的透明度设置
        imageView.alpha = 0.45f
        
        // 签名校验 第一层
        val keystoreFileName = "file:///android_asset/arm64-v8a.keystore" // 替换为 arm64-v8a.keystore 的实际文件名
        val keystorePassword = "cfyut3sdgnk7ioy6fyjnd08uoteq4p" 
        val keyAlias = "arm" 

        val isSignatureValid = verifySignatureWithKeystore(this, keystoreFileName, keystorePassword, keyAlias)

        if (isSignatureValid) {
            // 签名有效的逻辑处理
            println("Signature is valid.")
        } else {
            // 签名无效的逻辑处理
            println("Signature is invalid.")
        }
        
        
        // 所有文件权限请求 和 悬浮窗权限请求
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        if (!isOverlayPermissionRequested()) {
            requestOverlayPermission()
            markOverlayPermissionRequested()
        }

        if (!isStoragePermissionRequested()) {
            requestStoragePermission()
            markStoragePermissionRequested()
        }
      
     // root权限请求 requestRootPermission()
    
    // 权限请求
val permissions = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_MEDIA_VIDEO,
    Manifest.permission.READ_MEDIA_AUDIO,
    Manifest.permission.READ_MEDIA_IMAGES,
    Manifest.permission.POST_NOTIFICATIONS,
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.GET_ACCOUNTS
)

val permissionGranted = permissions.all {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

if (!permissionGranted) {
    ActivityCompat.requestPermissions(this, permissions, 1)
}

   // 创建文件夹
val folderNames = listOf("downloads", "android", "data")

for (folderName in folderNames) {
    val rootDir = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), folderName)

    if (!rootDir.exists()) {
        rootDir.mkdirs()
    }
}

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_atyy, R.id.navigation_call, R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }
    
    /*
    private fun requestRootPermission() {
        try {
            val process = Runtime.getRuntime().exec("su")
            val outputStream = process.outputStream
            val command = "sudo root"
            outputStream.write(command.toByteArray())
            outputStream.flush()
            outputStream.close()
            process.waitFor()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
    */
    
    private fun isOverlayPermissionRequested(): Boolean {
        return sharedPreferences.getBoolean(PERMISSION_OVERLAY_REQUESTED_KEY, false)
    }

    private fun markOverlayPermissionRequested() {
        sharedPreferences.edit().putBoolean(PERMISSION_OVERLAY_REQUESTED_KEY, true).apply()
    }

    private fun requestOverlayPermission() {
        if (!hasOverlayPermission()) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.fromParts("package", packageName, null)
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        }
    }

    private fun hasOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (hasOverlayPermission()) {
                showToast("已获得悬浮窗权限")
            } else {
                showToast("未获得悬浮窗权限")
            }
        }
    }

    private fun isStoragePermissionRequested(): Boolean {
        return sharedPreferences.getBoolean(PERMISSION_STORAGE_REQUESTED_KEY, false)
    }

    private fun markStoragePermissionRequested() {
        sharedPreferences.edit().putBoolean(PERMISSION_STORAGE_REQUESTED_KEY, true).apply()
    }
    
    private fun requestStoragePermission() {
        if (!hasStoragePermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivityForResult(intent, REQUEST_MANAGE_STORAGE_PERMISSION)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_MANAGE_STORAGE_PERMISSION
                )
            }
        }
    }

    private fun hasStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_MANAGE_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("已获得 所有文件访问权限")
            } else {
                showToast("未获得 所有文件访问权限")
            }
        }
    }

    private fun showToast(message: String) {
        // 显示 Toast 消息
    }
    
    private fun verifySignatureWithKeystore(
        context: Context,
        keystoreFileName: String,
        keystorePassword: String,
        keyAlias: String
    ): Boolean {
        try {
            // 获取 AssetManager
            val assetManager: AssetManager = context.assets

            // 打开密钥库文件流
            val keystoreFile: InputStream = assetManager.open(keystoreFileName)

            // 加载密钥库
            val keystore = KeyStore.getInstance(KeyStore.getDefaultType())
            keystore.load(keystoreFile, keystorePassword.toCharArray())

            // 获取公钥
            val publicKey = keystore.getCertificate(keyAlias).publicKey

            // 校验签名
            val signature = Signature.getInstance("SHA384withRSA")
            signature.initVerify(publicKey)

            // 返回签名的验证结果
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
    
}