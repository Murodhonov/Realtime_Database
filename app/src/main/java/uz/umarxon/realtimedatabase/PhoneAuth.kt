package uz.umarxon.realtimedatabase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import uz.umarxon.realtimedatabase.databinding.ActivityPhonteAuthBinding
import java.util.concurrent.TimeUnit

class PhoneAuth : AppCompatActivity() {

    lateinit var binding: ActivityPhonteAuthBinding
    lateinit var auth: FirebaseAuth
    private val TAG = "FragmentReciever"
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhonteAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.etNumber.visibility = View.GONE
        binding.send.visibility = View.GONE

        binding.send.setOnClickListener {
            sendVerificationCode(binding.etNumber.text.toString())
            Toast.makeText(this, "Yuborilmoqda..", Toast.LENGTH_SHORT).show()
        }

        binding.phone.setOnClickListener{
                binding.phone.visibility = View.GONE
                binding.google.visibility = View.GONE
                binding.send.visibility = View.VISIBLE
                binding.etNumber.visibility = View.VISIBLE
            }



    }

    override fun onBackPressed() {
        if (binding.phone.visibility == View.VISIBLE){
            super.onBackPressed()
        }else{
            binding.phone.visibility = View.VISIBLE
            binding.send.visibility = View.GONE
            binding.etNumber.visibility = View.GONE
            binding.google.visibility = View.VISIBLE
        }
    }

    fun verifyCode(){
        val code = binding.etNumber.text.toString()
        if (code.length == 6){
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    Toast.makeText(this, "Muvaffaqiyatli", Toast.LENGTH_SHORT).show()

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Muvaffaqiyatsiz!!!", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this, "Kod xato kiritildi", Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
                }
            }
    }

    private fun resentCode(phoneNimber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNimber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(resendToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}