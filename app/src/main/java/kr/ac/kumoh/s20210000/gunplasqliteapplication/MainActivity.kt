package kr.ac.kumoh.s20210000.gunplasqliteapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import kr.ac.kumoh.s20210000.gunplasqliteapplication.databinding.ActivityMainBinding
import kr.ac.kumoh.s20210000.gunplasqliteapplication.databinding.DialogAddBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val model: GunplaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            addItem()
        }

//        model.insert(Mechanic(
//            0,
//            "건담",
//            "RX-78-2",
//            "Earth Federation",
//            "Luna Titanium",
//            18.0,
//            43.4
//        ))
    }

    private fun addItem() {
        val binding = DialogAddBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setView(binding.root)
            .setPositiveButton("추가") { _, _ ->
                //val name = binding.name.text.toString()
                //Log.i("Dialog", name)

                if (binding.name.text.toString().isNotEmpty() &&
                    binding.model.text.toString().isNotEmpty())
                    model.insert(Mechanic(
                        0,
                        binding.name.text.toString(),
                        binding.model.text.toString(),
                        binding.manufacturer.text.toString(),
                        binding.armor.text.toString(),
                        binding.height.text.toString().toDouble(),
                        binding.weight.text.toString().toDouble()
                    ))
            }
            .create().show()
    }
}