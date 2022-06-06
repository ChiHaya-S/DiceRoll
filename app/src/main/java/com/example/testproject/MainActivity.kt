package com.example.testproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.example.testproject.databinding.ActivityMainBinding
import java.security.SecureRandom


class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MESSAGE = "com.example.testactivitytransdata.MESSAGE"
    }

    private lateinit var binding: ActivityMainBinding
    private val spinnerItems = arrayOf("6","8","10","100")

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val res = it.data?.getStringExtra(EXTRA_MESSAGE)
                binding.textView.text = res
            }
        }

    private fun roll(num: Int = 1,size: Int = 6):Pair<Any,Int> {
        val secureRandom = SecureRandom()
        val result = StringBuilder()
        var sum = 0
        val mp: MediaPlayer = MediaPlayer.create(this,R.raw.nc48133)
        mp.start()
        for (i in 1..num) {
            val me = secureRandom.nextInt(size) + 1
            result.append(me)
            sum += me
            if (i != num) {
                result.append(", ")
            }
        }
        Thread.sleep(1000)
        mp.release() //解放
        return Pair(result, sum)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val adapter = ArrayAdapter(applicationContext,
        //    android.R.layout.simple_spinner_item, spinnerItems)

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // spinner に adapter をセット
        // View Binding
        //binding.spinner.adapter = adapter

        //val spinner: Spinner = findViewById(R.id.spinner)
        //spinner.adapter = adapter

        var log = List(100){"0 d 0 ： 合計 0"}
        binding.editText.setText("1")
        binding.editText2.setText("6")

        binding.button.setOnClickListener {
            val dNum: Int=binding.editText.text.toString().toInt()
            //val dType: Int=spinner.selectedItem.toString().toInt()
            val dType: Int=binding.editText2.text.toString().toInt()
            val (res, sum) = roll(dNum,dType)
            if (dNum > 1) {
                binding.textView.text = res as CharSequence?
            }
            binding.numS.text = sum.toString()
            val b = listOf("${dNum} d ${dType} ： 合計 ${sum}")
            log = b + log.subList(0,99)
        }

        binding.button2.setOnClickListener {
            val dNum: Int=binding.editText.text.toString().toInt()
            //val dType: Int=spinner.selectedItem.toString().toInt()
            val dType: Int=binding.editText2.text.toString().toInt()
            // "DataStore"という名前でインスタンスを生成
            val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            // 文字列を"Input"に書き込む
            val editor = sharedPref.edit()
            editor.putString("Input", "${dNum},${dType}")

            editor.apply()
        }

        binding.button3.setOnClickListener{
            val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            // "Input"から読み出す
            val str = sharedPref.getString("Input", "1,6")

            if (str != null) {
                val arr = str.split(",")
                binding.editText.setText(arr[0])
                //spinner.setSelection(arr[1].toInt())
                binding.editText2.setText(arr[1])
            }
        }

        binding.logbutton.setOnClickListener {
            val intent = Intent(applicationContext, SubActivity::class.java)

            intent.putExtra(EXTRA_MESSAGE, log.joinToString(separator = "\n"))
            getResult.launch(intent)
        }
    }
}