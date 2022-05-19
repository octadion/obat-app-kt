package com.octadion.tugas_akhir.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.octadion.tugas_akhir.fragment.ChangePasswordFragmentDirections
import com.octadion.tugas_akhir.R
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        layoutPassword.visibility = View.VISIBLE
        layoutNewPassword.visibility = View.GONE
        btnAuth.setOnClickListener {
            val password = etPassword.text.toString().trim()
            if(password.isEmpty()){
                etPassword.error = "Password harus diisi!"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            user?.let {
                val userCredential = EmailAuthProvider.getCredential(it.email!!, password)
                it.reauthenticate(userCredential).addOnCompleteListener {
                    if(it.isSuccessful){
                        layoutPassword.visibility = View.GONE
                        layoutNewPassword.visibility = View.VISIBLE
                    } else if(it.exception is FirebaseAuthInvalidCredentialsException){
                        etPassword.error = "Password salah!"
                        etPassword.requestFocus()
                    } else {
                        Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            btnUpdate.setOnClickListener {view->
                val newPassword = etNewPasssword.text.toString().trim()
                val newPasswordConfirm = etNewPassswordConfirm.text.toString().trim()
                if (newPassword.isEmpty() || newPassword.length < 6 ) {
                    etNewPasssword.error = "Password lebih dari 6 karakter!"
                    etNewPasssword.requestFocus()
                    return@setOnClickListener
                }
                if(newPassword != newPasswordConfirm){
                    etNewPassswordConfirm.error = "Password tidak sama!"
                    etNewPassswordConfirm.requestFocus()
                    return@setOnClickListener
                }
                user.let {
                    user?.updatePassword(newPassword)?.addOnCompleteListener {
                        if(it.isSuccessful){
                            val actionPasswordChanged =
                                ChangePasswordFragmentDirections.actionPasswordChanged()
                            Navigation.findNavController(view).navigate(actionPasswordChanged)
                            Toast.makeText(activity, "Password berhasil diganti", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

}