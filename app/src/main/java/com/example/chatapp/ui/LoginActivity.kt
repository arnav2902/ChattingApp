package com.example.chatapp.ui


import android.content.DialogInterface
import android.content.Intent

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.chatapp.R
import com.google.android.gms.auth.api.credentials.*

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_login.*


const val PHONE_NUMBER = "phoneNo"
private const val PHONE_REQUEST = 1;

class LoginActivity : AppCompatActivity() {

    lateinit var phoneNumber: String
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHONE_REQUEST) {
            if (data != null) {
                val credential: Credential? = data.getParcelableExtra(Credential.EXTRA_KEY)
                 if(credential?.id != null){
                     phoneNumber = credential.id.toString().substring(3)
                     phoneNumberEt.setText(phoneNumber)
                 }else{
                     phoneNumberEt.addTextChangedListener {
                         nextBtn.isEnabled = ccp.isValidFullNumber
                     }
                 }

            }
        }else if (requestCode == PHONE_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            phoneNumber = phoneNumberEt.text.toString()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ccp.registerCarrierNumberEditText(phoneNumberEt)
        requestNumber()
        phoneNumberEt.addTextChangedListener {
            nextBtn.isEnabled = ccp.isValidFullNumber
        }

        nextBtn.setOnClickListener {
            phoneNumber = ccp.fullNumberWithPlus
            notify(ccp.fullNumberWithPlus)
        }

    }

    private fun requestNumber() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .setEmailAddressIdentifierSupported(false)
            .build()
        val options = CredentialsOptions.Builder()
            .forceEnableSaveDialog()
            .build()
        val pendingIntent = Credentials.getClient(this, options).getHintPickerIntent(hintRequest)
        startIntentSenderForResult(pendingIntent.intentSender, PHONE_REQUEST, null, 0, 0, 0)
    }

    private fun notify(phoneNumber: String) {
        //phoneNumber = ccp.fullNumberWithPlus.toString()
        MaterialAlertDialogBuilder(this).apply {
            setMessage(
                "\"We will be verifying the phone number:$phoneNumber \" \n" +
                        "\"Is this okay, or would you like to edit the number?\""
            )
            setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                startOtpActivity()
            })
            setNegativeButton("Edit") { dialog, which ->
                dialog.dismiss()
            }
            setCancelable(false)
            create()
            show()
        }

    }

    private fun startOtpActivity() {
        startActivity(
            Intent(this@LoginActivity, OtpActivity::class.java).putExtra(
                PHONE_NUMBER, phoneNumber
            )
        )
        finish()
    }
}