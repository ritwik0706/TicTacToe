package com.example.ritwikjha.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    private var mAuth:FirebaseAuth?=null

    private var database=FirebaseDatabase.getInstance()
    private var myref=database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        mAuth=FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        LoadMain()
    }

    fun buLoginEvent(view: View){
        LoginToFirebase(etEmail.text.toString(),etPass.text.toString())
    }

    fun LoginToFirebase(email:String,password:String){
        mAuth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext,"Login Successful",Toast.LENGTH_SHORT).show()

                var currentUser=mAuth!!.currentUser
                //save in database

                if (currentUser!=null){
                    myref.child("Users").child(SplitString(currentUser.email.toString())).child("Request").setValue(currentUser.uid)
                }

                LoadMain()
            }else{
                Toast.makeText(applicationContext,"Login Failed",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun LoadMain(){
        var currentUser=mAuth!!.currentUser

        if (currentUser!=null){
            var intent=Intent(this,MainActivity::class.java)
            intent.putExtra("email",currentUser.email)
            intent.putExtra("uid",currentUser.uid)

            startActivity(intent)
        }
    }

    fun SplitString(str:String): String {
        var split=str.split("@")
        return split[0]
    }
}
