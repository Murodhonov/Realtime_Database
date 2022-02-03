package uz.umarxon.realtimedatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.umarxon.realtimedatabase.databinding.ActivityMainBinding
import uz.umarxon.realtimedatabase.models.User

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var reference:DatabaseReference
    lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")

        /*binding.save.setOnClickListener{
            val key = reference.push().key
            val user = User(key,binding.etName.text.toString(),binding.etNumber.text.toString())

            reference.child(key!!).setValue(user)
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()

        }*/



    }
}